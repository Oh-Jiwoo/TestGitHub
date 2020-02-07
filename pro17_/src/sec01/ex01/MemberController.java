package sec01.ex01;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 클라이언트가 웹브라우저 주소창에 주소를 입력하여 서블릿에게 요청합니다.
// 요청 주제 : DB에 존재하는 모든 회원정보를 검색해줘
// 요청 주소 : http://localhost:8090/pro17_/mem.do
//@WebServlet("/mem.do")
public class MemberController extends HttpServlet{
	
	MemberDAO memberDAO;
	
	//init()메소드에서 MemberDAO객체를 생성해서 초기화
	@Override
	public void init() throws ServletException {
		memberDAO = new MemberDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
						throws ServletException, IOException {
		doHandle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
						throws ServletException, IOException {
		doHandle(request, response);
	}
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) 
						throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		//MemberDAO의 listMembers()메소드를 호출하여 회원정보 조회요청에 대해
		//DB로부터 검색한 회원정보를 ArrayList로 반환받습니다.
		ArrayList membersList = memberDAO.listMembers();
		
		//이때 DB로부터 검색한 회원정보들 (ArrayList에 담신 MemberVO객체들)을 View페이지로
		//전달하기 위해 임시 저장 공간인 request내장객체 영역에 저장(바인딩)함.
		request.setAttribute("membersList", membersList);
		
		//그런 다음 RequestDispatcher클래스를 이용해 회원목록창(listMembers.jsp View페이지)로 포워딩함
		RequestDispatcher dispatcher = request.getRequestDispatcher("/test01/listMembers.jsp");
		dispatcher.forward(request, response);
		// = <jsp:forward page="/test01/listMembers.jsp">
		
		
	}
	
}
