package com.back.update;

import com.back.index.Article;
import com.back.index.UserData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddCollectionData extends AddData {
    @Override
    public void updateArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> collectionUserIds = article.getCollectionUserIds();
        collectionUserIds.add(userId);
        article.setSupportUserIds(collectionUserIds);
        articleRepository.save(article);
    }
    public void updateUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> collectArticleId = userData.getCollectArticleId();
        collectArticleId.add(articleId);
        userData.setSupportArticleId(collectArticleId);
        userDataRepository.save(userData);
    }
}
