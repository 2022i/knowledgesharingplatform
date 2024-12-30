package com.back.get.UserIdsList;

import com.back.get.IdsList;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public abstract class UserIdsList implements IdsList {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDataRepository;
}
