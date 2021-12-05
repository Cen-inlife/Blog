package com.blog.controller.admin;

import com.blog.entity.Blog;
import com.blog.entity.PageBean;
import com.blog.lucene.BlogIndex;
import com.blog.service.BlogService;
import com.blog.util.ResponseUtil;
import com.blog.util.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 博客信息管理
 */
@Controller
@RequestMapping({"/admin/blog"})
public class BlogAdminController {
    @Resource
    private BlogService blogService;

    //增删博客的同时放入到lucene
    private BlogIndex blogIndex = new BlogIndex();

    /**
     * 保存一条博客信息
     */
    @RequestMapping({"/save"})
    public String save(Blog blog, HttpServletResponse response)
            throws Exception {
        int resultTotal = 0;
        if (blog.getId() == null) {
            //新增
            resultTotal = this.blogService.add(blog);
            //增加博客的同时放入到lucene
            this.blogIndex.addIndex(blog);
        } else {
            //添加
            resultTotal = this.blogService.update(blog);
            this.blogIndex.updateIndex(blog);
        }
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", Boolean.TRUE);
        } else {
            result.put("success", Boolean.FALSE);
        }
        //把结果返回给浏览器
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 查询博客信息列表
     */
    @RequestMapping({"/list"})
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, Blog s_blog, HttpServletResponse response)
            throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap();
        //标题查询
        map.put("title", StringUtil.formatLike(s_blog.getTitle()));
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        //分页查询
        List<Blog> blogList = this.blogService.list(map);
        //获取共有多少条博客
        Long total = this.blogService.getTotal(map);

        //封装到json
        JSONObject result = new JSONObject();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
        //list转成json
        JSONArray jsonArray = JSONArray.fromObject(blogList, jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }


    /**
     *删除博客信息
     */
    @RequestMapping({"/delete"})
    public String delete(@RequestParam("ids") String ids, HttpServletResponse response)
            throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            this.blogService.delete(Integer.parseInt(idsStr[i]));
            //删除博客的同时删除lucene
            this.blogIndex.deleteIndex(idsStr[i]);
        }
        JSONObject result = new JSONObject();
        result.put("success", Boolean.TRUE);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     *根据id查询一条博客信息
     */
    @RequestMapping({"/findById"})
    public String findById(@RequestParam("id") String id, HttpServletResponse response)
            throws Exception {
        Blog blog = this.blogService.findById(Integer.parseInt(id));
        JSONObject jsonObject = JSONObject.fromObject(blog);
        ResponseUtil.write(response, jsonObject);
        return null;
    }
}



