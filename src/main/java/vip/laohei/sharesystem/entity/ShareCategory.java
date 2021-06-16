package vip.laohei.sharesystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ShareCategory 实体
 * 
 * @author laohei
 *
 */
@Entity
@Table(name = "tb_share_category")
public class ShareCategory {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "pinyin")
	private String pinyin;

	@Column(name = "description")
	private String description;

	@Column(name = "state")
	private String state = "1";

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "update_time")
	private Date updateTime;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "\n ShareCategory ==> { " //
				+ "\n     id: " + id //
				+ ", \n     name: " + name //
				+ ", \n     pinyin: " + pinyin //
				+ ", \n     description: " + description //
				+ ", \n     state: " + state //
				+ ", \n     createTime: " + createTime //
				+ ", \n     updateTime: " + updateTime //
				+ "\n }";
	}

}
