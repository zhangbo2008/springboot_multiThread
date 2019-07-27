package com.example.visit_record.service.impl;

import com.example.visit_record.entity.User;
import com.example.visit_record.mapper.UserMapper;
import com.example.visit_record.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhagnbo284
 * @since 2019-07-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
