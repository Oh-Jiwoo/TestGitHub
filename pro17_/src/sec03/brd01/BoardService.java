package sec03.brd01;

import java.util.List;

//�λ���
//BoardDAO��ü�� ������ �� selectAllArticels()�޼ҵ带 ȣ���� ��ü ���� �˻��ؼ� �����ɴϴ�.
public class BoardService {

	BoardDAO boardDAO;
	
	public BoardService() {
		boardDAO = new BoardDAO();
	}
	
	public List<ArticleVO> listArticles(){ //BoardController������� ȣ���ϴ� �޼ҵ�
		
		//������ BoardDAO�� selectAllArticels()�޼ҵ带 ȣ���� ��ü���� �˻��ؼ� ��ȯ����
		List<ArticleVO> articlesList = boardDAO.selectAllArticles();
		
		return articlesList; // �˻��� ��ü ��������(ArticleVO��)�� ��� �ִ� ArrayList�� ������ ��ȯ
		
	}
	
}
