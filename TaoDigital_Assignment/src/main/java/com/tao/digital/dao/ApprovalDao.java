package com.tao.digital.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tao.digital.model.Approval;

@Repository
public interface ApprovalDao extends CrudRepository<Approval, Integer>{
	
	@Query("SELECT a from Approval a where a.isDeleted=false order by a.requestedDate desc")
	List<Approval> findAllOrderByRequestedDateDesc();
	
	@Modifying
	@Query(value = "UPDATE Approval a set a.isDeleted=true where a.approvalId =:approvalId ")
    void deleteRequestFromQueue(@Param("approvalId") int approvalId);
		
}