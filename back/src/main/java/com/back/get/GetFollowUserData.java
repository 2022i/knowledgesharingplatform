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
public class GetFollowUserData extends GetData {
    @Resource
    protected UserDataRepository userDataRepository;
    @Resource
    protected UserRepository userRepository;

    //获取关注用户列表
    public List<User> getFollowUserList(int userId){
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> followUserId = userData.getFollowUserId();
        List<User> followUsers = new ArrayList<>();
        for( Integer integer : followUserId){
            followUsers.add(userRepository.findUserById(integer));
        }
        return followUsers;
    }

    //获取关注数
    @Override
    public int getCount(int userId){
        return getList(userId).size();
    }

    //获取关注用户ID列表
    @Override
    public List<Integer> getList(int userId){
        return userDataRepository.findUserDataById(userId).getFollowUserId();
    }

}
