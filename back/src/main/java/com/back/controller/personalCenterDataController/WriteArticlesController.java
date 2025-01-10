package com.back.controller.personalCenterDataController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.WriteArticlesList;
import com.back.get.preparation.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class WriteArticlesController {
    @Resource
    private WriteArticlesList writeArticlesList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @Description("个人中心接口，获取用户写过的文章详细信息列表，返回文章数据用于渲染")
    @GetMapping("/getWriteArticles")
    public List<RenderedArticle> getWriteArticles(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(writeArticlesList.getArticlesList(userId));
    }
}
