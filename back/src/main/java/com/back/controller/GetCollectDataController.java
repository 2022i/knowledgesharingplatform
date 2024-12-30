package com.back.controller;


import com.back.get.GetCollectData;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/getdata")
public class GetCollectDataController {
    @Resource
    private GetCollectData getCollectData;

    //获取收藏列表
    @GetMapping("/getCollectList")
    public List<Article> getCollectArticleList(@RequestParam int userId) {
        return getCollectData.getCollectArticleList(userId);
    }

    //获取收藏列表ID
    @GetMapping("/getCollectIDList")
    public List<Integer> getCollectIDList(@RequestParam int userId) {
        return getCollectData.getList(userId);
    }

    //获取收藏数
    @GetMapping("/getCollectCount")
    public int getCount(@RequestParam int userId) {
        return getCollectData.getCount(userId);
    }
}
