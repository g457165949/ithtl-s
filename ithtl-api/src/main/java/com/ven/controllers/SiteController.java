package com.ven.controllers;

import com.ven.model.account.User;
import com.ven.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.channels.FileChannel;
import java.util.Map;

@RestController
@RequestMapping("/site/*")
public class SiteController extends BaseController{

	@Resource
	private UserService userService;

	@GetMapping("islogin")
	public Map<Object,Object> islogin(ModelMap map) {

		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession(true);

		System.out.println("isAuthenticated:"+subject.isAuthenticated());
		System.out.println("isRemembered:"+subject.isRemembered());
		System.out.println("session:"+session.getAttribute("user"));
		//如果 isAuthenticated 为 false 证明不是登录过的，同时 isRememberd 为true 证明是没登陆直接通过记住我功能进来的
		if(!subject.isAuthenticated() && subject.isRemembered() && session.getAttribute("user") == null){
			System.out.println(subject.getPrincipal().toString());
			return success((User)subject.getPrincipal());
		}
		return success((User)subject.getPrincipal());
	}

	@PostMapping("login")
	@ResponseBody
	public Map<Object,Object> login(HttpServletRequest request) throws Exception {
		System.out.println("----->>login:error");
		// 登录失败从request中获取shiro处理的异常信息。
		// shiroLoginFailure:就是shiro异常类的全类名.
		String exception = (String) request.getAttribute("shiroLoginFailure");
		System.out.println("exception=" + exception);
		String msg = "";
		int code = -1;
		if (exception != null) {
			if (UnknownAccountException.class.getName().equals(exception)) {
				System.out.println("UnknownAccountException -- > 账号不存在：");
				msg = "账号不存在!";
			} else if (IncorrectCredentialsException.class.getName().equals(exception)) {
				System.out.println("IncorrectCredentialsException -- > 密码不正确：");
				msg = "密码不正确!";
			} else if (LockedAccountException.class.getName().equals(exception)){
				msg = "用户被锁定！";
			}else if("captcha.error".equals(exception)) {
				msg = "验证码错误！";
			}else{
				msg = "网络繁忙，请从新操作！";
				code = 50000;
				System.out.println("else -- >" + exception);
			}
		}
		return msg(code,msg);
	}

	@GetMapping("logout")
    public Map<Object, Object> logout(ModelMap map){
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息  
        SecurityUtils.getSubject().logout();
		map.addAttribute("message", "您已安全退出");
		return success(map);
    }
}
