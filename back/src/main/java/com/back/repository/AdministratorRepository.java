package com.back.repository;

import com.back.index.Administrator;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends ElasticsearchRepository<Administrator, Integer> {
    Administrator findAdministratorByUsername(String username);
}
