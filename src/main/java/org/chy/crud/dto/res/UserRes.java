package org.chy.crud.dto.res;


import lombok.Data;
import org.chy.crud.pojo.User;

import java.util.List;

public class UserRes {
    List<User> users;
    Integer total;
    Integer page;


    public UserRes() {
    }

    public UserRes(List<User> users, Integer total, Integer page) {
        this.users = users;
        this.total = total;
        this.page = page;
    }

    /**
     * 获取
     * @return users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * 设置
     * @param users
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * 获取
     * @return total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * 设置
     * @param total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * 获取
     * @return page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * 设置
     * @param page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    public String toString() {
        return "UserRes{users = " + users + ", total = " + total + ", page = " + page + "}";
    }
}
