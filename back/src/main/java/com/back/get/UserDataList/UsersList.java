package com.back.get.UserDataList;

import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;

import java.util.List;

public abstract class UsersList {
    @Resource
    protected UserDataRepository userDataRepository;
    @Resource
    protected ArticleRepository articleRepository;
    abstract List<UserData> getUserDataList(int id);
}
