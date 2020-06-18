package com.leyou.client;


import com.client.CategoryService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface CategoryClient extends CategoryService {

}
