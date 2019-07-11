package com.java456.entity.blog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java456.entity.base.CustomDateTimeSerializer;

@Entity
@Table(name="t_lunbo")
public class LunBo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length=50)
	private String name; // 图片名称
	@Column(length=200)
	private String url; // 地址
	@Column(length=200)
	private String imageUrl; // 图片地址
	@Column(length = 5)
	private Integer useIt;// 0不显示 1显示
	
	@NotNull(message = "排序号不能为空！")
	@Column(length = 10)
	private Integer orderNo;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDateTime;// 创建时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDateTime;// 修改时间

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
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
	
	
	
	
}
