package com.blog.dao;

import com.blog.entity.BlogType;
import java.util.List;
import java.util.Map;

public abstract interface BlogTypeDao
{
  public abstract List<BlogType> countList();
  
  public abstract BlogType findById(Integer paramInteger);
  
  public abstract List<BlogType> list(Map<String, Object> paramMap);
  
  public abstract Long getTotal(Map<String, Object> paramMap);
  
  public abstract Integer add(BlogType paramBlogType);
  
  public abstract Integer update(BlogType paramBlogType);
  
  public abstract Integer delete(Integer paramInteger);
}



