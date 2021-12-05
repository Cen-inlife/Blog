
package com.blog.service.impl;


import com.blog.dao.LinkDao;
/*  4:   */ import com.blog.entity.Link;
/*  5:   */ import com.blog.service.LinkService;
/*  6:   */ import java.util.List;
/*  7:   */ import java.util.Map;
/*  8:   */ import javax.annotation.Resource;
/*  9:   */ import org.springframework.stereotype.Service;


@Service("linkService")
public class LinkServiceImpl
        implements LinkService {

    @Resource
    private LinkDao linkDao;


    public int add(Link link) {

        return this.linkDao.add(link);

    }


    public int update(Link link) {

        return this.linkDao.update(link);

    }


    public List<Link> list(Map<String, Object> map) {

        return this.linkDao.list(map);

    }


    public Long getTotal(Map<String, Object> map) {

        return this.linkDao.getTotal(map);

    }

    public Integer delete(Integer id) {

        return this.linkDao.delete(id);

    }

}


