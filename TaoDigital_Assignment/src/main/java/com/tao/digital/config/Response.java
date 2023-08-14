package com.tao.digital.config;

import java.util.Date;

import com.tao.digital.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
	
	private String code;
	private String status;
	private String message;
	private Object payload;
	

}
