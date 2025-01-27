<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="DTO.Money" %>
<%
request.setCharacterEncoding("UTF-8");
ArrayList<Money> list = new ArrayList<Money>();
list = (ArrayList<Money>)request.getAttribute("list"); //강제형변환. list는 어레이리스트이고, list는 객체이므로 강제형변환.
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
	<%@ include file="topmenu.jsp"%>
	<section>
		<div class="title">회원매출조회</div>
		<div class="wrapper">
			<table style="width: 900px">
				<tr>
					<th>회원번호</th>
					<th>회원성명</th>
					<th>고객등급</th>
					<th>매출</th>
				</tr>
				<% for(Money m : list) {%>
				<tr>
					<td><%=m.getCustno() %></td>
					<td><%=m.getCustname()%></td>
					<td><%=m.getGrade()%></td>
					<td><%=m.getPrice()%></td>
				</tr>
				<%} %>
			</table>
		</div>
	</section>
	<%@ include file="footer.jsp"%>
</body>
</html>