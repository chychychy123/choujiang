package org.chy.crud.controller;

import org.chy.crud.dto.LoginFormDTO;
import org.chy.crud.pojo.UserLogin;
import org.chy.crud.service.UserLoginService;
import org.chy.crud.service.UserService;
import org.chy.crud.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class LoginController {
    @Autowired
    private UserLoginService UserLoginService;
    //用户登陆页面
    @PostMapping("/login")
    public UserLoginVO login(@RequestBody @Validated LoginFormDTO loginFormDTO){
        return UserLoginService.login(loginFormDTO);
    }
    //用户注册页面
    @PostMapping("/register")
    public UserLoginVO register(@RequestBody @Validated UserLogin userLogin){
        return UserLoginService.register(userLogin);}

    //查询登陆的用户
    @PostMapping("/queryLogin")
    public UserLogin queryLogin(Long id){

        return UserLoginService.query(id);
            }

}
