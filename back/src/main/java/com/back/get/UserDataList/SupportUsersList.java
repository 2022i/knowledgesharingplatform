package com.back.get.UserDataList;

import com.back.get.UserIdsList.SupportUserIdsList;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupportUsersList extends UsersList{
    @Resource
    private SupportUserIdsList supportUserIdsList;
    @Override
    public List<UserData> getUserDataList(int articleId){
        List<Integer> supportUserIds =supportUserIdsList.getIdsList(articleId);
        return getUserDataList(supportUserIds);
    }
}
