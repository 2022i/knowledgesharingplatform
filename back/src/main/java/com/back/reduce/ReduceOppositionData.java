package com.back.reduce;

import com.back.index.Article;
import com.back.index.UserData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReduceOppositionData extends ReduceData {
    @Override
    void reduceArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> oppositionUserIds = article.getOpposeUserIds();
        if (oppositionUserIds.contains(userId)) {
            oppositionUserIds.remove((Integer) userId);
            article.setOpposeUserIds(oppositionUserIds);
            articleRepository.save(article);
        }
    }

    @Override
    void reduceUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> oppositionArticleIds = userData.getOpposeArticleIds();
        if (oppositionArticleIds.contains(articleId)) {
            oppositionArticleIds.remove((Integer) articleId);
            userData.setOpposeArticleIds(oppositionArticleIds);
            userDataRepository.save(userData);
        }
    }
}
