package com.ven.controllers;

import com.ven.config.MyShiroRealm;
import com.ven.model.account.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


public class BaseController {

    /**
     * Web Object
     */
    @Resource
    protected HttpServletRequest request;

    private static final ThreadLocal<HttpServletRequest> requestContainer = new ThreadLocal<HttpServletRequest>();

    private static final ThreadLocal<HttpServletResponse> responseContainer = new ThreadLocal<HttpServletResponse>();

    private static final ThreadLocal<ModelMap> modelContainer = new ThreadLocal<ModelMap>();

    /**
     * 初始化response
     *
     * @param response
     */
    @ModelAttribute
    private final void initResponse(HttpServletResponse response) {
        responseContainer.set(response);
    }

    /**
     * 获取当前线程的response对象
     *
     * @return
     */
    protected final HttpServletResponse getResponse() {
        return responseContainer.get();
    }

    /**
     * 初始化request
     *
     * @param request
     */
    @ModelAttribute
    private final void initRequest(HttpServletRequest request) {
        requestContainer.set(request);
    }

    /**
     * 获取当前线程的request对象
     *
     * @return
     */
    protected final HttpServletRequest getRequest() {
        return requestContainer.get();
    }

    /**
     * 设置model
     *
     * @param model
     */
    @ModelAttribute
    private final void initModelMap(ModelMap model) {
        modelContainer.set(model);
    }

    /**
     * 获取当前线程的modelMap对象
     *
     * @return
     */
    protected final ModelMap getModelMap() {
        return modelContainer.get();
    }

    /**
     * 获得分页对象，自动封装客户端提交的分页参数
     *
     * @return
     */
    public Pageable pageable() {
        int page = ServletRequestUtils.getIntParameter(request, "page", 1);
        int rows = ServletRequestUtils.getIntParameter(request, "limit", 10);
        String sort = ServletRequestUtils.getStringParameter(request, "sort", "");
        String order = ServletRequestUtils.getStringParameter(request, "order", "");

        Pageable pageable = new PageRequest(page - 1, rows);

        return pageable;
    }

    /**
     * 返回成功
     *
     * @return
     */
    protected Map<Object, Object> success() {
        Map<Object, Object> jsonMap = new HashMap<Object, Object>();
        jsonMap.put("msg", "");
        jsonMap.put("code", 0);
        return jsonMap;
    }

    /**
     * 返回单个对象
     *
     * @param data
     * @return
     */
    protected Map<Object, Object> success(Object data) {
        Map<Object, Object> jsonMap = new HashMap<Object, Object>();
        jsonMap.put("data", data);
        jsonMap.put("msg", "");
        jsonMap.put("code", 0);
        return jsonMap;
    }

    /**
     * 返回分页list
     *
     * @param page
     * @return
     */
    protected Map<Object, Object> success(Page page) {
        Map<Object, Object> jsonMap = new HashMap<Object, Object>();
        // 分页参数
        jsonMap.put("data", page.getContent());
        jsonMap.put("page", page.getNumber());
        jsonMap.put("count", page.getTotalElements());
        jsonMap.put("msg", "");
        jsonMap.put("code", 0);
        return jsonMap;
    }


    /**
     * 返回错误信息
     *
     * @return
     */
    protected Map<Object, Object> msg(int code, String msg) {
        Map<Object, Object> jsonMap = new HashMap<Object, Object>();
        jsonMap.put("msg", msg);
        jsonMap.put("code", code);
        return jsonMap;
    }

    /**
     * 返回错误信息
     *
     * @return
     */
    protected Map<Object, Object> msg() {
        Map<Object, Object> jsonMap = new HashMap<Object, Object>();
        jsonMap.put("msg", "操作有误，请从新操作！");
        jsonMap.put("code", -1);
        return jsonMap;
    }

    /**
     * 从Session获得当前登录的用户对象
     * @return 当前登录的用户对象
     */
    public User getUser() {
        HttpSession session = request.getSession();
        User user = new User();
        if (session.getAttribute("user") instanceof User) {
            user = (User) session.getAttribute("user");
        }

        return user;
    }


    /**
     *
     * @Title: clearAuth
     * @Description: TODO 清空所有资源权限
     * @return void    返回类型
     */
    public static void clearAuth(){
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        MyShiroRealm realm = (MyShiroRealm)rsm.getRealms().iterator().next();
        realm.clearCached();
    }
}
