package com.leyou.controller;

import com.leyou.service.SpecParamService;
import com.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("specParam")
public class SpecParamController {
    @Autowired
    private SpecParamService specParamService;

    /**
     * 新增规格参数组下的参数
     * @param specParam
     */
    @RequestMapping("param")
    public void saveSpecParam(@RequestBody SpecParam specParam){
       if(specParam.getId()!=null){
           specParamService.updateSpecParam(specParam);
       }else {
           specParamService.saveSpecParam(specParam);
       }
    }

    /**
     * 删除规格参数组下的参数
     * @param id
     */
    @RequestMapping("param/{id}")
    public void deletSpecParamByid(@PathVariable("id") Long id){

        specParamService.deletSpecParamByid(id);
    }

    /**
     *根据分类id查询规格参数集合
     *
     * @param cid
     * @return
     */
    @RequestMapping("params")
    public List<SpecParam> findSpecParamBycid(@RequestParam("cid") Long cid){

        return specParamService.findSpecParamBycid(cid);
    }
    /**
     *根据三级分类id+搜索条件为1的参数查询规格参数集合
     *
     * @param cid
     * @return
     */
    @RequestMapping("paramsByCid")
    public List<SpecParam> findSpecParamByCidAndSearching(@RequestParam("cid") Long cid){
        return specParamService.findSpecParamByCidAndSearching(cid);
    }
    /**
     *根据三级分类id是否通用参数值
     *
     * @param cid
     * @return
     */
    @RequestMapping("paramsByCidAndGeneric")
    public List<SpecParam> findParamByCidAndGeneric(@RequestParam("cid") Long cid,
                                                    @RequestParam("generic") boolean generic){

        return specParamService.findParamByCidAndGeneric(cid,generic);
    }
}
