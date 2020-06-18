package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.PageResult;
import com.leyou.dao.BrandMapper;
import com.pojo.Brand;
import com.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    BrandMapper brandMapper;


    /**
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */

    public PageResult<Brand> findBrand(String key, Integer page, Integer rows, String sortBy, boolean desc) {
        //pageHelper

        PageHelper.startPage(page,rows);
        List<Brand>brandList=brandMapper.findBrand(key,sortBy,desc);

        PageInfo<Brand> pageInfo=new PageInfo<Brand>(brandList);

        return new PageResult<Brand>(pageInfo.getTotal(),pageInfo.getList());
    }
    public PageResult<Brand> findBrandLimit(String key, Integer page, Integer rows, String sortBy, boolean desc) {
        //手写sql

        //PageHelper.startPage(page,rows);
        List<Brand>brandList=brandMapper.findBrandLimit(key,(page-1)*rows,rows,sortBy,desc);

        //查询总条数
        Long brandCount=brandMapper.findBrandCount(key,sortBy,desc);
        //PageInfo<Brand> pageInfo=new PageInfo<Brand>(brandList);

        return new PageResult<Brand>(brandCount,brandList);
    }


    public void addOrEditBrand(Brand brand, List<String> cids) {
        //1保存brand
        brandMapper.insert(brand);
        //2.保存tb_category_brand
        cids.forEach(id->{
            brandMapper.addBrandAndCategory(brand.getId(),Long.parseLong(id));
        });
    }

    public void deletById(Long id) {
        //第一删除brand
        Brand brand=new Brand();
        brand.setId(id);
        brandMapper.deleteByPrimaryKey(brand);

        //第二删除tb_category_brand
        System.out.println(id);
        brandMapper.deleteBrandAndCategory(id);

    }

    public List<Category> findCategoryByBrandId(Long pid) {
        return brandMapper.findCategoryByBrandId(pid);
    }

    public void updateBrand(Brand brand, List<String> cids) {
        //1修改品牌表
        brandMapper.updateByPrimaryKey(brand);
        //2.修改品牌和分类的关系表
        //先删掉当前品牌下所有分类，再重新添加
        brandMapper.deleteBrandAndCategory(brand.getId());
        cids.forEach(cid ->{
            brandMapper.addBrandAndCategory(brand.getId(),Long.parseLong(cid));
        });

    }

    public List<Brand> findBrandBycid(Long cid) {
        return brandMapper.findBrandBycid(cid);
    }
    /**
     * 根据品牌id查询品牌名称
     * @param id
     * @return
     */
    public Brand findBrandById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }
}
