package com.back.index;

import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UserDataTest {
    @Resource
    private UserDataRepository userDataRepository;
    @Test
    void initializeUserData() {
        List<UserData> userDatas = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            List<Integer> count=new ArrayList<>();
            count.add(i);
            count.add(i+1);
            UserData userData = new UserData();
            userData.setId(i);
            userData.setUsername("user"+i);
            userData.setSupportArticleId(count);
            userData.setOpposeArticleId(count);
            userData.setCollectArticleId(count);
            userData.setShareArticleId(count);
            userData.setWriteArticleId(count);
            userData.setFansId(count);
            userData.setFollowUserId(count);
            userDatas.add(userData);
            userDataRepository.saveAll(userDatas);
        }
    }
}