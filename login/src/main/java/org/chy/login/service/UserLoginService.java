package org.chy.login.service;



import org.chy.login.dto.LoginFormDTO;
import org.chy.login.pojo.UserLogin;
import org.chy.login.vo.UserLoginVO;
import org.springframework.stereotype.Service;

@Service
public interface UserLoginService {
    UserLoginVO login(LoginFormDTO loginDTO);

    UserLoginVO register(UserLogin userLogin);


    UserLogin query(Long id);
}
