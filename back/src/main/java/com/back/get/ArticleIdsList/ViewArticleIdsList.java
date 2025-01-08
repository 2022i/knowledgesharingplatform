package com.back.get.ArticleIdsList;

import com.back.repository.ViewDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ViewArticleIdsList extends ArticleIdsList {
    @Resource
    private ViewDataRepository viewDataRepository;
    @Override
    public List<Integer> getIdsList(int userId) {
        List<Integer> viewDataIds= userDataRepository.findUserDataById(userId).getViewDataIds();
        List<Integer> viewArticleIds=new ArrayList<>();
        for(Integer viewDataId:viewDataIds){
            viewArticleIds.add(viewDataRepository.findViewDataById(viewDataId).getArticleId());
        }
        return viewArticleIds;
    }
}
