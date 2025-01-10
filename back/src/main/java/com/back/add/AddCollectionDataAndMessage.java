package com.back.add;

import com.back.sentSystemMessage.SentCollectMessage;
import com.back.get.ArticleIdsList.CollectArticleIdsList;
import com.back.get.UserIdsList.CollectUserIdsList;
import com.back.index.Article;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddCollectionDataAndMessage extends AddDataAndMessage {
    @Resource
    private SentCollectMessage sentCollectMessage;
    @Resource
    private CollectUserIdsList collectUserIdsList;
    @Resource
    private CollectArticleIdsList collectArticleIdsList;
    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> collectionUserIds = collectUserIdsList.getIdsList(articleId);
        collectionUserIds.add(userId);
        article.setSupportUserIds(collectionUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> collectArticleId = collectArticleIdsList.getIdsList(userId);
        collectArticleId.add(articleId);
        userData.setCollectArticleId(collectArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentCollectMessage.sentMessage(articleId,messageGenerator);
    }
}
