package com.back.add;

import com.back.get.LastIdOperation;
import com.back.dto.Response;
import com.back.index.ViewData;
import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import com.back.repository.ViewDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddViewData {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    private ViewDataRepository viewDataRepository;
    @Resource
    private LastIdOperation lastIdOperation;
    private ViewData viewData =new ViewData();
    public Response addViewData(int articleId, int userId) {
        addArticleData(articleId, userId);
        addUserData(articleId, userId);
        return Response.successAddData();
    }
    private void addArticleData(int articleId, int userId){
        Article article= articleRepository.findArticleById(articleId);
        List<Integer> viewUserIds= article.getViewUserIds();
        viewUserIds.add(userId);
        article.setViewUserIds(viewUserIds);
        articleRepository.save(article);
    }
    private void addUserData(int articleId, int userId){
        List<Integer> viewArticleIds =userDataRepository.findUserDataById(userId).getViewDataIds();
        if(viewDataRepository.findViewDataByArticleId(articleId)!=null){
            viewData = viewDataRepository.findViewDataByArticleId(articleId);
        }else{
            viewData.setId(lastIdOperation.getViewArticleId());
            viewData.setArticleId(articleId);
            viewArticleIds.add(viewData.getId());
        }
        viewData.setViewTime(LocalDateTime.now());
        viewDataRepository.save(viewData);
        UserData userData=userDataRepository.findUserDataById(userId);
        userData.setViewDataIds(viewArticleIds);
        userDataRepository.save(userData);
    }
}
