package com.minsait.com.oum.mdc.repository;

import com.minsait.mdc.data.model.Call;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallDomain extends MongoRepository<Call, String> {

	public Page<Call> findByIdGroup(Pageable pageable, String idGroup);

	public List<Call> findByIdGroup(String idGroup);

	public Long countByIdGroup(String idGroup);

	public Page<Call> findByIdGroupAndDatetimeBetween(Pageable pageable, String idGroup, Long from, Long to);

	@Query("{'name':?0, 'status': ?1, 'tasks' : { $elemMatch: { 'finishTime': {$gte: ?2}}}}")
	public List<Call> getByStatusAndFinishtimeGreaterThan(final String name, final String status, final Long from);

	@Query("{'status': ?0, 'tasks' : { $elemMatch: { 'finishTime': {$gte: ?1}}}}")
	public List<Call> getByStatusAndFinishtimeGreaterThan(final String status, final Long from);

//	@Query("{'datetime':{ $gte: ?0, $lte: ?1 }, 'idGroup':?2}")
//	public List<Call> findestoy(Long from, Long to, String idGroup);

	@Query("{'idDC': ?0}")
	public Call findByIdDC(String idDC);
}