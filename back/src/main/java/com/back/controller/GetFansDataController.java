//package com.back.controller;
//
//import com.back.get.UserDataList.FansList;
//import com.back.index.UserData;
//import jakarta.annotation.Resource;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@CrossOrigin(origins = "*")
//@RequestMapping("/getdata")
//public class   GetFansDataController {
//    @Resource
//    private FansList fansList;
//
//    //获取粉丝列表
//    @GetMapping("/getFansList")
//    public List<UserData> getFansList(@RequestParam int userId){
//
//        return fansList.getFansList(userId);
//    }
//
//    @GetMapping("/getFansCount")
//    public int getCount(@RequestParam int userId){
//        return fansList.getCount(userId);
//    }
//
//    //获取粉丝ID列表
//    @GetMapping("/getFansIDList")
//    public List<Integer> getList(@RequestParam int userId){
//        return fansList.getArticlesList(userId);
//    }
//}
