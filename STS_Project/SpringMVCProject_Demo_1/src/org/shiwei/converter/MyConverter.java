package org.shiwei.converter;

import org.shiwei.entity.Student;
import org.springframework.core.convert.converter.Converter;

public class MyConverter implements  Converter<String, Student> {

	@Override
	public Student convert(String source) {
		// 接收 前端传来的   String  1-zs-23
		String[] studentStrArr = source.split("-");
		Student student = new Student();
		student.setId(Integer.parseInt(studentStrArr[0]));
		student.setName(studentStrArr[1]);
		student.setAge(Integer.parseInt(studentStrArr[2]));
		
		return student;
	}



}
