package com.back.get.HomePageArticle;

import com.back.index.Article;
import com.back.index.UserData;
import org.springframework.stereotype.Service;

@Service
public class ValidArticle {
    public boolean isArticleValid(Article article, UserData userData){
        return article!=null&&
                article.isCheck() &&
                !article.isReject() &&
                !article.isDraft() &&
                !article.isDelete() &&
                !userData.getWriteArticleIds().contains(article.getId()) &&
                !userData.getSupportArticleIds().contains(article.getId()) &&
                !userData.getOpposeArticleIds().contains(article.getId()) &&
                !userData.getCollectArticleId().contains(article.getId()) &&
                !userData.getShareArticleId().contains(article.getId())&&
                !userData.getViewDataIds().contains(article.getId());
    }
}
