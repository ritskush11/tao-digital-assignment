package com.tao.digital.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDto {

	private int approvalId;
	private int productId;
	private Date requestedDate;
	
	@JsonIgnore
	private String endPoint;
}
