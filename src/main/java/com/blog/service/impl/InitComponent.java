
package com.blog.service.impl;


import com.blog.entity.Blog;
/*  4:   */ import com.blog.entity.BlogType;
/*  5:   */ import com.blog.entity.Blogger;
/*  6:   */ import com.blog.entity.Link;
/*  7:   */ import com.blog.service.BlogService;
/*  8:   */ import com.blog.service.BlogTypeService;
/*  9:   */ import com.blog.service.BloggerService;
/* 10:   */ import com.blog.service.LinkService;
/* 11:   */ import java.util.List;
/* 12:   */ import javax.servlet.ServletContext;
/* 13:   */ import javax.servlet.ServletContextEvent;
/* 14:   */ import javax.servlet.ServletContextListener;
/* 15:   */ import org.springframework.beans.BeansException;
/* 16:   */ import org.springframework.context.ApplicationContext;
/* 17:   */ import org.springframework.context.ApplicationContextAware;
/* 18:   */ import org.springframework.stereotype.Component;


@Component
public class InitComponent implements ServletContextListener, ApplicationContextAware {
    private static ApplicationContext applicationContext;


    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext application = servletContextEvent.getServletContext();

        BloggerService bloggerService = (BloggerService) applicationContext.getBean("bloggerService");

        Blogger blogger = bloggerService.find();

        blogger.setPassword(null);

        application.setAttribute("blogger", blogger);


        BlogTypeService blogTypeService = (BlogTypeService) applicationContext.getBean("blogTypeService");

        List<BlogType> blogTypeCountList = blogTypeService.countList();

        application.setAttribute("blogTypeCountList", blogTypeCountList);

        BlogService blogService = (BlogService) applicationContext.getBean("blogService");

        List<Blog> blogCountList = blogService.countList();

        application.setAttribute("blogCountList", blogCountList);

        LinkService linkService = (LinkService) applicationContext.getBean("linkService");

        List<Link> linkList = linkService.list(null);

        application.setAttribute("linkList", linkList);

    }


    public void contextDestroyed(ServletContextEvent sce) {
    }

}
