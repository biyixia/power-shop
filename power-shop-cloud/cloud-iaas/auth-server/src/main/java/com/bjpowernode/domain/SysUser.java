package com.bjpowernode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 系统用户
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUser implements Serializable, UserDetails {
    /**
     * 
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机号
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建者ID
     */
    @TableField(value = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 用户所在的商城Id
     */
    @TableField(value = "shop_id")
    private Long shopId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<String> authorities;

    public String getUsername() {
        return userId.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        ArrayList<String> PermsList = new ArrayList<>();
        authorities.forEach(
                authority -> {
                    if (authority.contains(",")) {
                        List<String> list = Arrays.asList(authority.split(","));
                        PermsList.addAll(list);
                    } else {
                        PermsList.add(authority);
                    }
                }
        );
        return PermsList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status.equals(1);
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status.equals(1);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status.equals(1);
    }

    @Override
    public boolean isEnabled() {
        return this.status.equals(1);
    }
}