package com.back.index;

import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ArticleTest {
    @Resource
    private ArticleRepository articleRepository;
    private List<Integer> count =new ArrayList<>();
    @Test
    public void saveMultipleArticles() {
        count.add(1);
        count.add(2);
        List<Article> articles = IntStream.range(1, 101)
                .mapToObj(i -> {
                    Article article = new Article();
                    article.setId(i);
                    article.setAuthorId(i);
                    article.setThemeId(200 + i);
                    article.setSupportUserIds(count);
                    article.setTitle("Test Title " + i);
                    article.setContent("Test Content " + i);
                    return article;
                })
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);
    }
    @Test
    public void addArticle() {
        Article article = new Article();
        article.setId(-1);
        article.setAuthorId(3);
        article.setThemeId(200 + 3);
        article.setSupportUserIds(count);
        article.setTitle("Test Title " + 3);
        article.setContent("Test Content " + 3);
        articleRepository.save(article);
    }
}