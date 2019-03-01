package com.cduestc.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cduestc.mybatis.Employee;

public interface EmployeeMapperDynamicSQL {
	public List<Employee> getEmpsByIf(Employee employee);
	public List<Employee> getEmpsByTrim(Employee employee);
	public List<Employee> getEmpsByChoose(Employee employee);
	public void updateEmp(Employee employee);
	public void updateEmpByTrim(Employee employee);
	public List<Employee> getEmpsByForeach(@Param("ids")List<Integer> ids);
	
	public void addEmps(@Param("emps")List<Employee> emps);
	public void addEmps2(@Param("emps2")List<Employee> emps2);
}
