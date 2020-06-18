package com.client;

import com.pojo.SpecParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@RequestMapping("specParam")
public interface SpecParamClientService {

    @RequestMapping("paramsByCidAndGeneric")
    public List<SpecParam> findParamByCidAndGeneric(@RequestParam("cid") Long cid,
                                                    @RequestParam("generic") boolean generic);
}
