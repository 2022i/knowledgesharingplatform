package com.back.get.UserDataList;

import com.back.get.UserIdsList.FollowUserIdsList;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowUsersList extends UsersList {
    @Resource
    private FollowUserIdsList followUserIdsList;
    @Override
    public List<UserData> getUserDataList(int userId){
        List<Integer> followUserIds =followUserIdsList.getIdsList(userId);
        return getUserDataList(followUserIds);
    }
}
