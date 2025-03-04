package com.back.get.ArtilcesList;

import com.back.get.ArticleIdsList.ShareArticleIdsList;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareArticlesList extends ArticlesList {
    @Resource
    private ShareArticleIdsList shareArticleIdsList;
    @Override
    public List<Article> getArticlesList(int userId){
        List<Integer> shareArticleIds = shareArticleIdsList.getIdsList(userId);
        return getArticlesList(shareArticleIds);
    }
}
