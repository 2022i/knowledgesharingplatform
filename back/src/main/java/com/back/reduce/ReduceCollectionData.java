package com.back.reduce;

import com.back.index.Article;
import com.back.index.UserData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReduceCollectionData extends ReduceData {
    @Override
    void reduceArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> collectionUserIds = article.getCollectUserIds();
        if (collectionUserIds.contains(userId)) {
            collectionUserIds.remove((Integer) userId);
            article.setCollectUserIds(collectionUserIds);
            articleRepository.save(article);
        }
    }

    @Override
    void reduceUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> collectionArticleIds = userData.getCollectArticleId();
        if (collectionArticleIds.contains(articleId)) {
            collectionArticleIds.remove((Integer) articleId);
            userData.setCollectArticleId(collectionArticleIds);
            userDataRepository.save(userData);
        }
    }
}
