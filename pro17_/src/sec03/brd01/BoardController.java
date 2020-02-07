package sec03.brd01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//���� ��û �ּ� : /board/listArticles.do �ּҷ� DB�� ����� ��ü�� ��� �˻� ��û��.
@WebServlet("/board/*")
public class BoardController extends HttpServlet{
	
	BoardService boardService;
	ArticleVO articleVO;
	
	//���� �ʱ�ȭ�� BoardService��ü�� ������.
	@Override
	public void init() throws ServletException {
		boardService = new BoardService();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doHandle(request, response);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doHandle(request, response);
		
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//�������� �ּҸ� ������ ���� ����
		String nextPage = "";
		
		//�ѱ�ó��
		request.setCharacterEncoding("UTF-8");
		
		//��û �ּ� ���
		String action = request.getPathInfo();
		System.out.println("action : "+action);
		
		try {
			List<ArticleVO> articlesList;
			
			if(action == null){ //��û���� null�϶�
				
				//DB�κ��� ��� ȸ������ �˻� ��û(�λ��� BoardService����)
				articlesList = boardService.listArticles();
				
				//�˻��� ȸ�� ������ ArrayList�� request��ü �޸𸮿� �����ؼ� ����
				request.setAttribute("articlesList", articlesList);
				
				//���û�� �� �̸��� nextPage������ ����
				nextPage = "/board01/listArticles.jsp"; // V
				
			}else if(action.equals("/listArticles.do")){
				
				//DB�κ��� ��� ȸ������ �˻� ��û(�λ��� BoardService����)
				articlesList = boardService.listArticles();
				
				//�˻��� ȸ�� ������ ArrayList�� request��ü �޸𸮿� �����ؼ� ����
				request.setAttribute("articlesList", articlesList);
				
				//���û�� �� �̸��� nextPage������ ����
				nextPage = "/board01/listArticles.jsp"; // V
				
			}
			
			//����ġ ������� ������
			RequestDispatcher dispatche = request.getRequestDispatcher(nextPage);
			
			//���û
			dispatche.forward(request, response);
			
		} catch (Exception e) {
			System.out.println("doHandle : "+e);
		}
		
	}

	
	
	
	
}
