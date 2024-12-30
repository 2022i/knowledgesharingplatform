//package com.back.controller;
//
//import com.back.get.ArtilcesList.SupportArticlesList;
//import com.back.index.Article;
//import jakarta.annotation.Resource;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/getList")
//@CrossOrigin(origins = "*")
//public class GetSupportDataController {
//    @Resource
//    private SupportArticlesList supportArticlesList;
//    @GetMapping("/getsupportIDList")
//    public List<Integer> getArticleIDList(@RequestParam int userId){
//        return supportArticlesList.getArticleIDList(userId);
//    }
//
//    @GetMapping("/getSupportList")
//    public List<Article> getArticleList(@RequestParam int userId){
//        return supportArticlesList.getArticleList(userId);
//    }
//}
