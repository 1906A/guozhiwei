package com.leyou.dao;

import com.pojo.Spu;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import vo.SpuVo;

import java.util.List;

public interface SpuMapper extends Mapper<Spu> {


    List<SpuVo> findSpuByPage(@Param("key")String key,
                              @Param("saleable")Integer saleable);

    SpuVo findSpuBySpuId(@Param("spuId")Long spuId);
}
