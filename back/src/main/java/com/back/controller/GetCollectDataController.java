//package com.back.controller;
//
//
//import com.back.get.ArtilcesList.CollectArticlesList;
//import com.back.index.Article;
//import jakarta.annotation.Resource;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@CrossOrigin(origins = "*")
//@RequestMapping("/getdata")
//public class GetCollectDataController {
//    @Resource
//    private CollectArticlesList collectArticlesList;
//
//    //获取收藏列表
//    @GetMapping("/getCollectList")
//    public List<Article> getCollectArticleList(@RequestParam int userId) {
//        return collectArticlesList.getCollectArticleList(userId);
//    }
//
//    //获取收藏列表ID
//    @GetMapping("/getCollectIDList")
//    public List<Integer> getCollectIDList(@RequestParam int userId) {
//        return collectArticlesList.getArticlesList(userId);
//    }
//
//    //获取收藏数
//    @GetMapping("/getCollectCount")
//    public int getCount(@RequestParam int userId) {
//        return collectArticlesList.getCount(userId);
//    }
//}
