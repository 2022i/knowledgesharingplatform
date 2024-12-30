//package com.back.controller;
//
//
//import com.back.get.UserDataList.FollowUsersList;
//import com.back.index.UserData;
//import jakarta.annotation.Resource;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@CrossOrigin(origins = "*")
//@RequestMapping("/getdata")
//public class GetFollowUserDataController {
//
//    @Resource
//    private FollowUsersList followUsersList;
//
//    //获取关注列表
//    @GetMapping("/getFollowUserList")
//    public List<UserData> getFollowUserList(@RequestParam int userId){
//        return followUsersList.getFollowUserList(userId);
//    }
//
//    @GetMapping("/getFollowUserCount")
//    public  int getCount(@RequestParam int userId){
//        return followUsersList.getCount(userId);
//    }
//
//    @GetMapping("/getFollowUserIDList")
//    public List<Integer> getList(@RequestParam int userId){
//        return followUsersList.getArticlesList(userId);
//    }
//}
