package com.back.get;

import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetArticleData extends GetData {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDatarepository;
    @Override
    public List<Integer> getList(int userId){
        UserData userData= userDatarepository.findUserDataById(userId);
        return userData.getWriteArticleId();
    }
    public int getCount(int userId){
        return  getList(userId).size();
    }
}
