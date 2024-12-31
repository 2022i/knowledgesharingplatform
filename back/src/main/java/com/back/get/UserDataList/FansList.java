package com.back.get.UserDataList;

import com.back.get.UserIdsList.FansIdsList;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FansList extends UsersList {
    @Resource
    private FansIdsList fansIdsList;
    @Override
    public List<UserData> getUserDataList(int userId){
        List<Integer> fansIds=fansIdsList.getIdsList(userId);
        return getUserDataList(fansIds);
    }
}
