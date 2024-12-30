package com.back.add;

import com.back.index.Article;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddCollectionData extends AddData {
    @Resource
    private SentCollectMessage sentMessage;
    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> collectionUserIds = article.getCollectionUserIds();
        collectionUserIds.add(userId);
        article.setSupportUserIds(collectionUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> collectArticleId = userData.getCollectArticleId();
        collectArticleId.add(articleId);
        userData.setSupportArticleId(collectArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentMessage.sentMessage(articleId,messageGenerator);
    }
}
