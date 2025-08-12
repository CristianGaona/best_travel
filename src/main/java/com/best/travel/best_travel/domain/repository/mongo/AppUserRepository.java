package com.best.travel.best_travel.domain.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.best.travel.best_travel.domain.entity.document.AppUserDocument;

@Repository
public interface AppUserRepository extends MongoRepository<AppUserDocument, String> {

	Optional<AppUserDocument> findByUsername(String username);
}
