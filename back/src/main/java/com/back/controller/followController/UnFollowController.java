package com.back.controller.followController;


import com.back.dto.Response;
import com.back.follow.FollowTheme;
import com.back.follow.FollowUser;
import com.back.follow.UnFollowTheme;
import com.back.follow.UnFollowUser;
import com.back.sentSystemMessage.SentFollowMessage;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
@CrossOrigin(origins = "*")
public class UnFollowController {
    @Resource
    private UnFollowTheme unFollowTheme;
    @Resource
    private UnFollowUser unFollowUser;
    @Resource
    private SentFollowMessage sentFollowMessage;

    @GetMapping("/unFollowTheme")
    public Response followTheme(@RequestParam int userId, @RequestParam int themeId) {
        unFollowTheme.unFollowTheme(userId,themeId);
        return Response.successAddDataAndSentMessage();
    }

    @GetMapping("/unFollowUser")
    public Response followUser(@RequestParam int userId, @RequestParam int followUserId) {
        unFollowUser.unFollowUser(userId,followUserId);
        return Response.successAddDataAndSentMessage();
    }
}
