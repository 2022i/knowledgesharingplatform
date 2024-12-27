package com.back.controller;

import com.back.dto.Response;
import com.back.get.GetFansData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/getdata")
public class   GetFansDataController {
    @Resource
    private GetFansData getFansData;

    //获取粉丝列表
    @GetMapping("/getFansList")
    public Response getFansList(@RequestParam int userId){
        getFansData.getFansList(userId);
        return Response.success();
    }

    @GetMapping("/getFansCount")
    public Response getCount(@RequestParam int userId){
        getFansData.getCount(userId);
        return Response.success();
    }

    //获取粉丝ID列表
    @GetMapping("/getFansIDList")
    public Response getList(@RequestParam int userId){
        getFansData.getList(userId);
        return Response.success();
    }
}
