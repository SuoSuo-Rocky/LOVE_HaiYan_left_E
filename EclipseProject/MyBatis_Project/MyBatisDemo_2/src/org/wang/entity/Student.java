package org.wang.entity;

public class Student {
	private int stuNo;
	private String stuName;
	private int stuAge;
	private String graName;

	public Student() {

	}

	public Student(int stuNo, String stuName, int stuAge, String graName) {
		super();
		this.stuNo = stuNo;
		this.stuName = stuName;
		this.stuAge = stuAge;
		this.graName = graName;
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
		return "\n"+"姓名为:" + this.stuName + ",id号 为:" + this.stuNo + ",年龄为：" + this.stuAge + ",班级为：" + this.graName;
	}
}
