package com.eve;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/13
 */

public class User {
    @JsonIgnore
    private String name;
    private String[] clzss;
    private int age;

    public String[] getClzss() {
        return clzss;
    }

    public void setClzss(String[] clzss) {
        this.clzss = clzss;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
