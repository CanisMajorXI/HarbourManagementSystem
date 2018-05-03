package com.redsun.pojo;

public class User {

    //用户分4种，操作员(operator)，货主(shipper)，承运人(freighter)，管理员(admin)
    public static final int TYPE_OPERATOR = 1;
    public static final int TYPE_SHIPPER = 2;
    public static final int TYPE_FREIGHTER = 3;
    public static final int TYPE_ADMIN = 4;
    //8位数字id
    private Integer id;
    private String password;
    private String email;
    private Byte type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
