package com.leyou.controller;

import com.leyou.common.PageResult;
import com.leyou.service.SpuService;
import com.pojo.Spu;
import com.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.SpuVo;

@RestController
@RequestMapping("spu")
public class SpuController<spuVo> {

    @Autowired
    SpuService spuService;

    @RequestMapping("page")
    public PageResult<SpuVo> findSpuByPage(@RequestParam("key") String key,
                                           @RequestParam("page") Integer page,
                                           @RequestParam("rows") Integer rows,
                                           @RequestParam("saleable") Integer saleable){
        System.out.println("1111111");
        return spuService.findSpuByPage(key,page,rows,saleable);

    }

    /**
     * 保存商品信息
     * @param spuVo
     */
    @RequestMapping("savaOrUpdateGoods")
    public void saveSpuDetail(@RequestBody SpuVo spuVo){
        if(spuVo.getId()!=null){
            spuService.updateSpuDetail(spuVo);
        }else {
            spuService.saveSpuDetail(spuVo);

        }

    }

    @RequestMapping("detail/{spuId}")
    public SpuDetail findSpuDetailBySpuId(@PathVariable("spuId") Long spuId){

        return spuService.findSpuDetailBySpuId(spuId);
    }

    /**
     * 根据spuId删除spu详情
     * @param spuId
     */
    @RequestMapping("deleteById/{id}")
    public void deleteSpuBySpuId(@PathVariable("id") Long spuId){
        spuService.deleteSpuBySpuId(spuId);
    }

    /**
     * 操作上下架
     * @param spuId
     */
    @RequestMapping("upOrDown")
    public void upOrDown(@RequestParam("spuId") Long spuId,@RequestParam("saleable") int saleable){
        spuService.upOrDown(spuId,saleable);
    }

    /**
     * 根据spuid查询spu
     * @param spuId
     * @return
     */
    @RequestMapping("findSpuById")
    public Spu findSpuById(@RequestParam("spuId") Long spuId){
        return spuService.findSpuById(spuId);
    }


    @RequestMapping("findSpuBySpuId")
    public SpuVo findSpuBySpuId(@RequestParam("spuId") Long spuId){
        return  spuService.findSpuBySpuId(spuId);

    }


}
