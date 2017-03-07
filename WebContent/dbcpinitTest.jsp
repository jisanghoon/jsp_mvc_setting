<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>Insert title here</title>
</head>
<body>

	<!--
		※NOTE※ 
		1. 테스트 하기 전  web.xml에서 DB 확인!!!!!
		2. 테스트 파일 (dbcpinitTest.jsp) 경로 => WEB-INF가  아닌  WebContent이다 !!!
	-->

	<p id="demo"></p>
	<%
		Connection conn = null;
		try {
			String jdbcDriver = "jdbc:apache:commons:dbcp:board";
			conn = DriverManager.getConnection(jdbcDriver);
			if (conn != null) {
				out.print("커넥션 풀에 연결되었습니다.");
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	%>
	<!--
		※NOTE※ 
		2017년 3월 6일 테스트 성공
		
		만약 안된다면 버전확인!!!		
	-->
</body>

</html>