package com.leyou.service;

import com.leyou.client.*;
import com.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsService {
    @Autowired
    private SpuClient spuClient;
    @Autowired
    private SkuClient skuClient;
    @Autowired
    private SpecGroupClient specGroupClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecParamClient specParamClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 查询商品详情数据
     * @param spuId
     * @return
     */
    public  Map<String,Object> item(Long spuId){
        //1.查询spu
        Spu spu = spuClient.findSpuById(spuId);

        //2.spudetail
        SpuDetail spuDetail = spuClient.findSpuDetailBySpuId(spuId);
        System.out.println(spuDetail);

        //3.查询sku
        List<Sku> skuList = skuClient.findSkuBySpuId(spuId);

        //4.规格参数组及组内信息
        List<SpecGroup> specGroupList = specGroupClient.findSpecGroupList(spu.getCid3());

        //5.三级分类
        List<Category> categoryList = categoryClient.findCatrgotByCids(
                Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));

        //6.参数中的规格参数
        List<SpecParam> specParamList=specParamClient.findParamByCidAndGeneric(spuId,false);
        Map<Long,String> paramMap=new HashMap<>();
        specParamList.forEach(param->{
            paramMap.put(param.getId(),param.getName());
        });

        //7.查询品牌
        Brand brand = brandClient.findBrandById(spu.getBrandId());

        Map<String,Object> map=new HashMap<>();
        map.put("spu",spu);
        map.put("spuDetail",spuDetail);
        map.put("skuList",skuList);
        map.put("specGroupList",specGroupList);
        map.put("categoryList",categoryList);
        map.put("paramMap",paramMap);
        map.put("brand",brand);

        return map;
    }

    /**
     * 创建静态化页面
     * @param spuId
     */
    public void creatHtml(Long spuId) {
        PrintWriter writer=null;
        try {
            //1.创建上下文
            Context context=new Context();
            //2.把数据放入上下文对象
            /*context.setVariable("spu",spu);
            context.setVariable("spuDetail",spuDetail);
            context.setVariable("skuList",skuList);
            context.setVariable("specGroupList",specGroupList);
            context.setVariable("categoryList",categoryList);
            context.setVariable("paramMap",paramMap);
            context.setVariable("brand",brand);*/
            context.setVariables(this.item(spuId));
            //3.写入文件
            File file=new File("E:\\vue\\nginx-1.16.1\\nginx-1.16.1\\html\\"+spuId+".html");
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

    }

    /**
     * 删除静态页面
     * @param spuId
     */
    public void deleteHtml(Long spuId) {
        File file=new File("E:\\vue\\nginx-1.16.1\\nginx-1.16.1\\html\\"+spuId+".html");
        if (file!=null && file.exists()){
            file.delete();
        }
    }
}
