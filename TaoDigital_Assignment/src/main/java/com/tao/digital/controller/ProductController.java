package com.tao.digital.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tao.digital.config.Iconstants;
import com.tao.digital.config.Response;
import com.tao.digital.dto.ApprovalDto;
import com.tao.digital.dto.ProductDto;
import com.tao.digital.service.ProductService;
import com.tao.digital.util.DateFormatUtil;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired(required=true)
	private ProductService productService;

	@PostMapping("/products")
	public Response addProduct(@RequestBody ProductDto productDto)
	{
		Response response = new  Response();
		if(productDto.getPrice()<=5000.00f)
		{	
			productDto = productService.addProduct(productDto);
			response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.PRODUCT_ADD,productDto);	
			return response;
		}

		else if (productDto.getPrice()>5000.00f && productDto.getPrice()<10001.00f)		
		{
		    productDto =  productService.sendProductForApproval(productDto);
			return	response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.PRODUCT_APPROVAL_PENDING,productDto);	

		}
		else
			return response = new Response(Iconstants.FAILED_CODE, Iconstants.FAILED, Iconstants.PRODUCT_MAX_LIMIT,null);
	}

	@GetMapping("/get-products")
	public Response getProducts()
	{

		List<ProductDto> products = productService.getActiveProducts();

		Response response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.PRODUCT_LIST,products);
		return response;
	}



	@PutMapping("/products/{productId}")
	public Response updateProduct(@RequestBody ProductDto productDto, @PathVariable("productId") int productId)
	{
		//Response response = new Response();
		productDto.setProductId(productId);
		productDto = productService.updateProduct(productDto);

		if(productDto!=null)
		{
			Response	response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.PRODUCT_ADD,productDto);
			return response;
		}

		else
		{	
		  Response	response = new Response(Iconstants.FAILED_CODE, Iconstants.FAILED, Iconstants.PRODUCT_UPDATE_FAILED,null);
		   return response;
		}
	}

	
	@GetMapping("/products/approval-queue")
	public Response getApprovalQueue()
	{

		List<ApprovalDto> pendingApproval = productService.getApprovalList();
		if(pendingApproval !=null && pendingApproval.size()>0)
		{	
		Response response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.APPROVAL_LIST,pendingApproval);
		return response;
		}
		
		else
		{	
		Response response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.RECORD_NOT_FOUND,null);
		return response;
		}
		
	}
	
	@PutMapping("/products/approval-queue/{approvalId}/reject")
	public Response rejectRequestFromApprovalQueue(@PathVariable("approvalId") int approvalId)
	{
        boolean flag =  productService.removeProductRequestFromQueue(approvalId);
		if(flag)
		{	
		Response response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.REQUEST_REJECTED,null);
		return response;
		}
		
		else
		{	
		Response response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.RECORD_NOT_FOUND,null);
		return response;
		}
		
	}

	@PutMapping("/products/approval-queue/{approvalId}/approve")
	public Response approveRequestFromApprovalQueue(@PathVariable("approvalId") int approvalId)
	{
        boolean flag =  productService.approveProductRequestFromQueue(approvalId);
		if(flag)
		{	
		Response response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.REQUEST_APPROVED,null);
		return response;
		}
		
		else
		{	
		Response response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.RECORD_NOT_FOUND,null);
		return response;
		}
		
	}
	
	
	@GetMapping("/products/search")
	public Response searchProducts(@RequestParam(name = "productName", required = false) String productName,
			@RequestParam(name = "minPrice", required = false) Float minPrice, @RequestParam(name = "maxPrice", required = false) Float maxPrice,
			@RequestParam(name = "minPostedDate", required = false) String minPostedDate,
			@RequestParam(name = "maxPostedDate", required = false) String maxPostedDate)
	{

		Date fromDate = null;
		Date toDate = null;
		if(minPostedDate !=null)
		{	
		 fromDate = DateFormatUtil.formatStringToDate(minPostedDate);
		}
		if(maxPostedDate !=null)
		{
			toDate = DateFormatUtil.formatStringToDate(maxPostedDate);
		}
		
		List<ProductDto> products = productService.searchProducts(productName, minPrice, maxPrice, fromDate, toDate);

		Response response = new Response(Iconstants.SUCCESS_CODE, Iconstants.SUCCESS, Iconstants.PRODUCT_LIST,products);
		return response;
	}

	
	
}
