package com.back.controller.personalCenterDataController;

import com.back.dto.article.RenderedViewArticle;
import com.back.get.ViewHistory;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import javax.management.DescriptorKey;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class ViewHistoryController {
    @Resource
    private ViewHistory viewHistory;
    @DescriptorKey("个人中心接口，获取用户浏览历史记录，返回文章数据用于渲染")
    @GetMapping("/getViewHistory")
    public List<RenderedViewArticle> getViewHistory(@RequestParam int userId){
        return viewHistory.getViewArticle(userId);
    }
}
