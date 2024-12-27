package com.back.repository;

import com.back.index.Article;
import com.back.index.Theme;
import com.back.index.UserData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDataRepository extends ElasticsearchRepository<UserData,Integer > {
    UserData findUserDataById(int id);
}
