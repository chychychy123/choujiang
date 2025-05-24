package org.chy.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.chy.crud.config.JwtProperties;
import org.chy.crud.dto.LoginFormDTO;
import org.chy.crud.enums.UserStatus;
import org.chy.crud.exception.BadRequestException;
import org.chy.crud.mapper.UserLoginMapper;
import org.chy.crud.pojo.User;
import org.chy.crud.pojo.UserLogin;
import org.chy.crud.service.UserLoginService;
import org.chy.crud.utils.JwtTool;
import org.chy.crud.vo.UserLoginVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements UserLoginService {
    private final PasswordEncoder passwordEncoder;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;
    @Override
    public UserLoginVO login(LoginFormDTO loginDTO) {
        // 1.数据校验
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        // 2.根据用户名或手机号查询
        UserLogin user = lambdaQuery().eq(UserLogin::getUsername, username).one();
        Assert.notNull(user, "用户名错误");
        // 3.校验是否禁用
        if (user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户被冻结");
        }
        // 4.校验密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("用户名或密码错误");
        }
        // 5.生成TOKEN
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());
        // 6.封装VO返回
        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setBalance(user.getBalance());
        vo.setToken(token);
        return vo;
    }

    @Override
    public UserLoginVO register(UserLogin userLogin) {
        // 校验用户名是否存在
        UserLogin existingUser = lambdaQuery()
                .eq(UserLogin::getUsername, userLogin.getUsername())
                .one();
        Assert.isNull(existingUser, "用户已存在");

        // 非空校验密码
        String rawPassword = userLogin.getPassword();
        Assert.notNull(rawPassword, "密码不能为空");

        // 加密并保存
        userLogin.setPassword(passwordEncoder.encode(rawPassword));
        save(userLogin);

        return new UserLoginVO(
                jwtTool.createToken(userLogin.getId(), jwtProperties.getTokenTTL()),
                Math.toIntExact(userLogin.getId()),
                userLogin.getUsername(),
                userLogin.getBalance()
        );
    }

    @Override
    public UserLogin query(Long id) {
        //根据登陆信息返回用户姓名
        return lambdaQuery().eq(UserLogin::getId, id).one();
    }

}
