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
public class GetCollectData extends GetData {
    @Resource
    protected ArticleRepository articlerepository;
    @Resource
    protected UserDataRepository userdatarepository;

    //获取用户收藏文章列表
    public List<Article> getCollectArticleList(int userId){
        UserData userData= userdatarepository.findUserDataById(userId);
        List<Integer> collectArticleId = userData.getCollectArticleId();
        List<Article> articleData=new ArrayList<>();
        for (Integer integer : collectArticleId) {
            articleData.add(articlerepository.findArticleById(integer));
        }
        return articleData;
    }

    //获取用户收藏文章ID列表
    @Override
    public List<Integer> getList(int userId){
        UserData userData = userdatarepository.findUserDataById(userId);
        return userData.getCollectArticleId();
    }

    public int getCount(int userId){
        return getList(userId).size();
    }
}
