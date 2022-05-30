package com.cwiztech.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwiztech.model.CompanyAccount;
import com.cwiztech.model.CompanyAccount;




	public interface companyAccountRepository extends JpaRepository<CompanyAccount,Long> {

		@Query(value="select *from TBLORGANIZATIORESOURCE where ISACTIVE='Y'",nativeQuery =true)
		 public List<CompanyAccount> findActive(); 

		@Query(value = "select * TBLORGANIZATIORESOURCE from where  CompanyAccount_ID in (:ids) ", nativeQuery = true)
		public List<CompanyAccount> findByIDs(@Param("ids") List<Integer> ids);

		@Query(value = "select * from TBLORGANIZATIORESOURCE "
				+ "where (MAXIMUM_NODES ?1 or CompanyAccount_DESCRIPTION like ?1 or ACCESSINTERNET like ?1) and ISACTIVE='Y'", nativeQuery = true)
		public List<CompanyAccount> findBySearch(String search);

		@Query(value = "select * from TBLORGANIZATIORESOURCE "
				+ "where like MAXIMUM_NODES?1 or CompanyAccount_DESCRIPTION like ?1 or PROXY_USERNAME like ?1 ", nativeQuery = true)
		public List<CompanyAccount> findAllBySearch(String search);

		@Query(value = "select * from TBLORGANIZATIORESOURCE " 
				+ "where ISACTIVE='Y'", nativeQuery = true)
		List<CompanyAccount> findByAdvancedSearch(Long id);

		@Query(value = "select * from TBLORGANIZATIORESOURCE " 
				+ "where ID LIKE PROXY_HOSTNAME  WHEN ?1 = 0 THEN CompanyAccount_ ID   ELSE ?1  END ", nativeQuery = true)
		List<CompanyAccount> findAllByAdvancedSearch(Long id);
}
