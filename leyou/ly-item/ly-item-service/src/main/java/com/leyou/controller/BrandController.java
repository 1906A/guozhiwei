package com.leyou.controller;

import com.leyou.common.PageResult;
import com.leyou.service.BrandService;
import com.pojo.Brand;
import com.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    @RequestMapping("page")
    public Object findBrandByPage(@RequestParam("key") String key,
                                  @RequestParam("page") Integer page,
                                  @RequestParam("rows") Integer rows,
                                  @RequestParam("sortBy") String sortBy,
                                  @RequestParam("desc") boolean desc){
        System.out.println(key+"--"+page+"--"+rows+"--"+sortBy+"--"+desc);

        PageResult<Brand> brandList= brandService.findBrandLimit(key,page,rows,sortBy,desc);
        return brandList;
    }

    /**
     * 品牌添加
     * @param brand
     * @param cids
     */
    @RequestMapping("addOrEditBrand")
    public void addOrEditBrand(Brand brand, @RequestParam("cids") List<String> cids){
        //判读主键的id是否有值
        if(brand.getId()!=null){
            //修改
            brandService.updateBrand(brand,cids);

        }else {

            brandService.addOrEditBrand(brand,cids);
        }


    }

    /**
     * 品牌删除
     * @param id
     */

    @RequestMapping("deleteById/{id}")
    public void deleteById(@PathVariable("id") Long id){
        brandService.deletById(id);
    }
    /**
     * 根据品牌id查询具体分类
     * @param id
     */

    @RequestMapping("bid/{id}")
    public List<Category> findCategoryByBrandId(@PathVariable("id") Long pid){
        return brandService.findCategoryByBrandId(pid);
    }

    @RequestMapping("cid/{cid}")
    public List<Brand> findBrandBycid(@PathVariable("cid") Long cid){

        return brandService.findBrandBycid(cid);
    }


    /**
     * 根据品牌id查询品牌名称
     * @param id
     * @return
     */
    @RequestMapping("findBrandById")
    public Brand findBrandById(@RequestParam("id") Long id){
        return brandService.findBrandById(id);
    }


}
