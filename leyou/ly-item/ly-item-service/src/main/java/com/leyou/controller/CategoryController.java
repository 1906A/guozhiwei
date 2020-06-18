package com.leyou.controller;

import com.leyou.service.CategoryService;
import com.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    //根据节点id查询所有
    @RequestMapping("list")
    public List<Category> categoryList(@RequestParam("pid") long pid){
        Category category = new Category();
        category.setParentId(pid);
        return categoryService.findcategory(category);
    }

    //测试
    @RequestMapping("id")
    public Object findCate(){
        return categoryService.findCate(6);
    }

    //添加商品分类
    @RequestMapping("add")
    public String add(@RequestBody Category category){
        String result="SUCC";
        try {

            categoryService.cateGoryAdd(category);
        }catch (Exception e) {
            System.out.println("添加商品分类异常");
            result ="FALL";
        }
        return result;

    }
    //修改商品分类
    @RequestMapping("updateCategory")
    public String updateCategory(@RequestBody Category category){
        String result="SUCC";
        try {

            categoryService.updateCategory(category);
        }catch (Exception e) {
            System.out.println("修改商品分类异常");
            result ="FALL";
        }
        return result;

    }
    //修改商品分类
    @RequestMapping("deleteById")
    public String deleteById(@RequestParam int id){
        String result="SUCC";
        try {

            categoryService.deleteById(id);
        }catch (Exception e) {
            System.out.println("删除商品分类异常");
            result ="FALL";
        }
        return result;

    }

    /**
     *根据分类id查询分类名称
     * @param id
     * @return
     */
    @RequestMapping("findCatrgotById")
    public Category findCatrgotById(@RequestParam("id") Long id){
       return categoryService.findCatrgotById(id);
    }

    /**
     * @param ids
     * @return
     */
    @RequestMapping("findCatrgotByCids")
    public  List<Category> findCatrgotByCids(@RequestBody List<Long> ids){
        return categoryService.findCatrgotByCids(ids);
    }

}
