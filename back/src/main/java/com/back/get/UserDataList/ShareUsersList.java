package com.back.get.UserDataList;

import com.back.get.UserIdsList.ShareUserIdsList;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShareUsersList extends UsersList {
    @Resource
    private ShareUserIdsList shareUserIdsList;
    @Override
    public List<UserData> getUserDataList(int userId){
        List<Integer> shareUserIds =shareUserIdsList.getIdsList(userId);
        List<UserData> userData = new ArrayList<>();
        for(Integer id : shareUserIds){
            userData.add(userDataRepository.findUserDataById(id));
        }
        return userData;
    }
}
