package com.blog.controller.admin;

import com.blog.entity.Link;
import com.blog.entity.PageBean;
import com.blog.service.LinkService;
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

//友情链接管理
@Controller
@RequestMapping({"/admin/link"})
public class LinkAdminController {
    @Resource
    private LinkService linkService;

    //友情链接列表
    @RequestMapping({"/list"})
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, HttpServletResponse response)
            throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        //查询友情链接列表
        List<Link> linkList = this.linkService.list(map);
        //查询总数
        Long total = this.linkService.getTotal(map);
        //将数据写入 response json格式
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(linkList);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    //保存/新增友情链接信息
    @RequestMapping({"/save"})
    public String save(Link link, HttpServletResponse response)
            throws Exception {
        int resultTotal = 0;
        if (link.getId() == null) {
            //新增
            resultTotal = this.linkService.add(link);
        } else {
            //更新
            resultTotal = this.linkService.update(link);
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

    //删除友情链接
    @RequestMapping({"/delete"})
    public String delete(@RequestParam("ids") String ids, HttpServletResponse response)
            throws Exception {
        //获取传过来的数组
        String[] idsStr = ids.split(",");
        //遍历删除
        for (int i = 0; i < idsStr.length; i++) {
            this.linkService.delete(Integer.valueOf(Integer.parseInt(idsStr[i])));
        }
        JSONObject result = new JSONObject();
        result.put("success", Boolean.valueOf(true));
        ResponseUtil.write(response, result);
        return null;
    }
}



