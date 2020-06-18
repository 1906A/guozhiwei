package com.leyou.controller;

import com.leyou.common.utils.CodeUtils;
import com.leyou.pojo.User;
import com.leyou.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 实现用户数据的效验  主要包括对手机号、用户名的唯一性效验
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public Boolean check(@PathVariable("data") String data,
                         @PathVariable("type") Integer type){
        System.out.println("效验："+data+"==="+type);
        return userService.check(data,type);
    }


    /**
     * 根据用户输入的手机号，生成随机验证码
     * @param phone
     */
    @PostMapping("code")
    public  void code(@RequestParam("phone") String phone){
        System.out.println("code ："+phone);
        //1.生成一个6位数的随机码
        String code = CodeUtils.messageCode(6);
        //2.使用rabbitmq发送验证码
        Map<String,String> map=new HashMap<>();
        map.put("phone",phone);
        map.put("code",code);
       amqpTemplate.convertAndSend("sms.changes","sms.send",map);
        //3.发送短信后存放redis 放验证码 code
        stringRedisTemplate.opsForValue().set("lysms_"+phone,code,5, TimeUnit.MINUTES);//放入redis中code的有效期5分钟

    }

    /**
     * 用户注册
     * @param user
     * @param code
     */
    @PostMapping("register")
    public  void register(@Valid User user, String code) {
        System.out.println("用户注册 ："+user.getUsername()+"code="+code);

        if (user!=null){
            //从redis中获取code
            String redisCode = stringRedisTemplate.opsForValue().get("lysms_" + user.getPhone());
            //1.判断code验证码是否一致
            if (redisCode.equals(code)){
                userService.insertUser(user);
            }
        }
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public User query(@RequestParam("username")String username,
                      @RequestParam("password") String password){
        System.out.println("用户注册：username= ："+username+"password="+password);
        User user=userService.findUser(username);
        if (user!=null){
            String salt = user.getSalt();
            String newPawword = DigestUtils.md5Hex(password + salt);
            if (newPawword.equals(user.getPassword())){
                System.out.println(user.getUsername()+"---"+user.getPassword());
                return user;
            }
        }
        return null;
    }
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public String login(@RequestParam("username")String username,
                         @RequestParam("password")String password){
        String result="1";
        //1.根据用户名查询用户信息
        User user=userService.findUser(username);
        if (user!=null){
            //2.比对密码
            String newPassword = DigestUtils.md5Hex(password + user.getSalt());
            System.out.println("newPassword"+newPassword);
            System.out.println(user.getPassword());
            if (newPassword.equals(user.getPassword())){
                return  "0";
            }
        }
        return result;
    }

}
