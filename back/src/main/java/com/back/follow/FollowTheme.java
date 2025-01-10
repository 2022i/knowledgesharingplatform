package com.back.follow;

import com.back.index.UserData;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowTheme {
    @Resource
    private UserDataRepository userDataRepository;
    public void followTheme(int userId,int themeId){
        List<Integer> subscriptionThemeId = userDataRepository.findUserDataById(userId).getSubscriptionThemeId();
        if (subscriptionThemeId.contains(themeId)) {
            throw new IllegalArgumentException("Theme is already followed");
        }
        subscriptionThemeId.add(themeId);
        UserData userData = userDataRepository.findUserDataById(userId);
        userData.setSubscriptionThemeId(subscriptionThemeId);
        userDataRepository.save(userData);
    }
}
