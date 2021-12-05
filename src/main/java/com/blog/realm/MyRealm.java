/*  1:   */
package com.blog.realm;

import com.blog.entity.Blogger;
/*  4:   */ import com.blog.service.BloggerService;
/*  5:   */ import javax.annotation.Resource;
/*  6:   */ import org.apache.shiro.SecurityUtils;
/*  7:   */ import org.apache.shiro.authc.AuthenticationException;
/*  8:   */ import org.apache.shiro.authc.AuthenticationInfo;
/*  9:   */ import org.apache.shiro.authc.AuthenticationToken;
/* 10:   */ import org.apache.shiro.authc.SimpleAuthenticationInfo;
/* 11:   */ import org.apache.shiro.authz.AuthorizationInfo;
/* 12:   */ import org.apache.shiro.realm.AuthorizingRealm;
/* 13:   */ import org.apache.shiro.session.Session;
/* 14:   */ import org.apache.shiro.subject.PrincipalCollection;
/* 15:   */ import org.apache.shiro.subject.Subject;

public class MyRealm
        extends AuthorizingRealm {
    @Resource
    private BloggerService bloggerService;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        Blogger blogger = this.bloggerService.getByUserName(userName);
        if (blogger != null) {
            SecurityUtils.getSubject().getSession().setAttribute("currentUser", blogger);
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(blogger.getUserName(), blogger.getPassword(), "xx");
            return authcInfo;
        }
        return null;
    }
}



