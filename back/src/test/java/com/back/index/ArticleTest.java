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
    //Generate Random Title
    @Test
    public void saveRandomTitleArticles() {
        count.add(1);
        count.add(2);
        List<Article> articles = IntStream.range(1, 101)
                .mapToObj(i -> {
                    Article article = new Article();
                    article.setId(i);
                    article.setAuthorId(1);
                    article.setThemeId(i);
                    article.setSupportUserIds(count);
                    article.setTitle("Test Title " + (int)(Math.random()*100));
                    article.setContent("Test Content " + (int)(Math.random()*100));
                    return article;
                })
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);
    }
}