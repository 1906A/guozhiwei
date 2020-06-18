package com.leyou.client;

import com.client.SpecClientService;
import com.client.SpecGroupClientServise;
import com.pojo.SpecGroup;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface SpecGroupClient extends SpecGroupClientServise {

}
