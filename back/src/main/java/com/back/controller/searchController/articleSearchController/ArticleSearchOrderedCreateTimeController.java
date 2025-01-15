package com.back.controller.searchController.articleSearchController;

import com.back.dto.article.RenderedArticle;
import com.back.get.preparation.RenderedArticleBooleanSet;
import com.back.search.ArticleSearchOrderedCreateTime;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleSearch")
public class ArticleSearchOrderedCreateTimeController {
    @Resource
    private ArticleSearchOrderedCreateTime articleSearchOrderedCreateTime;
    @Resource
    private RenderedArticleBooleanSet renderedArticleBooleanSet;
    @Description("根据关键字搜索文章，按文章的发布时间由近到远排序，返回文章数据用于渲染")
    @GetMapping("/orderedCreateTime")
    public List<RenderedArticle> getArticles(@RequestParam String keyword,@RequestParam int userId){
        return renderedArticleBooleanSet.setRenderedArticleBoolean(articleSearchOrderedCreateTime.getArticles(keyword),userId);
    }
}
