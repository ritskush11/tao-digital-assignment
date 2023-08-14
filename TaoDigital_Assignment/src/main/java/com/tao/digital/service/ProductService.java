package com.tao.digital.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tao.digital.dto.ApprovalDto;
import com.tao.digital.dto.ProductDto;

@Service
public interface ProductService {
	
	public List<ApprovalDto> getApprovalList();

	ProductDto addProduct(ProductDto productDto);
	
	List<ProductDto> getActiveProducts();
	
	ProductDto updateProduct(ProductDto productDto);
	
	ProductDto sendProductForApproval(ProductDto productDto);
	
	boolean removeProductRequestFromQueue(int approvalId);
	
	boolean approveProductRequestFromQueue(int approvalId);
	
	List<ProductDto> searchProducts(String productName, Float minPrice,  Float maxPrice, Date minPostedDate,Date maxPostedDate);
	
}
