package com.back.write;

import com.back.dto.article.ArticleFromFront;
import com.back.dto.LastId;
import com.back.dto.Response;
import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class WriteArticle {
    @Resource
    private ArticleRepository articleRepository;
    public Response writeArticle(ArticleFromFront articleFromFront) {
        Article article = new Article();
        article.setId(LastId.getArticleId());
        article.setAuthorId(articleFromFront.getAuthorId());
        article.setThemeId(articleFromFront.getThemeId());
        article.setTitle(articleFromFront.getTitle());
        article.setContent(articleFromFront.getContent());
        article.setCreateTime(LocalDateTime.now());
        articleRepository.save(article);
        return Response.successWriteArticle();
    }
}
