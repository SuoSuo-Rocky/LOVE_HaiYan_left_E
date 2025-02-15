package org.wang.entity;

import java.io.Serializable;

//  实现 序列化 ------》 实现一个 接口。
public class Student  implements Serializable{
	private int stuNo;
	private String stuName;
	private int stuAge;
	private String graName;
	private boolean stuSex;

	public Student() {

	}

	public Student(int stuNo, String stuName, int stuAge, String graName) {
		super();
		this.stuNo = stuNo;
		this.stuName = stuName;
		this.stuAge = stuAge;
		this.graName = graName;
	}
	
	
	



	public boolean isStuSex() {
		return stuSex;
	}

	public void setStuSex(boolean stuSex) {
		this.stuSex = stuSex;
	}

	public int getStuNo() {
		return stuNo;
	}

	public void setStuNo(int stuNo) {
		this.stuNo = stuNo;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public int getStuAge() {
		return stuAge;
	}

	public void setStuAge(int stuAge) {
		this.stuAge = stuAge;
	}

	public String getGraName() {
		return graName;
	}

	public void setGraName(String graName) {
		this.graName = graName;
	}

	@Override
	public String toString() {
		return "\n"+"姓名为:" + this.stuName + ",id号 为:" + this.stuNo + ",年龄为：" 
	     + this.stuAge + ",班级为：" + this.graName + "性别为:" + this.stuSex;
	}
}
