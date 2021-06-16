package vip.laohei.sharesystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import vip.laohei.sharesystem.entity.Resume;

/**
 * ResumeDao
 * 
 * @author laohei
 *
 */
public interface ResumeDao extends JpaRepository<Resume, String>, JpaSpecificationExecutor<Resume> {

	/**
	 * 根据 id 查询简历信息
	 * 
	 * @param id
	 * @return
	 */
	Resume findOneById(String id);

	/**
	 * 根据 id 删除简历信息
	 * 
	 * @param resumeId
	 * @return
	 */
	@Modifying
	int deleteAllById(String resumeId);

}
