package com.example.springcloudclient2.controller;

import com.example.springcloudclient2.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class ProductController {
    private Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    @Autowired
    private DiscoveryClient discoveryClient = null;

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long id){
        ServiceInstance service = discoveryClient.getInstances("USER").get(0);
        log.info("["+service.getServiceId()+"]:"+service.getHost()+":"+service.getPort());
        Product product = new Product();
        product.setId(id);
        int level=(int)(id%3+1);
        product.setLevel(level);
        product.setUserName("user_name_"+id);
        return product;
    }
    @PostMapping("/insert")
    public Map<String,Object> insertUser(@RequestBody Product product){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("success",true);
        map.put("message","更新产品：【"+product.getId()+"】成功");
        return map;
    }
    @PostMapping("update/{productName}")
    public Map<String,Object> updateProductName(
            @PathVariable("productName") String productName,
            @RequestHeader("id") Long id
    ){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("success",true);
        map.put("message","更新产品【"+id+"】名称：【"+productName+"】成功。");
        return map;
    }
    @GetMapping("/timeout")
    public String timeout(){
        long ms = (long) (3000L*Math.random());
        try{
            Thread.sleep(ms);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return "熔断测试";
    }
}
