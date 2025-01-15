package com.back.controller;

import com.back.dto.ArticleAuthor;
import com.back.get.preparation.UserRenderingPreparation;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/search")
public class UserDataSearchController {
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    private UserRenderingPreparation userRenderingPreparation;
    @GetMapping("/userData")
    public List<ArticleAuthor> searchUserData(@RequestParam String name, @RequestParam int userId) {
        return userRenderingPreparation.getArticleAuthors(userDataRepository.findUserDataByUsername(name),userId);
    }
}
