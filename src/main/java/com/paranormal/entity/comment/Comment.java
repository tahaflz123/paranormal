package com.paranormal.entity.comment;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.paranormal.entity.BaseEntity;
import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment extends BaseEntity{

	@Lob
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	private User sender;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
	
	
}
