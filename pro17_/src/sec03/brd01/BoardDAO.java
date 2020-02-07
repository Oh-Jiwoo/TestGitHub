package sec03.brd01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

//부장
public class BoardDAO {

	private DataSource dataFactory; //커넥션풀을 저장할 변수 선언
			Connection conn;
			PreparedStatement pstmt;
			ResultSet rs;
			
	public BoardDAO() {
		try{
			//InitialContext객체가 하는 역할은  톰캣 실행시..
			//context.xml에 의해서 생성된 Context객체들에 접근을 하는 역할을 함.
			Context ctx = new InitialContext();
			
			//JDNI방법으로 접근하기 위해 기본경로(java:/comp/env)를 지정합니다.
			//환결설정에 관련된 컨텍스트 객체에 접근하기 위한 기본 주소 입니다.
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			
			/*커넥션풀 자원 얻기*/
			//그런후 다시 톰캣은 context.xml에 설정한 <Resource name="jdbc/oracle".../>
			//태그의  name속성값인? "jdbc/oracle"을 이용해 톰캣이 미리 DB에 연결해 놓은
			//DataSource객체(커넥션풀 역할을 하는 객체)를 받아 옵니다.
			dataFactory = (DataSource)envContext.lookup("jdbc/oracle");
			
		}catch(Exception err){
			err.printStackTrace();
		}
	}
	
	//BoardService클래스에서 BoardDAO의 selectAllArticles()메소드를 호출하면
	//계층형 SQL문을 이용해 계층형 구조로 전체 글을 조회한 후 반환합니다.
	public List<ArticleVO> selectAllArticles() {
		
		List articlesList = new ArrayList(); //조회한 글들을 저장하기 위한 용도
		
		try {
			
			//커넥션풀에서 커넥션 얻기
			conn = dataFactory.getConnection();
			//계층형 구조로 전체글을 조회하는 오라클의 계층형 SQL문
			String query = "select level,articleNo, parentNo, title, content, writedate, id from t_board start with parentNo = 0"
						   + " connect by prior articleNo = parentNo"
						   + " order siblings by articleNo desc";
			
			/*
			 	위 SELECT구문 참고 설명 : 
			 	1. 먼저 start with parentNo = 0
			 		-> patentNo의 값이 0인 글이 최상위 계층이다라는 의미
			 		   parentNo가 0이면 그 글은 최상위 부모글이 되는 것입니다.
			 		   
			 	2. connect by prior articleNo = parentNo
			 		-> 각 글이 어떤 부모글과 연결되는지 나타냅니다.
			 		
			 	3. order siblings by articleNo desc
			 		-> 계층 구조로 조회된 글을 articleNo 내림차순으로 정렬하여 검색.
			 */
			
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				int level = rs.getInt("level"); //각 글의 깊이(계층) 저장
				int articleNo = rs.getInt("articleNo"); // 글번호
				int parentNo = rs.getInt("parentNo"); //부모글번호
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				
				//검색한 글정보를 레코드 단위로 저장할 ArticleVO객체 생성 후 각 변수에 저장
				ArticleVO article = new ArticleVO();
				article.setLevel(level);
				article.setArticleNo(articleNo);
				article.setParentNo(parentNo);
				article.setTitle(title);
				article.setContent(content);
				article.setId(id);
				article.setWriteDate(writeDate);
				
				//ArrayList에 ArticleVO객체 추가
				articlesList.add(article);
				
			}
			
		} catch (Exception e) {
			System.out.println("selectAllArticles메소드 오류 : "+ e);
		} finally {
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return articlesList; // -> 부장 BoardDAO -> 부사장 BoardService에게 반환
	}

} //BoardDAO 부장
