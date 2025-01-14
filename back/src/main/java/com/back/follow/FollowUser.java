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
        UserData followUserData=userDataRepository.findUserDataById(followUserId);
        List<Integer> fansList = followUserData.getFansId();
        if (!fansList.contains(userId)){
            UserData fansData=userDataRepository.findUserDataById(userId);
            List<Integer> followList=fansData.getFollowUserId();
            followList.add(followUserId);
            fansData.setFollowUserId(followList);
            userDataRepository.save(fansData);
            fansList.add(userId);
            followUserData.setFansId(fansList);
            userDataRepository.save(followUserData);
        }
    }
}
