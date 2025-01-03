//package com.back.get.UserDataList;
//
//import com.back.get.UserIdsList.RootCommentUserIdsList;
//import com.back.index.UserData;
//import jakarta.annotation.Resource;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class RootCommentUsersList extends  UsersList {
//    @Resource
//    private RootCommentUserIdsList rootCommentUserIdsList;
//    @Override
//    public List<UserData> getUserDataList(int articleId){
//        List<Integer> commentUserIds = rootCommentUserIdsList.getIdsList(articleId);
//        return getUserDataList(commentUserIds);
//    }
//}
