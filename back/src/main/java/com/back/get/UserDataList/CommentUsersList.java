//package com.back.get.UserDataList;
//
//import com.back.get.UserIdsList.CommentUserIdsList;
//import com.back.index.UserData;
//import jakarta.annotation.Resource;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class CommentUsersList extends  UsersList {
//    @Resource
//    private CommentUserIdsList commentUserIdsList;
//    @Override
//    public List<UserData> getUserDataList(int articleId){
//        List<Integer> commentUserIds = commentUserIdsList.getCommentsList(articleId);
//        return getUserDataList(commentUserIds);
//    }
//}
