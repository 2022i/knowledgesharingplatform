package com.back.follow;

import com.back.index.UserData;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnFollowUser {
    @Resource
    private UserDataRepository userDataRepository;

    public void unFollowUser(int userId, int followUserId){
        //将followUserId从userId的关注列表删除
        List<Integer> followList = userDataRepository.findUserDataById(userId).getFollowUserId();
        if (!followList.contains(followUserId)) {
            throw new IllegalArgumentException("User is already unfollowed");
        }
        followList.remove(Integer.valueOf(followUserId));
        UserData userData_fans=userDataRepository.findUserDataById(userId);
        userData_fans.setFollowUserId(followList);
        userDataRepository.save(userData_fans);
        //将userId从followUserId的粉丝列表删除
        List<Integer> fansList = userDataRepository.findUserDataById(followUserId).getFansId();
        fansList.remove(Integer.valueOf(userId));
        UserData userData_follow=userDataRepository.findUserDataById(followUserId);
        userData_follow.setFansId(fansList);
        userDataRepository.save(userData_follow);
    }
}

