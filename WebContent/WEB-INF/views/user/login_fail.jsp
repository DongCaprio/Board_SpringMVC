<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var='root' value="${pageContext.request.contextPath }/"/>
<script>
	alert('로그인에 실패하였습니다')
	//로그인 fail 기본값을 컨트롤러에서 false로 설정함
	//로그인에 실패했을때는 true로 보내주기위해 밑에 설정함
	location.href = '${root}user/login?fail=true'
</script>