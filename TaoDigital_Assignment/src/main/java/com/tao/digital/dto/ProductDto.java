package com.tao.digital.dto;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	
	
	@JsonProperty(required = false)
	private int productId;
	
	@JsonProperty(required = true)
	private String productName;
	@JsonProperty(required = true)
	private float price;
	
	
	@JsonProperty(required = false)
	private Date postedDate;
	
	@JsonIgnore
	private Date updatedDate;
	
	@JsonIgnore
	private boolean IsDeleted;
	
	@JsonProperty(required = true)
	private String status;

}
