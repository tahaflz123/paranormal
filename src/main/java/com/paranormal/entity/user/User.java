package com.paranormal.entity.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.paranormal.entity.BaseEntity;

@Entity
@Table(name = "user")
public class User extends BaseEntity{
	
	private String username;
	
	private String email;
	
	private String password;

}
