package com.back.write;

import com.alibaba.dashscope.exception.ApiException;
import com.back.dto.article.ArticleFromFront;
import com.back.get.LastIdOperation;
import com.back.dto.Response;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class WriteArticle {
    @Resource
    private LastIdOperation lastIdOperation;
    @Resource
    private SaveData saveData;
    public Response writeArticle(ArticleFromFront articleFromFront) throws ApiException{
        Article article = new Article();
        article.setId(lastIdOperation.getArticleId());
        article.setAuthorId(articleFromFront.getAuthorId());
        article.setThemeId(articleFromFront.getThemeId());
        article.setTitle(articleFromFront.getTitle());
        article.setContent(articleFromFront.getContent());
        article.setSummary(articleFromFront.getSummary());
        article.setRelatedKnowledge(articleFromFront.getRelatedKnowledge());
        article.setCreateTime(LocalDateTime.now());
        saveData.saveData(article, article.getAuthorId());
        return Response.successWriteArticle();
    }
}
