package com.leyou.client;

import com.client.SkuClientService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface SkuClient extends SkuClientService {

}
