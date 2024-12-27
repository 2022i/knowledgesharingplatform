package com.back.controller;


import com.back.dto.Response;
import com.back.get.GetFollowUserData;
import com.back.index.User;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/getdata")
public class GetFollowUserDataController {

    @Resource
    private GetFollowUserData getFollowUserData;

    //获取关注列表
    @GetMapping("/getFollowUserList")
    public List<UserData> getFollowUserList(@RequestParam int userId){
        return getFollowUserData.getFollowUserList(userId);
    }

    @GetMapping("/getFollowUserCount")
    public  int getCount(@RequestParam int userId){
        return getFollowUserData.getCount(userId);
    }

    @GetMapping("/getFollowUserIDList")
    public List<Integer> getList(@RequestParam int userId){
        return getFollowUserData.getList(userId);
    }
}
