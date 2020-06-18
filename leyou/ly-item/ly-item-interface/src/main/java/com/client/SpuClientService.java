package com.client;

import com.leyou.common.PageResult;
import com.pojo.Spu;
import com.pojo.SpuDetail;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vo.SpuVo;

@RequestMapping("spu")
public interface SpuClientService {

    @RequestMapping("/page")
    public PageResult<SpuVo> findSpuByPage(@RequestParam("key") String key,
                                           @RequestParam("page") Integer page,
                                           @RequestParam("rows") Integer rows,
                                           @RequestParam("saleable") Integer saleable);

    @RequestMapping("detail/{spuId}")
    public SpuDetail findSpuDetailBySpuId(@PathVariable("spuId") Long spuId);
    /**
     * 根据spuid查询spu
     * @param spuId
     * @return
     */
    @RequestMapping("findSpuById")
    public Spu findSpuById(@RequestParam("spuId") Long spuId);
    @RequestMapping("findSpuBySpuId")
    public SpuVo findSpuBySpuId(@RequestParam("spuId") Long spuId);
}
