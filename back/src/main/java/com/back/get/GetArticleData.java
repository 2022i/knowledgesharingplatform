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
public class GetArticleData extends GetData {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDatarepository;

    //获取用户作品列表
    public List<Article> getArticleList(int userId){
        UserData userData= userDatarepository.findUserDataById(userId);
        List<Integer> writeArticleId = userData.getWriteArticleId();
        List<Article> articleData=new ArrayList<>();
        for (Integer integer : writeArticleId) {
            articleData.add(articleRepository.findArticleById(integer));
        }
        return articleData;
    }
    //获取用户作品数
    @Override
    public List<Integer> getList(int userId){
        UserData userData = userDatarepository.findUserDataById(userId);
        return userData.getWriteArticleId();
    }
    @Override
    public int getCount(int userId){
        return  getList(userId).size();
    }
}
