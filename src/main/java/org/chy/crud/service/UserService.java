package org.chy.crud.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.chy.crud.dto.LoginFormDTO;
import org.chy.crud.pojo.User;
import org.chy.crud.pojo.UserLogin;
import org.chy.crud.vo.UserLoginVO;

import java.util.List;

public interface UserService extends IService<User> {
    void insert(User user);

    User query(Integer id);

    void delete(Integer id);

    void update(User user);


    Page<User> queryPage(Integer page, Integer pageSize,String name);

    List<User> show();



}
