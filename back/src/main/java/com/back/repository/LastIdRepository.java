package com.back.repository;

import com.back.index.LastId;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LastIdRepository extends ElasticsearchRepository<LastId,Integer> {
    LastId findLastIdById(int id);
}
