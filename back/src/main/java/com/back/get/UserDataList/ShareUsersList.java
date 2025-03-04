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
    public List<UserData> getUserDataList(int articleId){
        List<Integer> shareUserIds =shareUserIdsList.getIdsList(articleId);
        return getUserDataList(shareUserIds);
    }
}
