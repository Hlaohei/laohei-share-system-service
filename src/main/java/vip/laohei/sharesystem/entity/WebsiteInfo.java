package vip.laohei.sharesystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WebsiteInfo 实体
 * 
 * @author laohei
 *
 */
@Entity
@Table(name = "tb_website_info")
public class WebsiteInfo {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "keywords")
	private String keywords;

	@Column(name = "view_count")
	private long viewCount = 0L;

	@Column(name = "beian_icp")
	private String beianIcp;

	@Column(name = "beian_icp_url")
	private String beianIcpUrl;

	@Column(name = "beian_gongan")
	private String beianGongan;

	@Column(name = "beian_gongan_url")
	private String beianGonganUrl;

	@Column(name = "notice")
	private String notice;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public long getViewCount() {
		return viewCount;
	}

	public void setViewCount(long viewCount) {
		this.viewCount = viewCount;
	}

	public String getBeianIcp() {
		return beianIcp;
	}

	public void setBeianIcp(String beianIcp) {
		this.beianIcp = beianIcp;
	}

	public String getBeianIcpUrl() {
		return beianIcpUrl;
	}

	public void setBeianIcpUrl(String beianIcpUrl) {
		this.beianIcpUrl = beianIcpUrl;
	}

	public String getBeianGongan() {
		return beianGongan;
	}

	public void setBeianGongan(String beianGongan) {
		this.beianGongan = beianGongan;
	}

	public String getBeianGonganUrl() {
		return beianGonganUrl;
	}

	public void setBeianGonganUrl(String beianGonganUrl) {
		this.beianGonganUrl = beianGonganUrl;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
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
		return "\n WebsiteInfo ==> { " //
				+ "\n     id: " + id //
				+ ", \n     title: " + title //
				+ ", \n     description: " + description //
				+ ", \n     keywords: " + keywords //
				+ ", \n     viewCount: " + viewCount //
				+ ", \n     beianIcp: " + beianIcp //
				+ ", \n     beianIcpUrl: " + beianIcpUrl //
				+ ", \n     beianGongan: " + beianGongan //
				+ ", \n     beianGonganUrl: " + beianGonganUrl //
				+ ", \n     notice: " + notice //
				+ ", \n     createTime: " + createTime //
				+ ", \n     updateTime: " + updateTime //
				+ "\n }";
	}

}
