package com.back.controller.articleSearchController;

import com.back.dto.Article.RenderedArticle;
import com.back.search.ArticleSearchOrderedViewCount;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleSearch")
public class DefaultArticleSearchController {
    @Resource
    private ArticleSearchOrderedViewCount articleSearchOrderedViewCount;
    @GetMapping("/default")
    public List<RenderedArticle> getArticles(@RequestParam String keyword){
        return articleSearchOrderedViewCount.getArticles(keyword);
    }
}
