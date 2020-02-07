package sec03.brd01;

import java.util.List;

//부사장
//BoardDAO객체를 생성한 후 selectAllArticels()메소드를 호출해 전체 글을 검색해서 가져옵니다.
public class BoardService {

	BoardDAO boardDAO;
	
	public BoardService() {
		boardDAO = new BoardDAO();
	}
	
	public List<ArticleVO> listArticles(){ //BoardController사장님이 호출하는 메소드
		
		//부장인 BoardDAO의 selectAllArticels()메소드를 호출해 전체글을 검색해서 반환받음
		List<ArticleVO> articlesList = boardDAO.selectAllArticles();
		
		return articlesList; // 검색한 전체 글정보들(ArticleVO들)을 담고 있는 ArrayList를 서블릿에 반환
		
	}
	
}
