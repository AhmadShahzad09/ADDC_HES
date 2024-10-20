package com.minsait.oum.mdc.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.minsait.oum.mdc.data.model.CallGroup;


@Repository
public interface CallGroupRepository extends MongoRepository<CallGroup, String>, CustomCallGroupRepository {

}
