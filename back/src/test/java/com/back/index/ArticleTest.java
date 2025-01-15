package com.back.index;

import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class ArticleTest {
    @Resource
    private ArticleRepository articleRepository;
    @Test
    public void saveRandomTitleArticles() {
        List<Article> articles=articleRepository.findAll();
        for(Article article:articles){
            article.setCheck(true);
            articleRepository.save(article);
        }
    }
    @Test
    public void saveRandomTitleArticles2() {
        List<Article> articles=articleRepository.findAll();
        for(Article article:articles){
            article.setCheck(false);
            articleRepository.save(article);
        }
    }
}