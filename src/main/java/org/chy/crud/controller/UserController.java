package org.chy.crud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.chy.crud.common.Result;
import org.chy.crud.pojo.User;
import org.chy.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    //添加用户信息
    @Autowired
    private UserService userService;
    @PostMapping("/insert")
    public String insert(@RequestBody User user){
        userService.insert(user);
        return "添加成功";
    }
    //查询用户信息
    @GetMapping("/query")
    public User query(Integer id){
        User user = userService.query(id);
        return user;
    }
    //删除用户信息
    @PostMapping("/delete")
    public String delete(@RequestBody Map<String, Integer> request) {
        userService.delete(request.get("id"));
        return "删除成功";
    }
    //修改用户信息
    @PostMapping("/update")
    public String update(@RequestBody User user){
        userService.update(user);
        return "修改成功";
    }
    //查询多个用户信息
    @GetMapping("/queryIds")
    public String queryIds(Integer[] ids){
        //使用for循环
        for (Integer id : ids) {
            User user = userService.query(id);
            System.out.println(user);
        }
        return "查询成功";
    }
    //批量删除用户信息
    @PostMapping("/deleteIds")
    public String deleteIds(@RequestBody Integer[] ids){
        for (Integer id : ids) {
            userService.delete(id);
        }
        return "删除成功";
    }
    //批量添加用户信息
    @PostMapping("/insertIds")
    public String insertIds(@RequestBody User[] users){
        for (User user : users) {
            userService.insert(user);
        }
        return "添加成功";
    }
    //分页模糊查询每页八条
    @GetMapping("/queryPage")
    public Page<User> queryPage(Integer page, Integer pageSize, String name) {
        return  userService.queryPage(page, pageSize, name);
    }
    //展示所有用户信息
    @GetMapping("/show")
    public List<User> show(){
        return userService.show();
    }

}


