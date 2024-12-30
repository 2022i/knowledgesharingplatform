package com.back.get.UserIdsList;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectUserIdsList extends UserIdsList{
    @Override
    public List<Integer> getIdsList(int articleId){
        return articleRepository.findArticleById(articleId).getCollectionUserIds();
    }
}
