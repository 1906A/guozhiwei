package com.leyou.client;

import com.client.SpecClientService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface SpecClient extends SpecClientService {

}
