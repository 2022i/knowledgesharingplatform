package com.back.get.HomePageArticle;


import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import com.back.repository.ViewDataRepository;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotArticle {
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private ViewDataRepository viewDataRepository;


    public List<Article> getHotArticles() {
        // 获取所有文章

        List<Article> allArticles = articleRepository.findAll();

        // 过滤文章状态
        List<Article> validArticles = allArticles.stream()
                .filter(article -> article.isCheck() && !article.isReject() && !article.isDraft() && !article.isDelete())
                .collect(Collectors.toList());

        // 统计每篇文章的ViewUser数量
// 统计每篇文章的ViewUser数量
        Map<Article, Long> articleViewCountMap = validArticles.stream()
                .collect(Collectors.toMap(
                        article -> article,
                        article -> (long) article.getViewUserIds().size()
                ));


// 按ViewUser数量由高到低排序，并获取所有文章
        List<Article> hotArticles = articleViewCountMap.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return hotArticles;
    }
}
