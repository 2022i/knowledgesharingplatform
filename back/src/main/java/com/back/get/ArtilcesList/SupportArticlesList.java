package com.back.get.ArtilcesList;

import com.back.get.ArticleIdsList.SupportArticleIdsList;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportArticlesList extends ArticlesList {
    @Resource
    private SupportArticleIdsList supportArticleIdsList;
    @Override
    public List<Article> getArticlesList(int userId){
        List<Integer> supportArticleIds = supportArticleIdsList.getIdsList(userId);
        return getArticlesList(supportArticleIds);
    }
}
