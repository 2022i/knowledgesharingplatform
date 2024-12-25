package com.back.service;

import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Resource
    private ArticleRepository articleRepository;
    public List<Article> findByTitle(String title) {
        return articleRepository.findArticleByTitle(title);
    }
    public List<Article> findByContent(String content) {
        return articleRepository.findArticleByContent(content);
    }
}
