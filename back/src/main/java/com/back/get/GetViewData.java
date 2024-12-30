package com.back.get;

import com.back.dto.ViewArticle;
import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetViewData extends GetData{
    @Resource
    protected ArticleRepository articlerepository;
    @Resource
    protected UserDataRepository userdatarepository;

    //获取用户浏览文章列表
    public List<ViewArticle> getViewList(int userId){
        UserData userData = userdatarepository.findUserDataById(userId);
        return userData.getViewArticleId();
    }

    @Override
    public int getCount(int userId){
        return getViewList(userId).size();
    }

    @Override
    public List<Integer> getList(int userId){
        List<Integer> viewArticleId = new ArrayList<>();
        for(int i=0;i<getViewList(userId).size();i++){
            viewArticleId.add(getViewList(userId).get(i+1).getArticleId());
        }
        return viewArticleId;
    }
}
