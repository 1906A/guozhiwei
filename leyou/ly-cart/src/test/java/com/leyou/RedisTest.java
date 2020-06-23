package com.leyou;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    public void test(){
        //存放hash值
        BoundHashOperations<String, Object, Object> hashOps =
                stringRedisTemplate.boundHashOps("Ly_carts");
        hashOps.put("sku_id123","{\"title\":\"崔子手机\"}");
        hashOps.put("sku_id456","{\"title\":\"华为手机\"}");
        hashOps.put("sku_id789","{\"title\":\"小米手机\"}");
        Map<Object, Object> map = hashOps.entries();
        map.keySet().forEach(key->{
            System.out.println(key+"---"+map.get(key));
        });
        System.out.println(hashOps);
    }
}
