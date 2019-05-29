package com.beneil.redis.pojo;


import javax.persistence.Column;
import java.io.Serializable;

public class User implements Serializable {

    private Integer id;
    private String uname;
    private String uaddress;

    public User() {
    }

    public User(Integer id, String uname) {
        this.id = id;
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", uaddress='" + uaddress + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUaddress() {
        return uaddress;
    }

    public void setUaddress(String uaddress) {
        this.uaddress = uaddress;
    }
}
