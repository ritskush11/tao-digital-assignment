package com.tao.digital.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tao.digital.model.Product;

@Repository
public interface ProductDao extends CrudRepository<Product, Integer>  {
	
	
	@Query("SELECT e from Product e where e.status ='active' order by e.postedDate desc")
	List<Product> findByStatusOrderByPostedDateDesc();
	
	
	
    
	

}
