package com.blog.controller.admin;

import com.blog.entity.Blog;
import com.blog.entity.BlogType;
import com.blog.entity.Blogger;
import com.blog.entity.Link;
import com.blog.service.BlogService;
import com.blog.service.BlogTypeService;
import com.blog.service.BloggerService;
import com.blog.service.LinkService;
import com.blog.util.ResponseUtil;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
@RequestMapping({"/admin/system"})
public class SystemAdminController {
    @Resource
    private BloggerService bloggerService;
    @Resource
    private BlogTypeService blogTypeService;
    @Resource
    private BlogService blogService;
    @Resource
    private LinkService linkService;

    //刷新系统缓存
    @RequestMapping({"/refreshSystem"})
    public String refreshSystem(HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        ServletContext application = RequestContextUtils.getWebApplicationContext(request).getServletContext();
        Blogger blogger = this.bloggerService.find();
        blogger.setPassword(null);
        application.setAttribute("blogger", blogger);

        //博客雷彪
        List<BlogType> blogTypeCountList = this.blogTypeService.countList();
        application.setAttribute("blogTypeCountList", blogTypeCountList);

        //按年月分类的博客数量
        List<Blog> blogCountList = this.blogService.countList();
        application.setAttribute("blogCountList", blogCountList);

        //友情链接
        List<Link> linkList = this.linkService.list(null);
        application.setAttribute("linkList", linkList);

        JSONObject result = new JSONObject();
        result.put("success", Boolean.TRUE);
        ResponseUtil.write(response, result);
        return null;
    }
}



