package com.leyou.client;

import com.leyou.pojo.client.UserClientService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service")
public interface UserClient extends UserClientService {

}
