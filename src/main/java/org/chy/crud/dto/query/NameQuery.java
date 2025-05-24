package org.chy.crud.dto.query;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;


public class NameQuery {
    Integer page;
    Integer pageSize;
    String username;


    public NameQuery() {
    }

    public NameQuery(Integer page, Integer pageSize, String username) {
        this.page = page;
        this.pageSize = pageSize;
        this.username = username;
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

    /**
     * 获取
     * @return pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设置
     * @param pageSize
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    public String toString() {
        return "NameQuery{page = " + page + ", pageSize = " + pageSize + ", username = " + username + "}";
    }
}
