package com.back.controller.articleSearchController;

import com.back.dto.article.RenderedArticle;
import com.back.search.ArticleSearchOrderedSupportCount;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleSearch")
public class ArticleSearchOrderedSupportCountController {
    @Resource
    private ArticleSearchOrderedSupportCount articleSearchOrderedSupportCount;
    @Description("根据关键字搜索文章，按文章的点赞数由高到低排序，返回文章数据用于渲染")
    @GetMapping("/orderedSupportCount")
    public List<RenderedArticle> getArticles(@RequestParam String keyword){
        return articleSearchOrderedSupportCount.getArticles(keyword);
    }
}
