<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%  String  username=(String)session.getAttribute("name"); 
if(username==null){
	out.println("<h2>学号输入错误，请重新输入！</h2>");
	out.println("<h2>2 秒后，自动跳转到登录界面 </h2>");
	response.setHeader("refresh","2,main.jsp");
}else{
	String strTemp=(String)session.getAttribute("welcome");
%>
<h4><%=strTemp%></h4>	
	<%
	String strCount=(String)application.getAttribute("count");
	int count=1;
	if(strCount!=null){
		count=Integer.parseInt(strCount)+1;
	}
	application.setAttribute("count",String.valueOf(count));
	%>
	你是第<%=count%>参试者，现在时间<%= new Date().toLocaleString()%>
	<hr>
	<form action="score.jsp" method="post" >
	1.response使用下列哪个方法可以实现页面重定向：（）;<br>
	<input type="radio" name="r1" value="A">A.setAttribute()&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="radio" name="r1" value="B">B.setHeader()&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="radio" name="r1" value="C">C.sendRedirect()&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="radio" name="r1" value="D">D.setContentType()<br>
	2.使用管理员权限输入下列哪个命令可以打开注册表：（）<br>
	<input type="radio" name="r2" value="A">A.services.msc&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="radio" name="r2" value="B">B.cmd&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="radio" name="r2" value="C">C.regedit&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="radio" name="r2" value="D">D.shutdown -l<br>
	3.JS使用下列 命令可以实现一个消息警告框：（）<br>
	<input type="radio" name="r3" value="A">A.innerHTML()&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="radio" name="r3" value="B">B.alert()&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="radio" name="r3" value="C">C.out()&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="radio" name="r3" value="D">D.window()<br>
	4.下列属于JSP 内置对象的是：（）<br>
	<input type="checkbox" name="r4" value="A">A.session&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="checkbox" name="r4" value="B">B.request&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="checkbox" name="r4" value="C">C.window&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="checkbox" name="r4" value="D">D.Handler<br>
	
	
	
	
	
	
	<br><br><br>
	<input type="submit" value="提交答案" name="submit">&nbsp;
	<input type="reset" value="重置" name="reset">
	
	</form>
	<%} %>
	
</body>
</html>