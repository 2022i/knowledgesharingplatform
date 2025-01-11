package com.back.write;

import com.back.dto.Response;
import com.back.dto.article.ArticleFromFront;
import com.back.get.LastIdOperation;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class WriteDraft {
    @Resource
    private LastIdOperation lastIdOperation;
    @Resource
    private SaveData saveData;
    public Response writeDraft(ArticleFromFront articleFromFront){
        Article article = new Article();
        article.setId(lastIdOperation.getArticleId());
        article.setAuthorId(articleFromFront.getAuthorId());
        article.setThemeId(articleFromFront.getThemeId());
        article.setTitle(articleFromFront.getTitle());
        article.setContent(articleFromFront.getContent());
        article.setCreateTime(LocalDateTime.now());
        article.setDraft(true);
        saveData.saveData(article, article.getAuthorId());
        return Response.successWriteDraft();
    }
}
