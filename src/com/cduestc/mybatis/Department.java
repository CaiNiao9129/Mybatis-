package com.cduestc.mybatis;

import java.io.Serializable;
import java.util.List;

public class Department implements Serializable{
	private Integer did;
	private String deptName;
	private List<Employee> emps;
	
	
	
	public Department(Integer did) {
		super();
		this.did = did;
	}
	public Integer getDid() {
		return did;
	}
	public void setDid(Integer did) {
		this.did = did;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public List<Employee> getEmps() {
		return emps;
	}
	public void setEmps(List<Employee> emps) {
		this.emps = emps;
	}
	@Override
	public String toString() {
		return "Department [did=" + did + ", deptName=" + deptName + "]";
	}
	

}
