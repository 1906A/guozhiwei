package com.leyou.service;

import com.leyou.dao.UserMapper;
import com.leyou.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public Boolean check(String data, Integer type) {
        Boolean result=false;
        User user=new User();
        //1.判断效验的是谁  type 1 用户名  2.手机
        if (type==1){
            //用户名
            user.setUsername(data);
        }else if (type==2){
            //手机
            user.setPhone(data);
        }
        //2.根据效验内容去数据库查询
        User user1 = userMapper.selectOne(user);
        if (user1==null){
            return true;
        }
        //3.用户名存在 false true  手机号存在 false true
        return result;
    }

    public void  insertUser(User user) {
        //盐值  数据生成字符串
        String salt = UUID.randomUUID().toString().substring(0,32);
        String password=this.getPws(user.getPassword(),salt);
        user.setPassword(password);
        user.setCreated(new Date());
        user.setSalt(salt);

        userMapper.insert(user);
    }

    /**
     * 通过原生密码+盐值生成md5加密后的密码
     * @param password
     * @param salt
     * @return
     */
    public String getPws(String password,String salt){
        //如何使用md5加密
        String md5Hex = DigestUtils.md5Hex(password + salt);
        return md5Hex;
    }

    public User findUser(String username) {
        User user=new User();
         user.setUsername(username);
        User user1 = userMapper.selectOne(user);
        return user1;
    }


}
