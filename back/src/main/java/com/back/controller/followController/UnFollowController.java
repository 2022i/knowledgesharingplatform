package com.back.controller.followController;

import com.back.dto.Response;
import com.back.follow.UnFollowTheme;
import com.back.follow.UnFollowUser;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
@CrossOrigin(origins = "*")
public class UnFollowController {
    @Resource
    private UnFollowTheme unFollowTheme;
    @Resource
    private UnFollowUser unFollowUser;

    @PutMapping("/unFollowTheme")
    @Description("用户点击取关某一主题")
    public Response followTheme(@RequestParam int userId, @RequestParam int themeId) {
        unFollowTheme.unFollowTheme(userId,themeId);
        return Response.successAddDataAndSentMessage();
    }

    @PutMapping("/unFollowUser")
    @Description("用户点击取关另一用户")
    public Response followUser(@RequestParam int currentUserId, @RequestParam int followUserId) {
        unFollowUser.unFollowUser(currentUserId,followUserId);
        return Response.successAddDataAndSentMessage();
    }
}
