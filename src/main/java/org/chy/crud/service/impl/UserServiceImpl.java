package org.chy.crud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.chy.crud.mapper.UserMapper;
import org.chy.crud.pojo.User;
import org.chy.crud.service.UserService;
import org.springframework.stereotype.Service;


import java.util.List;
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public void insert(User user) {
        baseMapper.insert(user);
        System.out.println("添加成功");
    }

    @Override
    public User query(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void delete(Integer id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void update(User user) {
        baseMapper.updateById(user);
        System.out.println("修改成功");
    }




    @Override
    public Page<User> queryPage(Integer page, Integer pageSize, String name) {
        IPage<User> page1 = new Page<>(page,pageSize);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username",name);
        IPage<User> userIPage = baseMapper.selectPage(page1, queryWrapper);
        return (Page<User>) userIPage;

    }

    @Override
    public List<User> show() {
        return baseMapper.selectList(null);
    }




}



