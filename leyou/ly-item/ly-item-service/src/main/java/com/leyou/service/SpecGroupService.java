package com.leyou.service;

import com.leyou.dao.SpecGroupMapper;
import com.leyou.dao.SpecParamMapper;
import com.pojo.SpecGroup;
import com.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecGroupService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 保存商品规格组
     * @param specGroup
     */

    public void saveSpecGroup(@RequestBody SpecGroup specGroup) {
        System.out.println("11111");
        specGroupMapper.insert(specGroup);

    }

    public List<SpecGroup> findSpecGroupList(Long cateGoryId) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cateGoryId);

        //根据分类id查询规格参数组及组内参数列表
        List<SpecGroup> groupList=new ArrayList<>();
        groupList=specGroupMapper.select(specGroup);
        groupList.forEach(group ->{
            SpecParam param=new SpecParam();
            param.setGroupId(group.getId());
            group.setParams(specParamMapper.select(param));
        });


        return groupList;
    }

    public void deleteBySpecGroupId(Long id){
        specGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改商品规格组
     * @param specGroup
     */
    public void updateSpecGroup(SpecGroup specGroup) {
        specGroupMapper.updateByPrimaryKey(specGroup);
    }

    /**
     * 根据组id查询组参数列表
     * @param gid
     * @return
     */
    public List<SpecParam> findSpecParam(Long gid) {
        SpecParam specParam=new SpecParam();
        specParam.setGroupId(gid);
        System.out.println(gid);
        return specParamMapper.select(specParam);
    }
}
