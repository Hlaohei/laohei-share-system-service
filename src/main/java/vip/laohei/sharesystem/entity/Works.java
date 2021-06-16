package vip.laohei.sharesystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Works 实体
 * 
 * @author laohei
 *
 */
@Entity
@Table(name = "tb_works")
public class Works {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "works_category_id")
	private String worksCategoryId;

	@Column(name = "summary")
	private String summary;

	@Column(name = "url")
	private String url;

	@Column(name = "cover")
	private String cover;

	@Column(name = "state")
	private String state = "1";

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "update_time")
	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorksCategoryId() {
		return worksCategoryId;
	}

	public void setWorksCategoryId(String worksCategoryId) {
		this.worksCategoryId = worksCategoryId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "\n Works ==> { " //
				+ "\n     id: " + id //
				+ ", \n     name: " + name //
				+ ", \n     worksCategoryId: " + worksCategoryId //
				+ ", \n     summary: " + summary //
				+ ", \n     url: " + url //
				+ ", \n     cover: " + cover //
				+ ", \n     state: " + state //
				+ ", \n     createTime: " + createTime //
				+ ", \n     updateTime: " + updateTime //
				+ "\n }";
	}

}
