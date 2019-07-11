package com.java456.entity.base;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * 网站配置实体
 */
@Entity
@Table(name = "t_config")
public class Config {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length=50)
	private String webName; // 网站名称
	@Column(length=50)
	private String stationName; // 机构名称
	@Column(length=20)
	private String cssVersion;
	
	
	@Column(length=200)
	private String indexTitle;
	@Column(length=200)
	private String keywords;
	@Column(length=200)
	private String description;
	
	@Column(length=10)
	private Integer indexPageSize;//首页显示最近几条数据
	@Column(length=10)
	private Integer allpageSize;//其余分页 显示几条数据
	
	
	
	public Integer getAllpageSize() {
		return allpageSize;
	}

	public void setAllpageSize(Integer allpageSize) {
		this.allpageSize = allpageSize;
	}

	public Integer getIndexPageSize() {
		return indexPageSize;
	}

	public void setIndexPageSize(Integer indexPageSize) {
		this.indexPageSize = indexPageSize;
	}

	public String getIndexTitle() {
		return indexTitle;
	}

	public void setIndexTitle(String indexTitle) {
		this.indexTitle = indexTitle;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWebName() {
		return webName;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}


	public String getCssVersion() {
		return cssVersion;
	}

	public void setCssVersion(String cssVersion) {
		this.cssVersion = cssVersion;
	}
	
	
	
	
	
}
