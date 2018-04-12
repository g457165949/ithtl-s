package com.ven.filter;

import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class MyPassThruAuthenticationFilter extends PassThruAuthenticationFilter {

    private String loginErrUrl ="/site/login-error";


    public void setLoginErrUrl(String loginErrUrl) {
        this.loginErrUrl = loginErrUrl;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            return true;
        } else {
            this.saveRequestAndRedirectToLogin(request, response);
            return false;
        }
    }
    //重写redirectToLogin方法是因为saveRequestAndRedirectToLogin方法会调用它，而原始的redirectToLogin方法会使得请求重定向到loginUrl
    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        WebUtils.issueRedirect(request, response, loginErrUrl);
    }
}
