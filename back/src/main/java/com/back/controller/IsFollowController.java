package com.back.controller;

import com.back.get.UserIdsList.FollowUserIdsList;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("")
public class IsFollowController {
    @Resource
    private FollowUserIdsList followUserIdsList;
    @GetMapping("/isFollow")
    public boolean isFollow(@RequestParam int currentUserId, @RequestParam int followUserId) {
        return followUserIdsList.getIdsList(currentUserId).contains(followUserId);
    }
}
