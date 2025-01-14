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

    public void unFollowUser(int currentUserId, int followUserId){
        List<Integer> followList = userDataRepository.findUserDataById(currentUserId).getFollowUserId();
        if (followList.contains(followUserId)) {
            followList.remove((Integer) followUserId);
            UserData currentUserData=userDataRepository.findUserDataById(currentUserId);
            currentUserData.setFollowUserId(followList);
            userDataRepository.save(currentUserData);
            List<Integer> fansList = userDataRepository.findUserDataById(followUserId).getFansId();
            fansList.remove((Integer) currentUserId);
            UserData followUserData=userDataRepository.findUserDataById(followUserId);
            followUserData.setFansId(fansList);
            userDataRepository.save(followUserData);
        }
    }
}

