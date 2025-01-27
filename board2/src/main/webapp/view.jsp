<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Insert title here</title>
<link rel="stylesheet" href="./css/style.css" />
</head>
<body>
<div class="board_wrap">
      <div class="board_title">
        <strong>자유게시판</strong>
        <p>자유게시판 입니다.</p>
      </div>
      <div class="board_view_wrap">
        <div class="board_view">
          <div class="title">${board.title}</div> <!-- DTO에 있는 요소와 맞춰서 쓰기 -->
          <div class="info" style="position:relative;">
            <dl>
              <dt>번호</dt>
              <dd>${board.board_no}</dd>
            </dl>
            <dl>
              <dt>글쓴이</dt>
              <dd>${board.user_id}</dd>
            </dl>
            <dl>
              <dt>작성일</dt>
              <dd>${board.reg_date}</dd>
            </dl>
            <dl>
              <dt>조회</dt>
              <dd>${board.view}</dd>
            </dl>
            <dl style="position:absolute; right:0;">
              <dt><a href="#" onclick="chkDelete(${board.board_no}); return false;">삭제하기</a></dt>
            </dl>
          </div>
          <div class="cont" style="white-space:pre-wrap;">
			${board.content}
          </div>
        </div>
        <div class="bt_wrap">
          <a href="list" class="on">목록</a>
          <a href="edit?board_no=${board.board_no}">수정</a>	<!-- get방식 -->
        </div>
      </div>
    </div>
    	<script>
		//에러메세지 전달
		<c:if test="${param.error != null}">
			alert("${error}");	//error 메세지가 뜰 것(BoardController에 있는 value값이 뜬다.)
		</c:if>
		
		<c:if test="${error != null}">
		alert("${error}");	//error 메세지가 뜰 것(BoardController에 있는 value값이 뜬다.)
		</c:if>
	</script>
<script type="text/javascript" src="./script.js"></script>
</body>
</html>