package com.tao.digital.model;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Approval {

	@Id
	@GeneratedValue
	private int approvalId;
	private int productId;
	
	@Temporal(TemporalType.DATE)
	private Date requestedDate;
	
	private String endPoint;
	
	@Column(columnDefinition="tinyint(1) default 0")
	private boolean isDeleted;
}
