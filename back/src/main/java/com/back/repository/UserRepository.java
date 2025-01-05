package com.back.repository;

import com.back.index.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ElasticsearchRepository<User,Integer> {
    User findUserByUsername(String username);
    User findUserByEmail(String email);
}
