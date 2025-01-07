package com.back.get.ArticleIdsList;

import com.back.dto.article.ViewArticle;
import com.back.get.ArtilcesList.ViewArticlesList;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewArticleIdsList {
    @Resource
    private ViewArticlesList viewArticlesList;

    // 获取浏览过文章的ID
    public List<Integer> getIdsList(int userId) {
        List<ViewArticle> viewArticles = viewArticlesList.getArticlesList(userId);
        List<Integer> viewArticleIdsList = new ArrayList<>();
        for (ViewArticle viewArticle : viewArticles) {
            viewArticleIdsList.add(viewArticle.getArticleId());
        }
        return viewArticleIdsList;
    }
}
