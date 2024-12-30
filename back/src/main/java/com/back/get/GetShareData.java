package com.back.get;

import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetShareData extends GetList {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDataRepository;

    //获取用户分享文章列表
    public List<Article> getShareList(int userId){
        UserData userData= userDataRepository.findUserDataById(userId);
        List<Integer> shareArticleId = userData.getShareArticleId();
        List<Article> articleData=new ArrayList<>();
        for (Integer integer : shareArticleId) {
            articleData.add(articleRepository.findArticleById(integer));
        }
        return articleData;
    }

    //获取用户分享文章ID列表
    @Override
    public List<Integer> getArticleIDList(int userId){
        UserData userData = userDataRepository.findUserDataById(userId);
        return userData.getShareArticleId();
    }
}
