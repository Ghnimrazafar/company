package com.cwiztech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cwiztech.model.Department;

public interface departmentRepository extends JpaRepository<Department, Long>{
	@Query(value = "select * from TBLDEPARTMENT where ISACTIVE='Y'", nativeQuery = true)
	public List<Department> findActive();
	
	@Query(value = "select * from TBLDEPARTMENT where DEPARTMENT_CODE=?1", nativeQuery = true)
	public Department findByCode(String code);
	
	@Query(value = "select * from TBLDEPARTMENT where NETSUITE_ID=?1", nativeQuery = true)
	public Department findByNetSuiteID(Long id);

	@Query(value = "select * from TBLDEPARTMENT "
			+ "where DEPARTMENT_NAME like ?1 or DEPARTMENT_DESCRIPTION like ?1 and ISACTIVE='Y'", nativeQuery = true)
	public List<Department> findBySearch(String search);

	@Query(value = "select * from TBLDEPARTMENT "
			+ "where DEPARTMENT_NAME like ?1 or DEPARTMENT_DESCRIPTION like ?1 ", nativeQuery = true)
	public List<Department> findAllBySearch(String search);


}
