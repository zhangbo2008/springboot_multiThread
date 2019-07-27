package com.example.visit_record.controller;


import com.example.visit_record.entity.User;
import com.example.visit_record.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhagnbo284
 * @since 2019-07-27
 */
@RestController

public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private User user;
//写2个接口可以添加user.
    @RequestMapping(value = "/1", method = {RequestMethod.GET})
    public String sfd1(){
        user.setName("324");
        userService.save(user);
//        System.out.println("进入/1");
         return "user1";
    }

    @RequestMapping(value = "/2", method = {RequestMethod.GET})
    public String sfd2(){
        user.setName("325");
        userService.save(user);

        return "user2";
    }
}
