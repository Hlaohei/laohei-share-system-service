package vip.laohei.sharesystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import vip.laohei.sharesystem.entity.Works;

/**
 * WorksDao
 * 
 * @author laohei
 *
 */
public interface WorksDao extends JpaRepository<Works, String>, JpaSpecificationExecutor<Works> {

	Works findOneById(String id);

	@Modifying
	int deleteAllById(String shareId);

}
