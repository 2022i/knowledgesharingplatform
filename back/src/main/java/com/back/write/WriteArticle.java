package com.back.write;

import com.back.dto.FrontArticle;
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
    public Response writeArticle(FrontArticle frontArticle) {
        Article article = new Article();
        article.setId(LastId.getArticleId());
        article.setAuthorId(frontArticle.getAuthorId());
        article.setThemeId(frontArticle.getThemeId());
        article.setTitle(frontArticle.getTitle());
        article.setContent(frontArticle.getContent());
        article.setCreateTime(LocalDateTime.now());
        articleRepository.save(article);
        return Response.successWriteArticle();
    }
}
