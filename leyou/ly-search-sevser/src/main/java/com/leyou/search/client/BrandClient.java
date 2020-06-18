package com.leyou.search.client;

import com.client.BrandService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface BrandClient extends BrandService {

}
