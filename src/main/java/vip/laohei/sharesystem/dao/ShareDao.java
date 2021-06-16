package vip.laohei.sharesystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import vip.laohei.sharesystem.entity.Share;

/**
 * ShareDao
 * 
 * @author laohei
 *
 */
public interface ShareDao extends JpaRepository<Share, String>, JpaSpecificationExecutor<Share> {

	Share findOneById(String id);

	@Modifying
	int deleteAllById(String shareId);

}
