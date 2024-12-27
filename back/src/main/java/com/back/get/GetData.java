package com.back.get;

import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;

import java.util.List;

public abstract class GetData {
    @Resource
    protected ArticleRepository articlerepository;
    @Resource
    protected UserDataRepository userdatarepository;

    public void getData(int articleId,int userId){
        getCount(userId);
        getList(userId);
    }

    abstract int getCount(int userId);
    abstract List<Integer> getList(int userId);

}
