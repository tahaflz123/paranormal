package com.paranormal.entity.post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.paranormal.entity.BaseEntity;
import com.paranormal.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity{

	@Column(name = "header", unique = true)
	private String header;
	
	@Lob
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
