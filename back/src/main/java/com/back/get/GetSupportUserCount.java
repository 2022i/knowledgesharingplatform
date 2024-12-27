package com.back.get;

import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetSupportUserCount {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDatarepository;
    @Override
    public List<Integer> getSupportUserList(int userId){
        return userDatarepository.findUserDataById(userId).getSupportArticleId();
    }
}
