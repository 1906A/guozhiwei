package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.PageResult;
import com.leyou.controller.SpuController;
import com.leyou.dao.SkuMapper;
import com.leyou.dao.SpuDetailMapper;
import com.leyou.dao.SpuMapper;
import com.leyou.dao.StockMapper;
import com.netflix.discovery.converters.Auto;
import com.pojo.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.SpuVo;

import java.util.Date;
import java.util.List;

@Service
public class SpuService {

    @Autowired
    SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<SpuVo> findSpuByPage(String key, Integer page, Integer rows, Integer saleable) {
        PageHelper.startPage(page,rows);
        List<SpuVo> spuList=spuMapper.findSpuByPage(key,saleable);

        PageInfo<SpuVo> pageInfo=new PageInfo<SpuVo>(spuList);

        return new PageResult<SpuVo>(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 保存商品信息
     * @param spuVo
     */
    public void saveSpuDetail(SpuVo spuVo) {

        Date nowDate=new Date();
        /**
         * 1.保存spu
         * 2.保存spu_detail
         * 3.保存sku
         * 4.保存stock
         */

        Spu spu=new Spu();
        spu.setTitle(spuVo.getTitle());
        spu.setSubTitle(spuVo.getSubTitle());
        spu.setBrandId(spuVo.getBrandId());
        spu.setCid1(spuVo.getCid1());
        spu.setCid2(spuVo.getCid2());
        spu.setCid3(spuVo.getCid3());

        spu.setSaleable(false);//默认保存时不上架
        spu.setValid(true);
        spu.setCreateTime(nowDate);
        spu.setLastUpdateTime(nowDate);
        spuMapper.insert(spu);

        //保存spu扩展表
        SpuDetail spuDetail = spuVo.getSpuDetail();
        spuDetail.setSpuId(spu.getId());

        spuDetailMapper.insert(spuDetail);

        //保存Sku
        List<Sku> skus=spuVo.getSkus();
        skus.forEach(sku ->{
            sku.setSpuId(spu.getId());
            sku.setEnable(true);
            sku.setCreateTime(nowDate);
            sku.setLastUpdateTime(nowDate);
            skuMapper.insert(sku);
            //库存
            Stock stock=new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);

        });
        //发送，mq消息
        this.sendMsg("insert",spu.getId());

    }

    public void sendMsg(String type,Long spuId){
        amqpTemplate.convertAndSend("item.exchanges","item."+type,spuId);
    }

    /**
     * 根据spuId查询商品集列表
     * @param spuId
     * @return
     */

    public SpuDetail findSpuDetailBySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 修改商品信息
     * @param spuVo
     */
    public void updateSpuDetail(SpuVo spuVo) {
        Date date = new Date();

        /**
         * 1.保存spu
         * 2.保存spu_detail
         * 3.保存sku
         * 4.保存stock
         */
        spuVo.setCreateTime(null);
        spuVo.setLastUpdateTime(date);
        spuVo.setSaleable(null);
        spuVo.setValid(null);

        spuMapper.updateByPrimaryKeySelective(spuVo);

        //修改spudetail
        SpuDetail spuDetail = spuVo.getSpuDetail();
        spuDetail.setSpuId(spuVo.getId());
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);

        //第三修改sku
      /*  List<Sku> skus = spuVo.getSkus();
        skus.forEach(s -> {
                    //修改sku
                    s.setEnable(false);
                    skuMapper.updateByPrimaryKey(s);
                    //库存
                    stockMapper.deleteByPrimaryKey(s.getId());
         });*/
        List<Sku> skuList=skuMapper.findSkuBySpuId(spuVo.getId());
        skuList.forEach(s->{
            skuMapper.deleteByPrimaryKey(s.getId());
            stockMapper.deleteByPrimaryKey(s.getId());
        });

            //保存Sku
        List<Sku> skus1=spuVo.getSkus();
        skus1.forEach(sku ->{
            sku.setSpuId(spuVo.getId());
            sku.setEnable(true);
            sku.setCreateTime(date);
            sku.setLastUpdateTime(date);
            skuMapper.insert(sku);
            //库存
            Stock stock=new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);

        });
        //发送，mq消息
        this.sendMsg("update",spuVo.getId());
    }

    /**
     * 根据spuId删除spu详情
     * @param spuId
     */
    public void deleteSpuBySpuId(Long spuId) {
        /**
         * 1.保存spu
         * 2.保存spu_detail
         * 3.保存sku
         * 4.保存stock
         * 多表删倒着删
         */
        //删除sku
        List<Sku> skuList = skuMapper.findSkuBySpuId(spuId);
        skuList.forEach(s ->{
            //删除sku
            s.setEnable(false);
            skuMapper.updateByPrimaryKeySelective(s);

            //库存
            stockMapper.deleteByPrimaryKey(s.getId());

        });

        //删除detail
        spuDetailMapper.deleteByPrimaryKey(spuId);
        //删除spu
        spuMapper.deleteByPrimaryKey(spuId);
        //删除商品信息发送mq
        this.sendMsg("delete",spuId);
    }

    /**
     * 操作上下架
     * @param spuId
     * @param saleable
     */
    public void upOrDown(Long spuId, int saleable) {
        Spu spu=new Spu();
        spu.setId(spuId);
        spu.setSaleable(saleable == 1?true:false);
        spuMapper.updateByPrimaryKeySelective(spu);
    }
    /**
     * 根据spuid查询spu
     * @param spuId
     * @return
     */
    public Spu findSpuById(Long spuId) {
        return spuMapper.selectByPrimaryKey(spuId);
    }

    public SpuVo findSpuBySpuId(Long spuId) {
       return spuMapper.findSpuBySpuId(spuId);
    }
}
