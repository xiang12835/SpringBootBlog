package com.java456.entity.blog;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java456.entity.base.CustomDateTimeSerializer;
import com.java456.entity.base.Role;
import com.java456.entity.base.User;

@Entity
@Table(name="t_blog")
public class Blog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotEmpty(message="标题不能为空！")
	@Column(length=200)
	private String title; // 博客类型名称
	@Lob
    @Basic(fetch=FetchType.LAZY)
	private String content; //html 代码  long text
	@Temporal(TemporalType.TIMESTAMP) 
	private Date createDateTime;//创建时间
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updateDateTime;//修改时间
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;//哪个用户发布的
	@Column(length=10)
	private Integer state;//0未审校   1已审核
	@Column(length=220)
	private  String imageUrl;//封面
	@Column(length=10)
	private Integer useIt;//0不首页  1首页    是否首页显示 
	@Lob
    @Basic(fetch=FetchType.LAZY)
	private String contentNoTag; //html 代码  long text
	
	
	@ManyToOne
	@JoinColumn(name="blogTypeId")
	private BlogType blogType; //类型
	@Column(length=10)
	private Integer clickHit;//查看次数
	@Column(length=10)
	private Integer replyHit; //回复次数
	@Column(length=200)
	private  String summary;//摘要
	
	
	
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Integer getClickHit() {
		return clickHit;
	}
	public void setClickHit(Integer clickHit) {
		this.clickHit = clickHit;
	}
	public Integer getReplyHit() {
		return replyHit;
	}
	public void setReplyHit(Integer replyHit) {
		this.replyHit = replyHit;
	}
	public String getContentNoTag() {
		return contentNoTag;
	}
	public void setContentNoTag(String contentNoTag) {
		this.contentNoTag = contentNoTag;
	}
	public BlogType getBlogType() {
		return blogType;
	}
	public void setBlogType(BlogType blogType) {
		this.blogType = blogType;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getUpdateDateTime() {
		return updateDateTime;
	}
	public void setUpdateDateTime(Date updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getUseIt() {
		return useIt;
	}
	public void setUseIt(Integer useIt) {
		this.useIt = useIt;
	}
	
	
	
	
	
}
