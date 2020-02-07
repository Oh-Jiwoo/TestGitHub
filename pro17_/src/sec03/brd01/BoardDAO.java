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

//����
public class BoardDAO {

	private DataSource dataFactory; //Ŀ�ؼ�Ǯ�� ������ ���� ����
			Connection conn;
			PreparedStatement pstmt;
			ResultSet rs;
			
	public BoardDAO() {
		try{
			//InitialContext��ü�� �ϴ� ������  ��Ĺ �����..
			//context.xml�� ���ؼ� ������ Context��ü�鿡 ������ �ϴ� ������ ��.
			Context ctx = new InitialContext();
			
			//JDNI������� �����ϱ� ���� �⺻���(java:/comp/env)�� �����մϴ�.
			//ȯ�ἳ���� ���õ� ���ؽ�Ʈ ��ü�� �����ϱ� ���� �⺻ �ּ� �Դϴ�.
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			
			/*Ŀ�ؼ�Ǯ �ڿ� ���*/
			//�׷��� �ٽ� ��Ĺ�� context.xml�� ������ <Resource name="jdbc/oracle".../>
			//�±���  name�Ӽ�����? "jdbc/oracle"�� �̿��� ��Ĺ�� �̸� DB�� ������ ����
			//DataSource��ü(Ŀ�ؼ�Ǯ ������ �ϴ� ��ü)�� �޾� �ɴϴ�.
			dataFactory = (DataSource)envContext.lookup("jdbc/oracle");
			
		}catch(Exception err){
			err.printStackTrace();
		}
	}
	
	//BoardServiceŬ�������� BoardDAO�� selectAllArticles()�޼ҵ带 ȣ���ϸ�
	//������ SQL���� �̿��� ������ ������ ��ü ���� ��ȸ�� �� ��ȯ�մϴ�.
	public List<ArticleVO> selectAllArticles() {
		
		List articlesList = new ArrayList(); //��ȸ�� �۵��� �����ϱ� ���� �뵵
		
		try {
			
			//Ŀ�ؼ�Ǯ���� Ŀ�ؼ� ���
			conn = dataFactory.getConnection();
			//������ ������ ��ü���� ��ȸ�ϴ� ����Ŭ�� ������ SQL��
			String query = "select level,articleNo, parentNo, title, content, writedate, id from t_board start with parentNo = 0"
						   + " connect by prior articleNo = parentNo"
						   + " order siblings by articleNo desc";
			
			/*
			 	�� SELECT���� ���� ���� : 
			 	1. ���� start with parentNo = 0
			 		-> patentNo�� ���� 0�� ���� �ֻ��� �����̴ٶ�� �ǹ�
			 		   parentNo�� 0�̸� �� ���� �ֻ��� �θ���� �Ǵ� ���Դϴ�.
			 		   
			 	2. connect by prior articleNo = parentNo
			 		-> �� ���� � �θ�۰� ����Ǵ��� ��Ÿ���ϴ�.
			 		
			 	3. order siblings by articleNo desc
			 		-> ���� ������ ��ȸ�� ���� articleNo ������������ �����Ͽ� �˻�.
			 */
			
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				int level = rs.getInt("level"); //�� ���� ����(����) ����
				int articleNo = rs.getInt("articleNo"); // �۹�ȣ
				int parentNo = rs.getInt("parentNo"); //�θ�۹�ȣ
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				
				//�˻��� �������� ���ڵ� ������ ������ ArticleVO��ü ���� �� �� ������ ����
				ArticleVO article = new ArticleVO();
				article.setLevel(level);
				article.setArticleNo(articleNo);
				article.setParentNo(parentNo);
				article.setTitle(title);
				article.setContent(content);
				article.setId(id);
				article.setWriteDate(writeDate);
				
				//ArrayList�� ArticleVO��ü �߰�
				articlesList.add(article);
				
			}
			
		} catch (Exception e) {
			System.out.println("selectAllArticles�޼ҵ� ���� : "+ e);
		} finally {
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return articlesList; // -> ���� BoardDAO -> �λ��� BoardService���� ��ȯ
	}

} //BoardDAO ����
