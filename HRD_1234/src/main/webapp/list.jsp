<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="DTO.Member" %>
<%
request.setCharacterEncoding("UTF-8");
ArrayList<Member> list = new ArrayList<Member>();
list = (ArrayList<Member>)request.getAttribute("list"); //강제형변환. list는 어레이리스트이고, list는 객체이므로 강제형변환.
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
		<div class="title">홈쇼핑 회원관리 프로그램</div>
		<div class="wrapper">
			<table>
				<tr>
					<th>회원번호</th>
					<th>회원성명</th>
					<th>전화번호</th>
					<th>주소</th>
					<th>가입일자</th>
					<th>고객등급</th>
					<th>거주지역</th>
					<th>삭제</th>
				</tr>
				<% for(Member m : list) {%>
				<tr>
					<td><a href="modify?custno=<%=m.getCustno()%>"><%=m.getCustno() %></a></td>
					<td><%=m.getCustname()%></td>
					<td><%=m.getPhone()%></td>
					<td><%=m.getAddress()%></td>
					<td><%=m.getJoindate()%></td>
					<td><%=m.getGrade()%></td>
					<td><%=m.getCity()%></td>
					<td><a href="delete?custno=<%=m.getCustno()%>">Delete</a></td>
				</tr>
				<%} %>
			</table>
		</div>
	</section>
	<%@ include file="footer.jsp"%>
</body>
</html>