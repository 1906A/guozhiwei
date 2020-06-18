package com.client;

import com.pojo.Sku;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@RequestMapping("sku")
public interface SkuClientService {
    @RequestMapping("list")
    public List<Sku> findSkuBySpuId(@RequestParam("id") Long id);
}
