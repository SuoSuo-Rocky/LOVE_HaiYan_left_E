<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
<h2>JSP内置对象的综合应用</h2>

<form action="logincheck.jsp" method="post" name="form1" onsubmit="return check()">
<table align="center">
<tr>
<td> 请输入学号：</td>
<td><input type="text" name="userid"></td>
</tr>

<tr>
<td>请输入姓名： </td>
<td><input type="text"  name="username"></td>
</tr>

<tr>
<td>请选择班级：</td>
<td><select name="choose">
<option selected="selected" value="16401">计算机科学01
<option  value="16402">计算机科学02
<option  value="16403">计算机科学03
<option  value="16405">计算机科学05
<option  value="16406">计算机科学06
</select>
</td>
</tr>

<tr>
<td><input type="submit" value="提交"></td>
<td><input type="reset" value="重置"></td>
</tr>

</table>
</form>
<%session.setAttribute("name", "login"); %>
</body>
</html>


<script type="text/javascript">
function  check() {
	if(form1.userid.value==""){
		alert("请输入学号：");
		form1.userid.focus();
		return false;
	}
	
	if(form1.username.value==""){
		alert("请输入姓名：");
		form1.username.focus();
		return false;
	}

}
</script>


















