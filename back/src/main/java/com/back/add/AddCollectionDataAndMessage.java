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
    private List<Integer> collectionUserIds;
    private List<Integer> collectArticleId;
    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        collectionUserIds.add(userId);
        article.setCollectUserIds(collectionUserIds);
        articleRepository.save(article);

    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        collectArticleId.add(articleId);
        userData.setCollectArticleId(collectArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentCollectMessage.sentMessage(articleId,messageGenerator);
    }
    @Override
    protected boolean isAvailable(int articleId, int userId){
        collectionUserIds = collectUserIdsList.getIdsList(articleId);
        collectArticleId = collectArticleIdsList.getIdsList(userId);
        return !collectionUserIds.contains(userId)&&!collectArticleId.contains(articleId);
    }
}
