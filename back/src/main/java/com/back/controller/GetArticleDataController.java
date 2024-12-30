package com.back.controller;

import com.back.dto.Response;
import com.back.get.GetArticleData;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getdata")
@CrossOrigin(origins = "*")
public class GetArticleDataController {
    @Resource
    private GetArticleData getArticleData;
//获取作品列表
    @GetMapping("/getArticleList")
    public List<Article> getArticleList(@RequestParam int userId){
        return getArticleData.getArticleList(userId);
    }
//获取作品数
    @GetMapping("/getArticleCount")
    public int getCount(@RequestParam int userId){
        return getArticleData.getCount(userId);
    }
    //获取作品ID列表
    @GetMapping("/getWriteArticleIDList")
    public List<Integer> getList(@RequestParam int userId){
        return getArticleData.getList(userId);
    }
}
