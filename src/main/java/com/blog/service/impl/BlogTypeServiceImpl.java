
package com.blog.service.impl;


import com.blog.dao.BlogTypeDao;
/*  4:   */ import com.blog.entity.BlogType;
/*  5:   */ import com.blog.service.BlogTypeService;
/*  6:   */ import java.util.List;
/*  7:   */ import java.util.Map;
/*  8:   */ import javax.annotation.Resource;
/*  9:   */ import org.springframework.stereotype.Service;

@Service("blogTypeService")
 public class BlogTypeServiceImpl implements BlogTypeService {

    @Resource
    private BlogTypeDao blogTypeDao;

    public List<BlogType> countList() {
        return this.blogTypeDao.countList();
    }

    public List<BlogType> list(Map<String, Object> map) {
        return this.blogTypeDao.list(map);
    }

    public Long getTotal(Map<String, Object> map) {
        return this.blogTypeDao.getTotal(map);
    }

    public Integer add(BlogType blogType) {
        return this.blogTypeDao.add(blogType);
    }


    public Integer update(BlogType blogType) {
        return this.blogTypeDao.update(blogType);
    }

    public Integer delete(Integer id) {
        return this.blogTypeDao.delete(id);
    }

}

