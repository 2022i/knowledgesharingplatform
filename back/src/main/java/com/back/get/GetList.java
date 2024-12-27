package com.back.get;

import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;

public abstract class GetList {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDataRepository;

    public void getList(int userId);
}
