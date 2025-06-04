package org.blogapplication.repository;

import org.blogapplication.entity.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepo extends MongoRepository<BlogEntity, String> {
}
