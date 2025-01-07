package com.back.get.ArtilcesList;

import com.back.dto.Article.ViewArticle;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewArticlesList {
    @Resource
    protected UserDataRepository userDataRepository;

    public ViewArticlesList(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public List<ViewArticle> getArticlesList(int userId){
        return userDataRepository.findUserDataById(userId).getViewArticleList();
    }

}
