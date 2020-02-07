package sec02.ex02;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//��Ʈ�ѷ� ������ �ϴ� MemberController���� Ŭ����.
//�� ��Ʈ�ѷ������� request�� getPathInfo()�޼ҵ带 �̿��� �δܰ�� �̷���� ��û�ּҸ� �����ɴϴ�.
//action���� ���� ���� if���� �б��ؼ� ��û�� �۾��� �����մϴ�.

// lestMembers.jsp���������� ȸ������ ��ũ Ŭ����
// /member/memberForm.do �ּҷ� ��û�� ����(ȸ������ �Է� �������� �̵� ������)

//memberForm.jsp���������� DB�� �߰��� ȸ������ �Է��� �����ϱ� ��ư Ŭ����
// /member/addmember.do �ּҷ� ��û�� ����(DB�� ���ο� ȸ�� �߰�����)

//Ŭ���̾�Ʈ�� ���������� �̿��Ͽ� ��Ʈ�ѷ��� ��û�ϸ� request��ü�� getPathInfo()�޼ҵ带 �̿���
//���� ��û �ּ��� /member/modeMemberForm.do�� /member/modeMember.do�� ������ �� 
//if������ �б��Ͽ� �۾��� �����ϵ��� MemberControllerŬ������ �ۼ�����

//listMembers.jsp���������� ȸ�� ������ ���� ������ũ�� Ŭ��������
//���� ��û �ּ��� /member/delMember.do�� ������ ��
//if������ �б��Ͽ� �۾��� �����ϵ��� MemberController

@WebServlet("/member/*") //������������ ��û�� �δܰ�� ��û�� �̷����
public class MemberController extends HttpServlet{
	
	MemberDAO memberDAO;
	
	//init()�޼ҵ忡�� MemberDAO��ü�� �����ؼ� �ʱ�ȭ
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
		
		//MVC�߿� View�ּ� ������ �뵵
		String nextPage = null;
		
		//�ѱ�ó��
		request.setCharacterEncoding("UTF-8");
		
		//������ ������ ���� ����
		response.setCharacterEncoding("text/html;charset=utf-8");
		
		//Ŭ���̾�Ʈ�� 2�ܰ��� ��û�ּҰ� ���
		String action = request.getPathInfo();
		// /memberForm.do
		// /addMember.do
		// /modeMemberForm.do
		// /modeMember.do
		// /delMember.do
		
		System.out.println("action : "+action);
		
		//action������ ���� ���� if���� �б��ؼ� ��û�� �۾��� �����ϴµ�
		//���� action������ ���� null�̰ų� /listMembers.do�� ��쿡 ȸ���˻������ ����.
		if(action == null || action.equals("/listMembers.do")){
		
			//MemberDAO�� listMembers()�޼ҵ带 ȣ���Ͽ� ȸ������ ��ȸ��û�� ����
			//DB�κ��� �˻��� ȸ�������� ArrayList�� ��ȯ�޽��ϴ�.
			ArrayList membersList = memberDAO.listMembers();
			
			//�̶� DB�κ��� �˻��� ȸ�������� (ArrayList�� ��� MemberVO��ü��)�� View��������
			//�����ϱ� ���� �ӽ� ���� ������ request���尴ü ������ ����(���ε�)��.
			request.setAttribute("membersList", membersList);
			
			//�˻��� ȸ������(����޼���)�� ������ View������ �ּ� ����
			// = test02������ listMembers.jsp�� �������ϱ� ���� �ּ� ����
			nextPage = "/test03/listMembers.jsp";
		
		}else if(action.equals("/memberForm.do")){ //ȸ������ �Է��������� �̵� ������!
			
			nextPage = "/test03/memberForm.jsp"; //ȸ������ �Է�������(VIEW)�ּ� ����
			
		}else if(action.equals("/addmember.do")){ //�Է��� ���ο� ȸ�������� DB�� �߰�����
			
			//��û������ (�Է��� ���ο� ȸ����������) request�������� ���
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			
			//MemberVO�� ����
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			
			//��û�� ȸ�������� DB�� ���̺� insert�ϱ� ���� �޼ҵ� ȣ��
			memberDAO.addMember(memberVO);
			
			//���ο� ȸ�������� DB�� ���̺� insert�� �����ϸ� ���������� ������ �޼��� ����
			request.setAttribute("msg", "addMember");
			
			//ȸ����� �� �ٽ� ȸ������� �˻��ؼ� �����ִ� View�������� �̵��ϱ� ����
			//�ٽ� MemberController.java������ ���û�� �ּ� ����
			nextPage = "/member/listMembers.do";
			
		//listMembers.jsp���������� ������ũ�� Ŭ��������
		//��Ʈ�ѷ��� ȸ������ ����â ��û�� ID�� ȸ�������� ��ȸ���� ����â���� ��������.
		}else if(action.equals("/modeMemberForm.do")){
			//�����ϱ� ���� ������ ȸ�� ID�� ���� �޾� �˻��ϱ� ����.
			String id = request.getParameter("id");
			
			//������ ȸ�� ID�� �ش��ϴ� ȸ������ �˻�.
			MemberVO memberInfo = memberDAO.findMember(id);
		
			//������ ȸ�� �Ѹ��� ������ �˻��Ͽ� �������� View������(����â)���� �����ϱ� ����
			//request���尴ü�� ����
			request.setAttribute("memInfo", memberInfo);
			
			//ȸ������ ����â View�������� �������ϱ� ���� �ּ� ����
			nextPage = "/test03/modeMemberForm.jsp";
			
		//ȸ������ ����â(modeMemberForm.jsp)���� ȸ������ ���� ������ �Է� �� �����ϱ� ��ư Ŭ���� ��
		// /member/modeMember.do�ּҷ� DB���̺� ����� ȸ�� ������ ���� ��û�� ������
		}else if(action.equals("/modeMember.do")){
			//��û�����(ȸ����������)
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			//MemberVO�� �� ������ ����
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			//DB ȸ�� ���̺��� ������ ���� ��û
			memberDAO.modeMember(memberVO); //UPDATE
			//���� UPDATE�� �����Ͽ� listMembers.jsp�� ���� �۾� �Ϸ� �޼����� ���� �ϱ� ����
			//request�� �޼��� ����
			request.setAttribute("msg", "modified");
			//���� ��  ��� ȸ���� �˻��Ͽ� �����ֱ� ����
			//MemberController.java�������� ��û�� �ּ� ����
			nextPage = "/member/listMembers.do";
			
		}else if(action.equals("/delMember.do")){
			//��û�� ���
			String id = request.getParameter("id");
			//������ ȸ�� ID�� �̿��Ͽ� DB����� ȸ������ �۾� ���
			memberDAO.delMember(id);
			
			request.setAttribute("msg", "delete");
			
			nextPage = "/member/listMembers.do";
			
		}else{ //�� �� action������ �ٸ� ��û URL�� ����Ǿ� ������ ȸ������� �����
			ArrayList membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/test03/listMembers.jsp";
			
		}
		
		
		//nextPage������ �ּҸ� ���� ��������
		RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
		dispatcher.forward(request, response);
		// = <jsp:forward page="/test01/listMembers.jsp">
		
		
	}
	
}
