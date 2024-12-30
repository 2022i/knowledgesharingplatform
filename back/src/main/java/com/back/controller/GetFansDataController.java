package com.back.controller;

import com.back.dto.Response;
import com.back.get.GetFansData;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/getdata")
public class   GetFansDataController {
    @Resource
    private GetFansData getFansData;

    //获取粉丝列表
    @GetMapping("/getFansList")
    public List<UserData> getFansList(@RequestParam int userId){

        return getFansData.getFansList(userId);
    }

    @GetMapping("/getFansCount")
    public int getCount(@RequestParam int userId){
        return getFansData.getCount(userId);
    }

    //获取粉丝ID列表
    @GetMapping("/getFansIDList")
    public List<Integer> getList(@RequestParam int userId){
        return getFansData.getList(userId);
    }
}
