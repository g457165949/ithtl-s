package com.ven.model.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ven.model.system.SysRole;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the user database table.
 */
@Entity
@Table(name = "sys_user")
@JsonIgnoreProperties({ "password" })
@DynamicUpdate(value = true)
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6966521941614175452L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String email;

    private String username;

    private String nickname;

    private String password;

    private int status;

    @Column(name = "created_at")
    private int createdAt;

    @Column(name = "updated_at")
    private int updatedAt;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)    //立即从数据库中进行加载数据;
    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "rid")})
    @JsonBackReference
    private List<SysRole> sysRoleList;    // 一个用户具有多个角色

    public User() {

    }

    public User(String username, String nickname, String email, String password, int status, String description) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.description = description;
        this.createdAt = (int) (System.currentTimeMillis() / 1000);
        this.updatedAt = this.createdAt;
    }

    public List<SysRole> getSysRoleList() {return sysRoleList;}

    public void setSysRoleList(List<SysRole> sysRoleList) {
        this.sysRoleList = sysRoleList;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(int updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", username " + username +
                ", nickname " + nickname +
                ", email " + email +
                ", status " + status +
                ", updatedAt" + updatedAt +
                ", createdAt" + createdAt +
                ", description " + description +
                "}";
    }
}