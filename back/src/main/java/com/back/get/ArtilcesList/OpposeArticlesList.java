package com.back.get.ArtilcesList;

import com.back.get.ArticleIdsList.OpposeArticleIdsList;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpposeArticlesList extends ArticlesList {
    @Resource
    private OpposeArticleIdsList opposeArticleIdsList;
    @Override
    public List<Article> getArticlesList(int userId){
        List<Integer> opposeArticleIds = opposeArticleIdsList.getIdsList(userId);
        return getArticlesList(opposeArticleIds);
    }
}
