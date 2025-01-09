package com.back.get.HomePageArticle;

import com.back.dto.article.ViewArticle;
import com.back.get.ArticleIdsList.ViewArticleIdsList;
import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.ThemeRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PersonalizationArticleForUser {
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    private ViewArticleIdsList viewArticleIdsList;
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private ThemeRepository themeRepository;


    public List<Article> getRecommendedArticles(int userId) {
        return getArticleByViewFrequency(userId,true);
    }

    public List<Article> getBarrierArticles(int userId) {
        return getArticleByViewFrequency(userId,false);
    }

    public List<Article> getArticleByViewFrequency(int userId,boolean isMostView){
        UserData userData=userDataRepository.findUserDataById(userId);
        List<Integer> viewArticleIds=viewArticleIdsList.getIdsList(userId);
        List<Article> articles=new ArrayList<>();
        for(Integer viewArticleId:viewArticleIds){
            Article article=articleRepository.findArticleById(viewArticleId);
            if(article.isCheck() && !article.isReject() && !article.isDraft() && !article.isDelete()){
                articles.add(article);
            }
        }
        // 统计每个 themeId 的出现次数
        Map<Integer, Long> themeCountMap = articles.stream()
                .collect(Collectors.groupingBy(Article::getThemeId, Collectors.counting()));

        // 根据传入的 isMostView 参数，决定查找最多或最少出现的 themeId
        Integer targetThemeId = themeCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 0) // 确保不为空
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            if (isMostView) {
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
