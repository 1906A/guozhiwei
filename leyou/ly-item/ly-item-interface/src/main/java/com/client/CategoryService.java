package com.client;

import com.pojo.Category;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryService {
    @RequestMapping("findCatrgotById")
    public Category findCatrgotById(@RequestParam("id") Long id);
    @RequestMapping("findCatrgotByCids")
    public List<Category> findCatrgotByCids(@RequestBody List<Long> ids);
}
