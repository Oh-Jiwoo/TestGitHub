<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
    <%-- 컨텍스트 주소 얻기 --%>
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    
    <%
    	request.setCharacterEncoding("UTF-8");
    %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>검색한 글 목록 창</title>
</head>
<body>
	<table align="center" border="1" width="80%">
		<tr align="center" height="10" bgcolor="lightgreen">
			<td>글번호</td>
			<td>작성자</td>
			<td>글제목</td>
			<td>작성일</td>
		</tr>
		
<c:choose>
	<%-- BoardController.java서블릿으로부터 전달받은 request영역에 articlesList속성명(키)으로 바인딩된 ArraytList객체가 저장되어 있지 않다면 --%>
	<c:when test="${articlesList == null}">
		<tr height="10">
			<td colspan="4">
				<p align="center">
					<b><span style="font-size: 9pt">등록된 글이 없습니다.</span></b>
				</p>
			</td>
		</tr>
	</c:when>
	
	<%-- BoardController.java서블릿으로부터 전달받은 request영역에 articlesList속성명(키)으로 바인딩된 ArraytList객체가 저장되어 있다면 --%>
	<c:when test="${requestScope.articlesList != null}">
		<%-- 
			request영역에 저장된 ArrayList객체의 크기(검색한 글의 개수(ArticleVO객체의 개수))만큼 반복하여
			검색한 글 정보들(ArticleVO객체들)을 ArrayList객체 내부의 각 index위치로부터 차례대로 꺼내와
			글목록을 표시합니다.
		 --%>
		<c:forEach var="article" items="${articlesList}" varStatus="articleNum">
			<tr align="center">
				<td width="5%"${articleNum.count}></td> <%-- varStatus의 count속성을 이용해 글번호를 1부터 자동으로 표시 --%>
				<td width="10%">${article.id}</td>
				<td width="35%" align="left">
					<%-- 왼쪽으로 30px만큼 여백을 준 후 글제목을 표시할 목적으로 여백줌 --%>
					<span style="padding-right: 30px"></span>
					<c:choose>
						<%-- 조건 : <foreach>태그 반복시 각 글의 level값이 1보다 크면? 답글(자식글)이므로 --%>
						<c:when test="${artlcle.level > 1}">
							<%-- 다시 내부 <foreach>태그를 이용해서 1부터 level값까지 반복하면서 부모글 밑에 공백으로 들여쓰기하여 답글(자식글)임을 표시 --%>
							<c:forEach begin="1" end="${article.level}" step="1">
								<span style="padding-left: 20px"></span>
							</c:forEach>
							<%-- 공백 다음에 자식글을 표시 --%>
							<span style="font-size: 12px;">[답변]</span>
							<a>${article.title}</a>
						</c:when>
						<%-- 조건 : level값이 1보다 크지않으면 부모글이므로 공백없이 제목을 표시 --%>
						<c:otherwise>
							<a>${article.title}</a>							
						</c:otherwise>
					</c:choose>
				</td>
				<td width="10%">
					<fmt:formatDate value="${article.writeDate}"/>
				</td>
				
			</tr>
		</c:forEach>
	</c:when>
</c:choose>
	</table>
	
	<a class="cls1" href="#">글쓰기</a>
	
</body>
</html>