package com.back.get.UserDataList;

import com.back.get.UserIdsList.CollectUserIdsList;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectUsersList extends UsersList{
    @Resource
    private CollectUserIdsList collectUserIdsList;
    @Override
    public List<UserData> getUserDataList(int articleId){
        List<Integer> collectUserIds = collectUserIdsList.getIdsList(articleId);
        return getUserDataList(collectUserIds);
    }
}
