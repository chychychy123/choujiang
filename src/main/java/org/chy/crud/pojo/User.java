package org.chy.crud.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user")
public class User {


    private Integer id;
    private String username;
    private Integer age;

    private String sex;
    private String address;
    private String phone;


    public User() {
    }

    public User(Integer id, String username, Integer age, String sex, String address, String phone) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
    }

    /**
     * 获取
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 获取
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toString() {
        return "User{id = " + id + ", username = " + username + ", age = " + age + ", sex = " + sex + ", address = " + address + ", phone = " + phone + "}";
    }
}

