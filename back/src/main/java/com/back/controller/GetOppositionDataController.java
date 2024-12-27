package com.back.controller;

import com.back.get.GetOppositionData;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class GetOppositionDataController {
    @Resource
    private GetOppositionData getOppositionData;

    @GetMapping("/getOppositionList")
    public List<Article> getOppositionList(@RequestParam int userId){
        return getOppositionData.getOppositionList(userId);
    }

    @GetMapping("/getOppositionIDList")
    public List<Integer> getArticleIDList(@RequestParam int userId){
        return getOppositionData.getArticleIDList(userId);
    }
}
