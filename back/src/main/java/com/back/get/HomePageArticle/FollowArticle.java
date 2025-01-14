package com.back.get.HomePageArticle;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FollowArticle extends RecommendedArticle{
    @Override
    protected void init(int userId){
        setUserData(userId);
        setSubscribedThemes();
        setFollowedAuthors();
    }
    @Override
    protected void setFollowedAuthors(){
        followedAuthors = new HashSet<>(userData.getFollowUserId());
    }
}
