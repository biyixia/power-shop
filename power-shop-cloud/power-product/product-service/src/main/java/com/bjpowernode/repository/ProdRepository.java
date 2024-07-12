package com.bjpowernode.repository;

import com.bjpowernode.domain.ProdEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdRepository extends ElasticsearchRepository<ProdEs, Long> {
}
