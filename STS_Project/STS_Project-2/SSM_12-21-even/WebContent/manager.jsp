<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.shiwei.entity.Book,org.shiwei.entity.Login"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title></title>
<meta name="description" content=" ">
<meta name="keywords" content=" ">
<link href=" " rel="stylesheet">
<link rel="stylesheet"
	href="Mlib/bootstrap-3.3.7/dist/css/bootstrap.min.css">
<script src="Mlib/de_jQuery_3_3_1.js"></script>
<script src="Mlib/bootstrap-3.3.7/dist/js/bootstrap.min.js"></script>
<style>
#ta {
	width: 85%;
	margin: 0 auto;
	font-size:20px;
}
</style>
</head>
<body>

	<nav class="navbar navbar-default" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">S_W</a>
			</div>
			<ul class="nav navbar-nav navbar-right">

				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown"> <span class="glyphicon glyphicon-cog "></span>
				</a>
					<ul class="dropdown-menu">
						<li><a href="#">帮助</a></li>
						<li class="divider"></li>
						<li><a href="index.jsp">退出</a></li>
					</ul></li>
			</ul>
			<form class="navbar-form navbar-right" role="search"
			action="addbookImg.jsp">
				<button type="submit" class="btn btn-default">添加图书</button>
			</form>
		</div>
	</nav>

	<table class="table table-hover success" id="ta">
		<caption class="text-center text-success" style="font-size: 28px;">我的图书信息</caption>
		<thead>
			<tr>
				<th>图书编号</th>
				<th>图书名</th>
				<th>图书等级</th>
				<th>图书图片文件名</th>
				<th>修改</th>
				<th>删除</th>
			</tr>
		</thead>
		<tbody>
			<%
				List<Book> mybook = (ArrayList<Book>) request.getAttribute("books");
				int s = mybook.size();
				for (int i = 0; i < s; i++) {
			%>

			<tr>
				<td><%=mybook.get(i).getBoId()%></td>
				<td><%=mybook.get(i).getBoName()%></td>
				<td><%=mybook.get(i).getBoRank()%></td>
				<td><%=mybook.get(i).getBoImg()%></td>
				<th><a href="shanchu.do?boid=<%=mybook.get(i).getBoId()%>"
					onClick="if(!confirm('确认要删除吗？'))return false;else return ture;">删除</a></th>
				<th><a href="update.jsp?boid=<%=mybook.get(i).getBoId()%>"
				 onClick="if(!confirm('确认要修改吗？'))return false;else return ture;">修改</a></th>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>


</body>
<script>
	
</script>

</html>