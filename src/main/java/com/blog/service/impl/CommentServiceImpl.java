
package com.blog.service.impl;


import com.blog.dao.CommentDao;
import com.blog.entity.Comment;
import com.blog.service.CommentService;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;


@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentDao commentDao;

    public int add(Comment comment) {
        return this.commentDao.add(comment);
    }

    public List<Comment> list(Map<String, Object> map) {
        return this.commentDao.list(map);
    }

    public Long getTotal(Map<String, Object> map) {
        return this.commentDao.getTotal(map);
    }

    public Integer delete(Integer id) {
        return this.commentDao.delete(id);
    }

    public int update(Comment comment) {
        return this.commentDao.update(comment);
    }

}

