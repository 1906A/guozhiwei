package com.client;

import com.pojo.SpecGroup;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@RequestMapping("spec")
public interface SpecGroupClientServise {
    /**
     * 查询规格参数组列表
     * @return
     */
    @RequestMapping("groups/{cid}")
    public List<SpecGroup> findSpecGroupList(@PathVariable("cid") Long cateGoryId);
}
