package com.back.controller.searchController.articleSearchController;

import com.back.dto.article.RenderedArticle;
import com.back.search.ArticleSearchOrderedViewCount;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleSearch")
public class DefaultArticleSearchController {
    @Resource
    private ArticleSearchOrderedViewCount articleSearchOrderedViewCount;
    @Description("根据关键字搜索文章，按文章的浏览量由高到低排序，返回文章数据用于渲染，是默认搜索结果的排序方式")
    @GetMapping("/default")
    public List<RenderedArticle> getArticles(@RequestParam String keyword,@RequestParam int userId) {
        return articleSearchOrderedViewCount.getArticles(keyword, userId);
    }
}
