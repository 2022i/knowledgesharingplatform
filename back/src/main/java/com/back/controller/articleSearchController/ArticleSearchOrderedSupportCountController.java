package com.back.controller.articleSearchController;

import com.back.dto.article.RenderedArticle;
import com.back.search.ArticleSearchOrderedSupportCount;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleSearch")
public class ArticleSearchOrderedSupportCountController {
    @Resource
    private ArticleSearchOrderedSupportCount articleSearchOrderedSupportCount;
    @GetMapping("/orderedSupportCount")
    public List<RenderedArticle> getArticles(@RequestParam String keyword){
        return articleSearchOrderedSupportCount.getArticles(keyword);
    }
}
