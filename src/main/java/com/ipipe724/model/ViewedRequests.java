package com.ipipe724.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "viewedrequests")
public class ViewedRequests {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
	
	@ManyToOne(cascade = CascadeType.ALL, targetEntity=User.class)
	@JoinColumn(name = "user_id")
	private User user = new User();
	
	@ManyToOne(cascade = CascadeType.ALL, targetEntity=RequestRepair.class)
	@JoinColumn(name = "request_id")
	private RequestRepair request = new RequestRepair();
	

	@Column(name = "status")
	private String status;
	
	@Column(name = "days")
	private String days;
	
	@Column(name = "price")
	private String price;
	
	@Column(name = "comment")
	private String comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public RequestRepair getRequest() {
		return request;
	}

	public void setRequest(RequestRepair request) {
		this.request = request;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
