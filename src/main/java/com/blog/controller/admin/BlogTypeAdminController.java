package com.blog.controller.admin;

import com.blog.entity.BlogType;
import com.blog.entity.PageBean;
import com.blog.service.BlogService;
import com.blog.service.BlogTypeService;
import com.blog.util.ResponseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 博客类型管理
 */
@Controller
@RequestMapping({"/admin/blogType"})
public class BlogTypeAdminController {
    @Resource
    private BlogTypeService blogTypeService;
    @Resource
    private BlogService blogService;

    //博客类型列表
    @RequestMapping({"/list"})
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, HttpServletResponse response)
            throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap();
        map.put("start", Integer.valueOf(pageBean.getStart()));
        map.put("size", Integer.valueOf(pageBean.getPageSize()));
        //查询博客类型列表
        List<BlogType> blogTypeList = this.blogTypeService.list(map);
        //查询总共有多少条记录
        Long total = this.blogTypeService.getTotal(map);
        //将数据转换成json
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(blogTypeList);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    //保存、新增博客类别信息
    @RequestMapping({"/save"})
    public String save(BlogType blogType, HttpServletResponse response)
            throws Exception {
        int resultTotal = 0;
        if (blogType.getId() == null) {
            //新增
            resultTotal = this.blogTypeService.add(blogType);
        } else {
            //更新
            resultTotal = this.blogTypeService.update(blogType);
        }
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", Boolean.valueOf(true));
        } else {
            result.put("success", Boolean.valueOf(false));
        }
        ResponseUtil.write(response, result);
        return null;
    }

    //删除博客类别
    @RequestMapping({"/delete"})
    public String delete(@RequestParam("ids") String ids, HttpServletResponse response)
            throws Exception {
        //获得传送过来的数组
        String[] idsStr = ids.split(",");
        JSONObject result = new JSONObject();
        //遍历删除
        for (int i = 0; i < idsStr.length; i++) {
            if (this.blogService.getBlogByTypeId(Integer.parseInt(idsStr[i])) > 0) {
                result.put("exist", "博客类别下有博客，不能删除！");
            } else {
                this.blogTypeService.delete(Integer.parseInt(idsStr[i]));
            }
        }
        result.put("success", Boolean.TRUE);
        ResponseUtil.write(response, result);
        return null;
    }
}



