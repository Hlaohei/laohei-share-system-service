package vip.laohei.sharesystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ShareLabel 实体
 * 
 * @author laohei
 *
 */
@Entity
@Table(name = "tb_share_label")
public class ShareLabel {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "count")
	private long count = 0L;

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

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
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
		return "\n ShareLabel ==> { " //
				+ "\n     id: " + id //
				+ ", \n     name: " + name //
				+ ", \n     count: " + count //
				+ ", \n     createTime: " + createTime //
				+ ", \n     updateTime: " + updateTime //
				+ "\n }";
	}

}
