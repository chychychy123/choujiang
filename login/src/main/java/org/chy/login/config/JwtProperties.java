package org.chy.login.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.time.Duration;


@ConfigurationProperties(prefix = "hm.jwt")
public class JwtProperties {
    private Resource location;
    private String password;
    private String alias;
    private Duration tokenTTL = Duration.ofMinutes(10);

    public JwtProperties() {
    }

    public JwtProperties(Resource location, String password, String alias, Duration tokenTTL) {
        this.location = location;
        this.password = password;
        this.alias = alias;
        this.tokenTTL = tokenTTL;
    }

    /**
     * 获取
     * @return location
     */
    public Resource getLocation() {
        return location;
    }

    /**
     * 设置
     * @param location
     */
    public void setLocation(Resource location) {
        this.location = location;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 获取
     * @return tokenTTL
     */
    public Duration getTokenTTL() {
        return tokenTTL;
    }

    /**
     * 设置
     * @param tokenTTL
     */
    public void setTokenTTL(Duration tokenTTL) {
        this.tokenTTL = tokenTTL;
    }

    public String toString() {
        return "JwtProperties{location = " + location + ", password = " + password + ", alias = " + alias + ", tokenTTL = " + tokenTTL + "}";
    }
}
