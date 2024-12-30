//package com.back.controller;
//
//import com.back.get.UserDataList.ShareUsersList;
//import com.back.index.Article;
//import jakarta.annotation.Resource;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/getList")
//@CrossOrigin(origins = "*")
//public class GetShareDataController {
//    @Resource
//    private ShareUsersList shareUsersList;
//
//    @GetMapping("/getShareList")
//    public List<Article> getShareList(@RequestParam int userId){
//        return shareUsersList.getShareList(userId);
//    }
//
//    @GetMapping("/getShareIDList")
//    public List<Integer> getArticleIDList(@RequestParam int userId){
//        return shareUsersList.getArticleIDList(userId);
//    }
//}
