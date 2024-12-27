package com.back.get;

import com.back.index.User;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import com.back.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetFansData extends GetData{
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDatarepository;
    @Resource
    protected UserRepository userRepository;

    //获取粉丝列表
    public List<User> getFansList(int userId){
        UserData userData = userDatarepository.findUserDataById(userId);
        List<Integer> fansId = userData.getFansId();
        List<User> users = new ArrayList<>();
        for(Integer integer : fansId){
            users.add(userRepository.findUserById(integer));
        }
        return users;
    }

    @Override
    //获取粉丝数
    public int getCount(int userId){
        return getList(userId).size();
    }

    //获取粉丝ID
    @Override
    public  List<Integer> getList(int userId){
        UserData userData = userDatarepository.findUserDataById(userId);
        return userData.getFansId();
    }
}
