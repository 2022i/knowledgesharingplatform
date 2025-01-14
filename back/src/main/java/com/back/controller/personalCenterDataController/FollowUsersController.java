package com.back.controller.personalCenterDataController;

import com.back.dto.ArticleAuthor;
import com.back.get.UserDataList.FollowUsersList;
import com.back.get.preparation.UserRenderingPreparation;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class FollowUsersController {
    @Resource
    private FollowUsersList followUsersList;
    @Resource
    private UserRenderingPreparation userRenderingPreparation;
    @Description("个人中心接口，获取用户关注的用户详细信息列表，返回用户数据用于渲染")
    @GetMapping("/getFollowUsers")
    public List<ArticleAuthor> getFollowUsers(@RequestParam int userId) {
        return userRenderingPreparation.getArticleAuthors(followUsersList.getUserDataList(userId),userId);
    }
}
