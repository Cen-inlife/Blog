package com.blog.controller;

import com.blog.entity.Blog;
import com.blog.lucene.BlogIndex;
import com.blog.service.BlogService;
import com.blog.service.CommentService;
import com.blog.util.StringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/blog"})
public class BlogController {
    @Resource
    private BlogService blogService;
    @Resource
    private CommentService commentService;
    private BlogIndex blogIndex = new BlogIndex();

    @RequestMapping({"/articles/{id}"})
    public ModelAndView details(@PathVariable("id") Integer id, HttpServletRequest request)
            throws Exception {
        ModelAndView mav = new ModelAndView();

        //根据主键查询博客信息
        Blog blog = this.blogService.findById(id);
        //处理关键字
        String keyWords = blog.getKeyWord();
        if (StringUtil.isNotEmpty(keyWords)) {
            String[] arr = keyWords.split(" ");
            mav.addObject("keyWords", StringUtil.filterWhite(Arrays.asList(arr)));
        } else {
            mav.addObject("keyWords", null);
        }
        mav.addObject("blog", blog);
        //点击数+1
        blog.setClickHit(blog.getClickHit() + 1);
        //更新
        this.blogService.update(blog);
        //显示评论
        Map<String, Object> map = new HashMap();
        map.put("blogId", blog.getId());
        map.put("state", 1);
        mav.addObject("commentList", this.commentService.list(map));
        //上一篇 下一篇
        mav.addObject("pageCode", genUpAndDownPageCode(this.blogService.getLastBlog(id), this.blogService.getNextBlog(id), request.getServletContext().getContextPath()));
        mav.addObject("mainPage", "foreground/blog/view.jsp");
        mav.addObject("pageTitle", blog.getTitle() + "_个人博客系统");
        //主页
        mav.setViewName("mainTemp");
        return mav;
    }

    //搜索
    @RequestMapping({"/q"})
    public ModelAndView search(@RequestParam(value = "q", required = false) String q, @RequestParam(value = "page", required = false) String page, HttpServletRequest request)
            throws Exception {
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("mainPage", "foreground/blog/result.jsp");
        //从lucene中查询
        List<Blog> blogList = this.blogIndex.searchBlog(q.trim());
        Integer toIndex = Integer.valueOf(blogList.size() >= Integer.parseInt(page) * 10 ? Integer.parseInt(page) * 10 : blogList.size());
        mav.addObject("blogList", blogList.subList((Integer.parseInt(page) - 1) * 10, toIndex));
        mav.addObject("pageCode", genUpAndDownPageCode(Integer.parseInt(page), blogList.size(), q, 10, request.getServletContext().getContextPath()));
        mav.addObject("q", q);
        mav.addObject("resultTotal", blogList.size());
        mav.addObject("pageTitle", "搜索关键字'" + q + "'结果页面_个人博客系统");
        mav.setViewName("mainTemp");
        return mav;
    }

    //上一篇 下一篇
    private String genUpAndDownPageCode(Blog lastBlog, Blog nextBlog, String projectContext) {
        StringBuffer pageCode = new StringBuffer();
        if ((lastBlog == null) || (lastBlog.getId() == null)) {
            pageCode.append("<p>上一篇：没有了</p>");
        } else {
            pageCode.append("<p>上一篇：<a href='" + projectContext + "/blog/articles/" + lastBlog.getId() + ".html'>" + lastBlog.getTitle() + "</a></p>");
        }
        if ((nextBlog == null) || (nextBlog.getId() == null)) {
            pageCode.append("<p>下一篇：没有了</p>");
        } else {
            pageCode.append("<p>下一篇：<a href='" + projectContext + "/blog/articles/" + nextBlog.getId() + ".html'>" + nextBlog.getTitle() + "</a></p>");
        }
        return pageCode.toString();
    }

    //查询结果的翻页功能
    private String genUpAndDownPageCode(Integer page, Integer totalNum, String q, Integer pageSize, String projectContext) {
        long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        StringBuffer pageCode = new StringBuffer();
        if (totalPage == 0L) {
            return "";
        }
        pageCode.append("<nav>");
        pageCode.append("<ul class='pager' >");
        //当前页大于一
        if (page > 1) {
            pageCode.append("<li><a href='" + projectContext + "/blog/q.html?page=" + (page - 1) + "&q=" + q + "'>上一页</a></li>");
        } else {//是第一页
            pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
        }
        if (page < totalPage) {//不是最后一页
            pageCode.append("<li><a href='" + projectContext + "/blog/q.html?page=" + (page + 1) + "&q=" + q + "'>下一页</a></li>");
        } else {//最后一页
            pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
        }
        pageCode.append("</ul>");
        pageCode.append("</nav>");

        return pageCode.toString();
    }
}
