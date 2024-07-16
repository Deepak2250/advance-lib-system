package com.ShelfSpace.ShelfSpace.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "forgot_password")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder  // For making the Builder Design Pattern
@ToString
public class ForgotPassword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fp_id" , nullable = false)
	private Long forgotpasswordId;
	
	@Column(name = "otp" , nullable = false)
	private Integer otp;
	
	@Column(name = "otp_Expire_Date" , nullable = false)
	private java.util.Date otpExpireDate;
	
	@OneToOne
	@JoinColumn(name = "user_email" , nullable = false)
	private User user;
}
