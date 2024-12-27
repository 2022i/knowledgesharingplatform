package com.back.index;

import com.back.repository.UserDataRepository;
import com.back.repository.UserRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserDataTest {
    @Resource
    private UserDataRepository userDataRepository;
    @Test
    void initializeUserData() {
        List<UserData> userDatas = new ArrayList<>();
        List<Integer> count=new ArrayList<>();
        count.add(1);
        count.add(2);
        for (int i = 1; i <= 100; i++) {
            UserData userData = new UserData();
            userData.setId(i);
            userData.setSupportArticleId(count);
            userData.setOpposeArticleId(count);
            userData.setCollectArticleId(count);
            userData.setShareArticleId(count);
            userData.setWriteArticleId(count);
            userDatas.add(userData);
            userDataRepository.saveAll(userDatas);
        }
    }
}