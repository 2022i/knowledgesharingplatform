package com.back.get.preparation;

import com.back.dto.ArticleAuthor;
import com.back.index.UserData;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserRenderingPreparation {
    public List<ArticleAuthor> getArticleAuthors(List<UserData> userDataList,int currentUserId){
        List<ArticleAuthor> articleAuthors = new ArrayList<>();
        for (UserData userData : userDataList) {
            articleAuthors.add(getArticleAuthor(userData,currentUserId));
        }
        return articleAuthors;
    }
    public ArticleAuthor getArticleAuthor(UserData userData,int currentUserId){
        ArticleAuthor articleAuthor = new ArticleAuthor();
        articleAuthor.setId(userData.getId());
        articleAuthor.setUsername(userData.getUsername());
        articleAuthor.setFollowed(isFollowed(userData,currentUserId));
        return articleAuthor;
    }
    private boolean isFollowed(UserData userData,int currentUserId){
        return userData.getFansId().contains(currentUserId);
    }
}
