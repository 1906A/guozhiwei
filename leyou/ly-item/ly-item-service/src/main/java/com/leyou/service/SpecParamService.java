package com.leyou.service;

import com.leyou.dao.SpecParamMapper;
import com.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamService {
    @Autowired
    SpecParamMapper specParamMapper;

    public void saveSpecParam(SpecParam specParam) {
        specParamMapper.insert(specParam);
    }

    public void updateSpecParam(SpecParam specParam) {
        specParamMapper.updateByPrimaryKey(specParam);
    }

    public void deletSpecParamByid(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }

    /**
     *根据分类id查询规格参数集合
     *
     * @param cid
     * @return
     */
    public List<SpecParam> findSpecParamBycid(Long cid) {
        return specParamMapper.findSpecParamBycid(cid);
    }

    /**
     *根据三级分类id+搜索条件为1的参数查询规格参数集合
     *
     * @param cid
     * @return
     */
    public List<SpecParam> findSpecParamByCidAndSearching(Long cid) {

        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        specParam.setSearching(true);
        return specParamMapper.select(specParam);
    }
    /**
     *根据三级分类id是否通用参数值
     *
     * @param cid
     * @return
     */
    public List<SpecParam> findParamByCidAndGeneric(Long cid, boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        return specParamMapper.select(specParam);
    }
}
