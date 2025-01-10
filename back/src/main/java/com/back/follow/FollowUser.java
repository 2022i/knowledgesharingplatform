package com.back.follow;

import com.back.index.UserData;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowUser {
    @Resource
    private UserDataRepository userDataRepository;

    public void followUser(int userId, int followUserId){
        //将followUserId加入userId的关注列表
        List<Integer> followList = userDataRepository.findUserDataById(userId).getFollowUserId();
        UserData userData_fans=userDataRepository.findUserDataById(userId);
        List<Integer> fansList = userDataRepository.findUserDataById(followUserId).getFansId();
        UserData userData_follow=userDataRepository.findUserDataById(followUserId);
        if (followList.contains(followUserId) || fansList.contains(userId)) {
            throw new IllegalArgumentException("User is already followed");
        }
        followList.add(followUserId);
        userData_fans.setFollowUserId(followList);
        userDataRepository.save(userData_fans);
        fansList.add(userId);
        userData_follow.setFansId(fansList);
        userDataRepository.save(userData_follow);
    }
}
