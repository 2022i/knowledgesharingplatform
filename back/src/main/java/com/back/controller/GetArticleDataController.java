//package com.back.controller;
//
//import com.back.index.Article;
//import jakarta.annotation.Resource;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/getData")
//@CrossOrigin(origins = "*")
//public class GetArticleDataController {
//    @Resource
//    private GetWriteArticleData GETWriteArticleData;
////获取作品列表
//    @GetMapping("/getArticleList")
//    public List<Article> getArticleList(@RequestParam int userId){
//        return GETWriteArticleData.getArticleList(userId);
//    }
////获取作品数
//    @GetMapping("/getArticleCount")
//    public int getCount(@RequestParam int userId){
//        return GETWriteArticleData.getCount(userId);
//    }
//    //获取作品ID列表
//    @GetMapping("/getWriteArticleIDList")
//    public List<Integer> getList(@RequestParam int userId){
//        return GETWriteArticleData.getArticlesList(userId);
//    }
//}
