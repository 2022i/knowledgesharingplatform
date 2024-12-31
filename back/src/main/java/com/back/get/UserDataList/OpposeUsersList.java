package com.back.get.UserDataList;

import com.back.get.UserIdsList.OpposeUserIdsList;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpposeUsersList extends UsersList {
    @Resource
    OpposeUserIdsList opposeUserIdsList;
    @Override
    public List<UserData> getUserDataList(int articleId){
        List<Integer> opposeUserIds =opposeUserIdsList.getIdsList(articleId);
        return getUserDataList(opposeUserIds);
    }
}
