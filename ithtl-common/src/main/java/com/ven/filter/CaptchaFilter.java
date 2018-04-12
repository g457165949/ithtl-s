package com.ven.filter;

import com.google.code.kaptcha.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 验证码过滤器
 */
public class CaptchaFilter extends AccessControlFilter {

	/**
	 * 是否开启验证码验证   默认true
	 */
	public boolean jcaptchaEbabled = true;

	/**
	 * 前台提交的验证码参数名
	 */
	public String param = "captcha";

	/**
	 * 验证失败后存储到的属性名
	 */
	public String failureKeyAttribute = "shiroLoginFailure";

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		System.out.println("进入验证码过滤器begin----->>");
		//1、设置验证码是否开启属性，页面可以根据该属性来决定是否显示验证码
		request.setAttribute("jcaptchaEbabled", jcaptchaEbabled);
		HttpServletRequest httpRequest = WebUtils.toHttp(request);

		//2、判断验证码是否禁用 或不是表单提交（允许访问）
		if (jcaptchaEbabled == false || !"post".equalsIgnoreCase(httpRequest.getMethod())) {
			return true;
		}

		//表单提交，校验验证码的正确性
		String storedCode = getSubject(request, response).getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY).toString();
		String currentCode = httpRequest.getParameter(param);
		System.out.println("进入验证码过滤器end----->>"+storedCode+"|"+currentCode+"|"+StringUtils.equalsIgnoreCase(storedCode, currentCode));
		return StringUtils.equalsIgnoreCase(storedCode, currentCode);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		System.out.println("进入验证码过滤器error----->>");
		request.setAttribute(failureKeyAttribute, "captcha.error");
		return true;
	}
}
