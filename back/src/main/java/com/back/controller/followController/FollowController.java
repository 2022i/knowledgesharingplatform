package com.back.controller.followController;


import com.back.dto.Response;
import com.back.follow.FollowTheme;
import com.back.follow.FollowUser;
import com.back.sentSystemMessage.SentFollowMessage;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
@CrossOrigin(origins = "*")
public class FollowController {
    @Resource
    private FollowTheme followTheme;
    @Resource
    private FollowUser followUser;
    @Resource
    private SentFollowMessage sentFollowMessage;

    @PutMapping ("/followTheme")
    public Response followTheme(@RequestParam int userId, @RequestParam int themeId) {
        followTheme.followTheme(userId,themeId);
        return Response.successAddDataAndSentMessage();
    }

    @PutMapping ("/followUser")
    public Response followUser(@RequestParam int currentUserId, @RequestParam int followUserId) {
        followUser.followUser(currentUserId,followUserId);
        sentFollowMessage.sentMessage(currentUserId,followUserId);
        return Response.successAddDataAndSentMessage();
    }

}
