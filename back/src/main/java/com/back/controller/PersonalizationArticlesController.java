package com.back.controller;

import com.back.dto.Response;
import com.back.get.PersonalizationArticle;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class PersonalizationArticlesController {
    @Resource
    private PersonalizationArticle personalizationArticle;

    @GetMapping("/barrier-articles")
    public Response getBarrierArticles(@RequestParam List<Integer> ids){
        personalizationArticle.getBarrierArticles(ids);
        return Response.success();
    }

    @GetMapping("/recommended-articles")
    public Response getRecommendedArticles(@RequestParam List<Integer> userIds) {
        personalizationArticle.getRecommendedArticles(userIds);
        return Response.success();
    }
}
