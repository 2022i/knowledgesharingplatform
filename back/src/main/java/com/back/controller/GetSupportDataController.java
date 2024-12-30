package com.back.controller;

import com.back.dto.Response;
import com.back.get.GetSupportData;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class GetSupportDataController {
    @Resource
    private GetSupportData getSupportData;
    @GetMapping("/getsupportIDList")
    public List<Integer> getArticleIDList(@RequestParam int userId){
        return getSupportData.getArticleIDList(userId);
    }

    @GetMapping("/getSupportList")
    public List<Article> getArticleList(@RequestParam int userId){
        return getSupportData.getArticleList(userId);
    }
}
