package com.leyou.client;

import com.client.SpecParamClientService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface SpecParamClient extends SpecParamClientService {
}
