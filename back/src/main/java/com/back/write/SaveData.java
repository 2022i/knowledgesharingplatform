package com.back.write;

import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SaveData {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private  UserDataRepository userDataRepository;
    public void saveData(Article article, int authorId){
        articleRepository.save(article);
        UserData userData = userDataRepository.findUserDataById(authorId);
        List<Integer> writeArticleIds= userData.getWriteArticleIds();
        writeArticleIds.add(article.getId());
        userData.setWriteArticleIds(writeArticleIds);
        userDataRepository.save(userData);
    }
}
