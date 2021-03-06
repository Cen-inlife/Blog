package com.blog.dao;

import com.blog.entity.Blogger;

public abstract interface BloggerDao
{
  public abstract Blogger find();
  
  public abstract Blogger getByUserName(String paramString);
  
  public abstract Integer update(Blogger paramBlogger);
}


