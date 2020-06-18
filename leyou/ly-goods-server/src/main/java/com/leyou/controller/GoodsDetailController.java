package com.leyou.controller;

import com.leyou.client.*;
import com.leyou.service.GoodsService;
import com.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GoodsDetailController {
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("hello")
    public String hello(Model model){
        String name ="张三";

        model.addAttribute("name",name);
        return "hello";
    }

    /**
     *请求商品详情的微服务
     *1.spu
     * 2.spudetail
     * 3.sku
     * 4.规格参数组
     * 5.规格参数详情
     * 6.三级分类
     * @param spuId
     * @param model
     * @return
     */
    @RequestMapping("item/{spuId}.html")
    public String item(@PathVariable("spuId") Long spuId,Model model){
       /* //1.查询spu
        Spu spu = spuClient.findSpuById(spuId);
        model.addAttribute("spu",spu);

        //2.spudetail
        SpuDetail spuDetail = spuClient.findSpuDetailBySpuId(spuId);
        model.addAttribute("spuDetail",spuDetail);
        System.out.println(spuDetail);

        //3.查询sku
        List<Sku> skuList = skuClient.findSkuBySpuId(spuId);
        model.addAttribute("skuList",skuList);

        //4.规格参数组及组内信息
        List<SpecGroup> specGroupList = specGroupClient.findSpecGroupList(spu.getCid3());
        model.addAttribute("specGroupList",specGroupList);

        //5.三级分类
        List<Category> categoryList = categoryClient.findCatrgotByCids(
                Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
        model.addAttribute("categoryList",categoryList);

        //6.参数中的规格参数
        List<SpecParam> specParamList=specParamClient.findParamByCidAndGeneric(spuId,false);
        Map<Long,String> paramMap=new HashMap<>();
        specParamList.forEach(param->{
            paramMap.put(param.getId(),param.getName());
        });
        model.addAttribute("paramMap",paramMap);

        //7.查询品牌
        Brand brand = brandClient.findBrandById(spu.getBrandId());
        model.addAttribute("brand",brand);*/

        //查询数据
        Map<String, Object> map = goodsService.item(spuId);
        //model.addAttribute(map);
        model.addAllAttributes(map);
        goodsService.creatHtml(spuId);
        //1.导入templateEngine
        // 2.写入静态文件

        return "item";
    }

    /**
     *
     * 通过thymeleaf实现页面的静态化
     * @param spu
     * @param spuDetail
     * @param skuList
     * @param specGroupList
     * @param categoryList
     * @param paramMap
     * @param brand
     */
    /*private void creatHtml(Spu spu, SpuDetail spuDetail, List<Sku> skuList, List<SpecGroup> specGroupList, List<Category> categoryList, Map<Long, String> paramMap, Brand brand) {
        PrintWriter writer=null;
        try {
            //1.创建上下文
            Context context=new Context();
            //2.把数据放入上下文对象
            context.setVariable("spu",spu);
            context.setVariable("spuDetail",spuDetail);
            context.setVariable("skuList",skuList);
            context.setVariable("specGroupList",specGroupList);
            context.setVariable("categoryList",categoryList);
            context.setVariable("paramMap",paramMap);
            context.setVariable("brand",brand);
            //3.写入文件
            File file=new File("E:\\vue\\nginx-1.16.1\\nginx-1.16.1\\html"+spu.getId()+".html");
             writer=new PrintWriter(file);
             //4.执行页面静态化方法
            templateEngine.process("item",context,writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (writer!=null){
                //5.关闭写入流
                writer.close();
            }
        }

    }*/
}
