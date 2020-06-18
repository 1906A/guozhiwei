package com.leyou.search.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.search.Item.Goods;
import com.leyou.search.client.SkuClient;
import com.leyou.search.client.SpecClient;
import com.leyou.search.client.SpuClient;
import com.leyou.search.repository.GoodsRepository;
import com.pojo.Sku;
import com.pojo.SpecParam;
import com.pojo.SpuDetail;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.SpuVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsService {

    public static final ObjectMapper MAPPER=new ObjectMapper();
    @Autowired
    private SkuClient skuClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private SpuClient spuClient;
    @Autowired
    private GoodsRepository goodsRepository;

    public Goods convent(SpuVo spuBo) throws Exception {

        Goods goods = new Goods();
        //基础数据
        goods.setId(spuBo.getId());
        goods.setSubTitle(spuBo.getSubTitle());
        goods.setBrandId(spuBo.getBrandId());
        goods.setCid1(spuBo.getCid1());
        goods.setCid2(spuBo.getCid2());
        goods.setCid3(spuBo.getCid3());
        goods.setCreateTime(spuBo.getCreateTime());

        //all存放  标题 分类+品牌
        goods.setAll(spuBo.getTitle()+" "+spuBo.getCname().replace("/"," ")+" "+spuBo.getBname());

        //复杂数据
        //根据spuid查询sku
        List<Sku> skuList = skuClient.findSkuBySpuId(spuBo.getId());

        List<Long> price =new ArrayList<>();
        skuList.forEach(sku->{
            price.add(sku.getPrice());
        });

        goods.setPrice(price);
        goods.setSkus(MAPPER.writeValueAsString(skuList));

        Map<String, Object> specs =new HashMap<>();

        //根据三级分类id和可搜索条件查询规格参数
        List<SpecParam> specParamList = specClient.findSpecParamByCidAndSearching(spuBo.getCid3());
        //根据spuid查询spudetail
        SpuDetail spuDetail = spuClient.findSpuDetailBySpuId(spuBo.getId());
        specParamList.forEach(sp ->{
            if(sp.getGeneric()){
                try {
                    Map<Long,Object> genericSpec = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>(){}) ;
                    String value = genericSpec.get(sp.getId()).toString();

                    if(sp.getNumeric()){
                        value = chooseSegment(value,sp);
                    }

                    specs.put(sp.getName(),value);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{

                Map<Long,Object> specialSpec = null;
                try {
                    specialSpec = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, Object>>(){});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String value = specialSpec.get(sp.getId()).toString();

                specs.put(sp.getName(),value);
            }

        });
        goods.setSpecs(specs);
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }


    /**
     * rabbitmq监听消息修改es数据
     * @param spuId
     */
    public void editEsData(Long spuId) throws Exception {
        //1.根据spuId查询spu
        SpuVo spuVo=spuClient.findSpuBySpuId(spuId);
        //2.spu转换成googs
        Goods goods = this.convent(spuVo);
        //3. 持久化es
        goodsRepository.save(goods);
    }

    /**
     * rabbitmq删除
     * @param spuId
     */
    public void deleteEsData(Long spuId) {
        goodsRepository.deleteById(spuId);

    }
}
