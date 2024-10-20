package com.minsait.com.oum.mdc.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minsait.mdc.data.model.CallGroup;

public interface CallGroupDomain extends MongoRepository<CallGroup, String> {

}
