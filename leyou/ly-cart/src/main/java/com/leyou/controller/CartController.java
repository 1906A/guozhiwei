package com.leyou.controller;

import com.leyou.common.JwtUtils;
import com.leyou.common.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.config.JwtProperties;
import com.leyou.pojo.SkuVo;
import com.pojo.Sku;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@EnableConfigurationProperties(JwtProperties.class) //在控制层主动默认开启加入
@Api("购物车服务接口")
public class CartController {
    public String prifix="ly_catrs";
    public String prifixSelectedSku="ly_catrs_selectedSku";
    /*
    * 操作购物车数据存放redis  hash
    *
    * 1.加入购物车
    * 2.修改购物车
    * 3.删除购物车
    * 4.查询购物车
    *
    * */
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    JwtProperties jwtProperties;

    /**
     * 加入购物车
     * @param token
     * @param skuVo
     */
    @PostMapping("add")
    @ApiOperation(value = "购物车添加商品信息保存在redis中", notes = "购物车添加商品信息")
    @ApiImplicitParam(name = "skuVo", required = true, value = "结算页选择sku串")
    public void add(@CookieValue("token")String token, @RequestBody SkuVo skuVo){
        UserInfo userInfo = this.getUserInfoByToken(token);
        if (userInfo!=null){
            //添加购物车
            BoundHashOperations<String, Object, Object> hashOps =
                    stringRedisTemplate.boundHashOps(prifix + userInfo.getId());
            //判断redis中信息
            if (hashOps.hasKey(skuVo.getId()+"")){
                //从redis中获取已存在的购物车信息
                SkuVo redisSkuVo = JsonUtils.parse(hashOps.get(skuVo.getId()+"").toString(),SkuVo.class);
                //修改购物车信息中的数量
                redisSkuVo.setNum(redisSkuVo.getNum()+skuVo.getNum());
                //重新存放redis
                hashOps.put(skuVo.getId()+"",JsonUtils.serialize(redisSkuVo));
                //当前操作的sku单独存放redis
                stringRedisTemplate.boundValueOps(prifixSelectedSku+userInfo.getId()).set(JsonUtils.serialize(redisSkuVo));

            }else {
                //第一次往redis存放商品信息
                hashOps.put(skuVo.getId()+"",JsonUtils.serialize(skuVo));
                //当前操作的sku单独存放redis
                stringRedisTemplate.boundValueOps(prifixSelectedSku+userInfo.getId()).set(JsonUtils.serialize(skuVo));
            }
        }
    }

    /**
     * 去redis中获取最新操作的sku的信息
     * @param token
     * @return
     */
    @PostMapping("selectedSku")
    public SkuVo selectedSku(@CookieValue("token")String token){
        UserInfo userInfo = this.getUserInfoByToken(token);
        //从redis获取最新的sku信息
        String s = stringRedisTemplate.boundValueOps(prifixSelectedSku + userInfo.getId()).get();

        SkuVo skuVo = JsonUtils.parse(s, SkuVo.class);
        return skuVo;
    }


    /**
     * 修改购物车
     * @param token
     * @param skuVo
     */
    @PostMapping("update")
    public void update(@CookieValue("token")String token, @RequestBody SkuVo skuVo){
        UserInfo userInfo = this.getUserInfoByToken(token);
        if (userInfo!=null){
            //添加购物车
            BoundHashOperations<String, Object, Object> hashOps =
                    stringRedisTemplate.boundHashOps(prifix + userInfo.getId());
            //判断redis中信息
            if (hashOps.hasKey(skuVo.getId()+"")){
                //从redis中获取已存在的购物车信息
                SkuVo redisSkuVo = JsonUtils.parse(hashOps.get(skuVo.getId()+"").toString()+"",SkuVo.class);
                //修改购物车信息中的数量
                redisSkuVo.setNum(skuVo.getNum());
                //重新存放redis
                hashOps.put(skuVo.getId()+"",JsonUtils.serialize(redisSkuVo));
            }else {
                //第一次往redis存放商品信息
                hashOps.put(skuVo.getId()+"",JsonUtils.serialize(skuVo));
            }
        }
    }

    /**
     * 删除购物车
     * @param token
     * @param id
     */
    @PostMapping("delete")
    public void delete(@CookieValue("token")String token,@RequestParam("id") Long id){
        UserInfo userInfo = this.getUserInfoByToken(token);
        if (userInfo!=null){
            //添加购物车
            BoundHashOperations<String, Object, Object> hashOps =
                    stringRedisTemplate.boundHashOps(prifix + userInfo.getId());
            hashOps.delete(id+"");
        }
    }

    /**
     * 查询购物车
     * @param token
     * @return
     */
    @PostMapping("query")
    public List<SkuVo> query(@CookieValue("token")String token){
        UserInfo userInfo = this.getUserInfoByToken(token);
        List<SkuVo> list=new ArrayList<>();
        if (userInfo!=null){
            //添加购物车
            BoundHashOperations<String, Object, Object> hashOps =
                    stringRedisTemplate.boundHashOps(prifix + userInfo.getId());
            Map<Object, Object> map = hashOps.entries();
            map.keySet().forEach(key->{
                SkuVo skuVo = JsonUtils.parse(hashOps.get(key).toString(), SkuVo.class);
                list.add(skuVo);
            });
        }
        return list;
    }

    /**
     * 登录后根据token解析用户信息
     * @param token
     * @return
     */
    public UserInfo getUserInfoByToken(String token){
        UserInfo userInfo=new UserInfo();
        try {
            userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }


}
