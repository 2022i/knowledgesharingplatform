package com.back.controller;

import com.back.dto.Response;
import com.back.get.GetArticleData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/getdata")
@CrossOrigin(origins = "*")
public class GetArticleDataController {
    @Resource
    private GetArticleData getArticleData;
//获取作品列表
    @GetMapping("/getArticleList")
    public Response getArticleList(@RequestParam int userId){
        getArticleData.getArticleList(userId);
        return Response.success();
    }
//获取作品数
    @GetMapping("/getArticleCount")
    public Response getCount(@RequestParam int userId){
        getArticleData.getCount(userId);
        return Response.success();
    }
    //获取作品ID列表
    @GetMapping("/getWriteArticleIDList")
    public Response getList(@RequestParam int userId){
        getArticleData.getList(userId);
        return Response.success();
    }
}
