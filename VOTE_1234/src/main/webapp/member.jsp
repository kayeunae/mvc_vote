<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="DTO.*" %>
<%
request.setCharacterEncoding("UTF-8");
ArrayList<MemParty> list = new ArrayList<MemParty>();
list = (ArrayList<MemParty>)request.getAttribute("list"); //강제형변환. list는 어레이리스트이고, list는 객체이므로 강제형변환.
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
	<%@ include file="topmenu.jsp" %>
	<section>
		<div class="title">후보 조회</div>
		<div class="wrapper">
			<table>
				<tr>
					<th>후보번호</th>
					<th>성명</th>
					<th>소속정당</th>
					<th>학력</th>
					<th>주민번호</th>
					<th>지역구</th>
					<th>대표전화</th>
				</tr>
				<%for(MemParty mp : list) {%>
				<tr>
					<td><%=mp.getM_no()%></td>
					<td><%=mp.getM_name()%></td>
					<td><%=mp.getP_name() %></td>
					<td><%=mp.getP_school() %></td>
					<td><%=mp.getM_jumin() %></td>
					<td><%=mp.getM_city() %></td>
					<td><%=mp.getP_tel1() %></td>
				</tr>
				<%} %>
				
			</table>
		</div>
	</section>
	<%@ include file="footer.jsp" %>
</body>
</html>