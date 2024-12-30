package com.back.controller;

import com.back.get.GetShareData;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class GetShareDataController {
    @Resource
    private GetShareData getShareData;

    @GetMapping("/getShareList")
    public List<Article> getShareList(@RequestParam int userId){
        return getShareData.getShareList(userId);
    }

    @GetMapping("/getShareIDList")
    public List<Integer> getArticleIDList(@RequestParam int userId){
        return getShareData.getArticleIDList(userId);
    }
}
