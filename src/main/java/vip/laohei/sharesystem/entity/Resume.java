package vip.laohei.sharesystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Resume 实体
 * 
 * @author laohei
 *
 */
@Entity
@Table(name = "tb_resume")
public class Resume {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "`key`")
	private String key;

	@Column(name = "`value`")
	private String value;

	@Column(name = "`order`")
	private String order = "0";

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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
		return "\n Resume ==> { " //
				+ "\n     id: " + id //
				+ ", \n     key: " + key //
				+ ", \n     value: " + value //
				+ ", \n     order: " + order //
				+ ", \n     state: " + state //
				+ ", \n     createTime: " + createTime //
				+ ", \n     updateTime: " + updateTime //
				+ "\n }";
	}

}
