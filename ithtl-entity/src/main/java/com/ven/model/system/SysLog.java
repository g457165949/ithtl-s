package com.ven.model.system;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sys_log")
public class SysLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String route;
    private String url;
    private String userAge;
    private String method;
    private String parameters;
    private Integer adminId;
    private String adminName;
    private String ip;
    private Integer createdAt;

    public SysLog() {
    }

    public SysLog(String method, String route, String url, String userAge, String parameters, Integer adminId, String adminName, String ip) {
        this.method = method;
        this.route = route;
        this.url = url;
        this.userAge = userAge;
        this.adminId = adminId;
        this.adminName = adminName;
        this.parameters = parameters;
        this.ip = ip;
        this.createdAt = (int) (System.currentTimeMillis() / 1000);
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Basic
    @Column(name = "route")
    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "user_age")
    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    @Basic
    @Column(name = "admin_id")
    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Basic
    @Column(name = "admin_name")
    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Basic
    @Column(name = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Basic
    @Column(name = "created_at")
    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

}
