package vip.laohei.sharesystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ShareNoContent 实体
 * 
 * @author laohei
 *
 */
@Entity
@Table(name = "tb_share")
public class ShareNoContent {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "title")
	private String title;

	@Column(name = "share_category_id")
	private String shareCategoryId;

	@Column(name = "summary")
	private String summary;

	@Column(name = "label")
	private String label;

	@Column(name = "cover")
	private String cover;

	@Column(name = "state")
	private String state = "1";

	@Column(name = "top")
	private String top = "0";

	@Column(name = "view_count")
	private long viewCount = 0L;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShareCategoryId() {
		return shareCategoryId;
	}

	public void setShareCategoryId(String shareCategoryId) {
		this.shareCategoryId = shareCategoryId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public long getViewCount() {
		return viewCount;
	}

	public void setViewCount(long viewCount) {
		this.viewCount = viewCount;
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
		return "\n Share ==> { " //
				+ "\n     id: " + id //
				+ ", \n     title: " + title //
				+ ", \n     shareCategoryId: " + shareCategoryId//
				+ ", \n     summary: " + summary //
				+ ", \n     label: " + label //
				+ ", \n     cover: " + cover //
				+ ", \n     state: " + state //
				+ ", \n     top: " + top //
				+ ", \n     viewCount: " + viewCount //
				+ ", \n     createTime: " + createTime //
				+ ", \n     updateTime: " + updateTime //
				+ "\n }";
	}

}
