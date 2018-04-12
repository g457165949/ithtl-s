package com.ven.interceptor;

import com.ven.model.account.User;
import com.ven.model.system.SysLog;
import com.ven.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class LogInterceptor implements HandlerInterceptor {

	@Autowired
	private SysLogService logService;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		System.out.println("uri" + request.getRequestURI());
		if(request.getRequestURI().equals("/captcha") ||
				request.getRequestURI().equals("/") ||
				request.getRequestURI().equals("/site/islogin") ||
				request.getRequestURI().equals("/site/logout") ||
				request.getRequestURI().equals("/error")){
			return;
		}

		/*
		 * 日志信息
		 */
		String ip = request.getRemoteAddr();
		StringBuilder parameters = new StringBuilder();
		// 获取提交参数
		if (request.getParameterMap() != null) {
			Map<String, String[]> map = request.getParameterMap();
			for (Entry<String, String[]> entry : map.entrySet()) {
				if (entry.getKey().contains("password") || entry.getKey().contains("pwd")) {
					parameters.append(entry.getKey() + ": ******");
					continue;
				}
				parameters.append(entry.getKey() + ": " + Arrays.toString(entry.getValue()) + "; ");
			}
			if (parameters.toString().endsWith("; ")) {
				parameters.substring(0, parameters.length() - 2);
			}
		}

		Object user = request.getSession().getAttribute("user");
		Integer userId = 0;
		String account = "未知";
		if (user != null) {
			if(user instanceof User){
				userId = ((User) user).getId();
				String name =((User) user).getUsername();
				String nickname = ((User) user).getNickname();
				account = name + "[" + nickname + "]";
			}
		}

		String userAge = request.getHeader("User-Agent");
		SysLog log = new SysLog(request.getMethod(),request.getRequestURI(),getUrl(request), userAge ,parameters.toString(), userId, account, ip);
		logService.save(log);
	}

	public String getUrl(HttpServletRequest request){
		String url = "http://" + request.getServerName() //服务器地址
				+ ":"
				+ request.getServerPort()           //端口号
				+ request.getContextPath()      //项目名称
				+ request.getServletPath();      //请求页面或其他地址
		return url;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		return true;
	}

}
