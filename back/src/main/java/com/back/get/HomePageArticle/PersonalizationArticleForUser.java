package com.back.get.HomePageArticle;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PersonalizationArticleForUser extends RecommendedArticle{
    private Set<Integer> interestedArticleIds;

    @Override
    protected void init(int userId){
        setUserData(userId);
        setInterestedArticleIds();
        setSubscribedThemes();
        setFollowedAuthors();
    }
    @Override
    protected void setFollowedAuthors(){
        followedAuthors = new HashSet<>(userData.getFollowUserId());
        for (Integer articleId : interestedArticleIds) {
            articleRepository.findById(articleId).ifPresent(article -> followedAuthors.add(article.getAuthorId()));
        }
    }
    private void setInterestedArticleIds(){
        interestedArticleIds = new HashSet<>();
        interestedArticleIds.addAll(userData.getSupportArticleIds());
        interestedArticleIds.addAll(userData.getCollectArticleId());
        interestedArticleIds.addAll(userData.getViewDataIds());
        interestedArticleIds.addAll(userData.getShareArticleId());
    }
}
