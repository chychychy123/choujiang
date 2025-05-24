package org.chy.crud.service;

import org.chy.crud.dto.LoginFormDTO;
import org.chy.crud.pojo.UserLogin;
import org.chy.crud.vo.UserLoginVO;

public interface UserLoginService {
    UserLoginVO login(LoginFormDTO loginDTO);

    UserLoginVO register(UserLogin userLogin);


    UserLogin query(Long id);
}
