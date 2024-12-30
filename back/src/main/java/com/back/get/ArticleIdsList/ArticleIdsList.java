package com.back.get.ArticleIdsList;

import com.back.get.IdsList;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public abstract class ArticleIdsList implements IdsList {
    @Resource
    protected UserDataRepository userDataRepository;
}
