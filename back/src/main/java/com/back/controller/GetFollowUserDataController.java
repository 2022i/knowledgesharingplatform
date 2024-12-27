package com.back.controller;


import com.back.dto.Response;
import com.back.get.GetFollowUserData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/getdata")
public class GetFollowUserDataController {

    @Resource
    private GetFollowUserData getFollowUserData;

    //获取关注列表
    @GetMapping("/getFollowUserList")
    public Response getFollowUserList(@RequestParam int userId){
        getFollowUserData.getFollowUserList(userId);
        return Response.success();
    }

    @GetMapping("/getFollowUserCount")
    public  Response getCount(@RequestParam int userId){
        getFollowUserData.getCount(userId);
        return Response.success();
    }

    @GetMapping("/getFollowUserIDList")
    public Response getList(@RequestParam int userId){
        getFollowUserData.getList(userId);
        return Response.success();
    }
}
