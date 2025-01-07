package com.back.controller.articleSearchController;

import com.back.dto.article.RenderedArticle;
import com.back.search.ArticleSearchOrderedCreateTime;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleSearch")
public class ArticleSearchOrderedCreateTimeController {
    @Resource
    private ArticleSearchOrderedCreateTime articleSearchOrderedCreateTime;
    @GetMapping("/orderedCreateTime")
    public List<RenderedArticle> getArticles(@RequestParam String keyword){
        return articleSearchOrderedCreateTime.getArticles(keyword);
    }
}
