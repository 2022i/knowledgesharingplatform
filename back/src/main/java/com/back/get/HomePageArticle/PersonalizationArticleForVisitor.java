package com.back.get.HomePageArticle;

import com.back.get.ArticleIdsList.ViewArticleIdsList;
import com.back.index.Article;
import com.back.repository.ArticleRepository;
import com.back.repository.ThemeRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonalizationArticleForVisitor {

    @Resource
    private ViewArticleIdsList viewArticleIdsList;
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private ThemeRepository themeRepository;

    // 获取浏览最多的主题文章
    public List<Article> getRecommendedArticles(List<Integer> Ids) {
        return getArticlesByThemeFrequency(Ids, true);
    }

    // 获取浏览最少的主题文章
    public List<Article> getBarrierArticles(List<Integer> Ids) {
        return getArticlesByThemeFrequency(Ids, false);
    }

    // 通用方法，获取根据主题出现频率最多或最少的文章
    public List<Article> getArticlesByThemeFrequency(List<Integer> Ids, boolean isMostFrequent) {
        // 获取主题ID列表
        List<Integer> themeIdsList = Ids.stream()
                .map(id -> articleRepository.findArticleById(id))
                .filter(article -> article.isCheck() && !article.isReject() && !article.isDraft() && !article.isDelete())
                .map(Article::getThemeId)
                .collect(Collectors.toList());

        // 统计每个 themeId 的出现次数
        Map<Integer, Long> themeCountMap = themeIdsList.stream()
                .collect(Collectors.groupingBy(themeId -> themeId, Collectors.counting()));

        // 根据传入的 isMostFrequent 参数，决定查找最多或最少出现的 themeId
        Integer targetThemeId = themeCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 0) // 确保不为空
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            if (isMostFrequent) {
                                return list.stream()
                                        .max(Map.Entry.comparingByValue()) // 查找出现次数最多的 themeId
                                        .map(Map.Entry::getKey)
                                        .orElseThrow(() -> new NoSuchElementException("No theme found"));
                            } else {
                                return list.stream()
                                        .min(Map.Entry.comparingByValue()) // 查找出现次数最少的 themeId
                                        .map(Map.Entry::getKey)
                                        .orElseThrow(() -> new NoSuchElementException("No theme found"));
                            }
                        }
                ));

        // 根据出现次数最多或最少的 themeId 查找文章
        return articleRepository.findArticleByThemeId(targetThemeId);
    }
}
