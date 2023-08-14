package com.tao.digital.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tao.digital.dao.ApprovalDao;
import com.tao.digital.dao.ProductDao;
import com.tao.digital.dto.ApprovalDto;
import com.tao.digital.dto.ProductDto;
import com.tao.digital.model.Approval;
import com.tao.digital.model.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ApprovalDao approvalDao;

	ModelMapper modelMapper = new ModelMapper();

	@Override
	public ProductDto addProduct(ProductDto productDto)
	{
		Product product = modelMapper.map(productDto, Product.class);
		product.setPostedDate(new Date(System.currentTimeMillis()));
		product.setUpdatedDate(new Date(System.currentTimeMillis()));
		//product.setDeleted(false);

		product = productDao.save(product);

		productDto = modelMapper.map(product, ProductDto.class);
		return  productDto;
	}

	public List<ProductDto> getActiveProducts()
	{
		List<Product> products =  productDao.findByStatusOrderByPostedDateDesc();
		List<ProductDto>productDtoList = Arrays.asList(modelMapper.map(products, ProductDto[].class));
		return productDtoList;
	}


	@Override
	public ProductDto updateProduct(ProductDto productDto)
	{

		Optional<Product> productOP = productDao.findById(productDto.getProductId());

		if(productOP.isPresent())
		{
			Product product = productOP.get();

			float currentPrice = product.getPrice();
			float newPrice = productDto.getPrice();

			float diff = newPrice - currentPrice;
			if(diff > (currentPrice/2))
			{
				productDto = sendProductForApproval(productDto);

			}	

			else
			{	
				product.setProductName(productDto.getProductName());
				product.setPrice(productDto.getPrice());
				product.setStatus(productDto.getStatus());

				product.setUpdatedDate(new Date(System.currentTimeMillis()));
				product = productDao.save(product);

				productDto = modelMapper.map(product, ProductDto.class);
			}

			return  productDto;

		}

		else
			return null;

	}

	@Transactional
	public ProductDto sendProductForApproval(ProductDto productDto)
	{

		Product product = modelMapper.map(productDto, Product.class);
		product.setPostedDate(new Date(System.currentTimeMillis()));
		product.setUpdatedDate(new Date(System.currentTimeMillis()));
		product.setStatus("pending");
		product = productDao.save(product);
		productDto = modelMapper.map(product, ProductDto.class);

		Approval approval  = new Approval();
		//approval.setApprovalId(0);
		approval.setProductId(productDto.getProductId());
		approval.setRequestedDate(productDto.getPostedDate());
		approval.setEndPoint("/api/products");

		approvalDao.save(approval);

		return productDto;

	}


	@Transactional
	public ProductDto updateProductForApproval(ProductDto productDto)
	{

		Optional<Product> productOP = productDao.findById(productDto.getProductId());

		if(productOP.isPresent())
		{

			Product product = productOP.get();
			product.setProductName(productDto.getProductName());
			product.setPrice(productDto.getPrice());
			product.setStatus(productDto.getStatus());

			product.setUpdatedDate(new Date(System.currentTimeMillis()));
			product = productDao.save(product);

			productDto = modelMapper.map(product, ProductDto.class);
			return  productDto;
		}

		Product product = modelMapper.map(productDto, Product.class);
		product.setPostedDate(new Date(System.currentTimeMillis()));
		product.setUpdatedDate(new Date(System.currentTimeMillis()));
		product.setStatus("pending");
		product = productDao.save(product);
		productDto = modelMapper.map(product, ProductDto.class);

		Approval approval  = new Approval();
		//approval.setApprovalId(0);
		approval.setProductId(productDto.getProductId());
		approval.setRequestedDate(productDto.getPostedDate());
		approval.setEndPoint("/api/products");

		approvalDao.save(approval);

		return productDto;

	}

	public List<ApprovalDto> getApprovalList()
	{
		List<Approval> approvals =  approvalDao.findAllOrderByRequestedDateDesc();
		List<ApprovalDto>approvalList = Arrays.asList(modelMapper.map(approvals, ApprovalDto[].class));
		return approvalList;
	}

	@Transactional
	public boolean removeProductRequestFromQueue(int approvalId)
	{
		Optional<Approval> approvalOptional =  approvalDao.findById(approvalId);
		if(approvalOptional.isPresent())
		{
			approvalDao.deleteRequestFromQueue(approvalId);	
			return true;
		}
		else
			return false;
	}


	@Transactional
	public boolean approveProductRequestFromQueue(int approvalId)
	{
		Optional<Approval> approvalOptional =  approvalDao.findById(approvalId);
		if(approvalOptional.isPresent())
		{

			approvalDao.deleteRequestFromQueue(approvalId);	
			Optional<Product> productOp =	productDao.findById(approvalOptional.get().getProductId());
			if(productOp.isPresent())
			{
				Product product  = productOp.get();
				product.setStatus("active");
			}	  


			return true;
		}
		else
			return false;
	}


	@PersistenceContext
	private EntityManager entityManager;

	public List<ProductDto> searchProducts(String productName, Float minPrice,  Float maxPrice, Date minPostedDate,Date maxPostedDate)
	{

		CriteriaBuilder  cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = cb.createQuery(Product.class);      
		Root<Product> productRoot = criteriaQuery.from(Product.class);

		criteriaQuery.select(productRoot);

		Root<Product> root = criteriaQuery.from(Product.class);

		List<Predicate> conditionsList = new ArrayList<Predicate>();
        
		if(productName !=null)
		{	
			Predicate pName = cb.like(root.get("productName"), "%" + productName + "%");
			conditionsList.add(pName);
		}	

		if(minPostedDate!=null)
		{	
			Predicate onStart = cb.greaterThanOrEqualTo(root.get("postedDate"), minPostedDate);
			conditionsList.add(onStart);
		}
		
		if(maxPostedDate!=null)
		{
		Predicate onEnd = cb.lessThanOrEqualTo(root.get("postedDate"), maxPostedDate);
		conditionsList.add(onEnd);
		}
		
		if( minPrice !=null && minPrice > 0)
		{	
			Predicate onStart = cb.greaterThanOrEqualTo(root.get("price"), minPrice);
			conditionsList.add(onStart);
		}
		
		if(maxPrice!=null &&  maxPrice>0)
		{
		Predicate onEnd = cb.lessThanOrEqualTo(root.get("price"), maxPrice);
		conditionsList.add(onEnd);
		}

		Predicate active = cb.equal(root.get("status"), "active");
     	conditionsList.add(active);
		
		criteriaQuery.select(root).where(conditionsList.toArray(new Predicate[]{}));

		TypedQuery<Product> query = entityManager.createQuery(criteriaQuery);
		List<Product> products = query.getResultList();

		List<ProductDto>productDtoList = Arrays.asList(modelMapper.map(products, ProductDto[].class));
		
		return productDtoList;

	}
}	
