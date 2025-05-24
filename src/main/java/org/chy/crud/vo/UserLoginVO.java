package org.chy.crud.vo;



public class UserLoginVO {
    private String token;
    private long userId;
    private String username;
    private Integer balance;

    public UserLoginVO() {
    }

    public UserLoginVO(String token, Integer userId, String username, Integer balance) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.balance = balance;
    }

    /**
     * 获取
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取
     *
     * @return userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * @return balance
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * 设置
     * @param balance
     */
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String toString() {
        return "UserLoginVO{token = " + token + ", userId = " + userId + ", username = " + username + ", balance = " + balance + "}";
    }
}
