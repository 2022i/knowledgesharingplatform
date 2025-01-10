package com.back.controller.personalCenterDataController;

import com.back.dto.article.RenderedViewArticle;
import com.back.get.ViewHistory;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class ViewHistoryController {
    @Resource
    private ViewHistory viewHistory;
    @GetMapping("/getViewHistory")
    public List<RenderedViewArticle> getViewHistory(@RequestParam int userId){
        return viewHistory.getViewArticle(userId);
    }
}
