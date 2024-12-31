package com.back.get.ArticleIdsList;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpposeArticleIdsList extends ArticleIdsList {
    @Override
    public List<Integer> getIdsList(int userId) {
        return userDataRepository.findUserDataById(userId).getOpposeArticleIds();
    }
}
