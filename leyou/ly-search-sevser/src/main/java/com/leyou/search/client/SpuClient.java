package com.leyou.search.client;

import com.client.SpuClientService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface SpuClient extends SpuClientService {

}
