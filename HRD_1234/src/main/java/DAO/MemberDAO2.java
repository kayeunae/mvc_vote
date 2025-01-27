package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DTO.Member;

public class MemberDAO2 {
	//DB 연결
	Connection conn = null;
	//쿼리문 실행
	PreparedStatement ps = null;
	//결과 담는 부분
	ResultSet rs = null;
	
	//데이터 베이스 연결
	public static Connection getConnection() throws Exception {
		Class.forName("oracle.jdbc.OracleDriver");
		Connection con = DriverManager.getConnection
					("jdbc:oracle:thin:@localhost:1521:xe", "system", "sys1234");
		return con;
	}
	
	//회원등록(insert)
	//시험장에선 인터넷이 안되기 때문에 utilBean 사용 불가, 요소마다 각자 request 받아 넣어줘야 함.
	public String insert(HttpServletRequest request, HttpServletResponse response) {
		int custno = Integer.parseInt(request.getParameter("custno"));
		String custname = request.getParameter("custname");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String joindate = request.getParameter("joindate");
		String grade = request.getParameter("grade");
		String city = request.getParameter("city");
		int result = 0;
		
		try {
			conn = getConnection();	//DB연결
			//어떤 타입의 데이터가 들어올지 모르므로 타입 부분은 전부 ?로 써준다.
			String sql = "insert into memeber_tbl_02 values(?,?,?,?,to_date(?, 'YYYY-MM-DD'),?,?";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, custno);
			ps.setString(2, custname);
			ps.setString(3, phone);
			ps.setString(4, address);
			ps.setString(5, joindate);
			ps.setString(6, grade);
			ps.setString(7, city);
			
			//insert, update, delete : 성공한 레코드의 갯수를 반환.
			result = ps.executeUpdate();
			
			System.out.println(result);
			
			//ps와 conn은 리소스이므로 사용 후에는 반드시 닫아줘야 한다.
			conn.close();
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "add";
	}
	
	//회원번호 자동증가
	public String nextCustno(HttpServletRequest request, HttpServletResponse response) {
		try {
			conn = getConnection();
			String sql = "select max(custno)+1 custno from member_tbl_02";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			int custno = 0;
			
			if(rs.next()) custno = rs.getInt(1);
			request.setAttribute("custno", custno);
			
			System.out.println(custno);
			
			
			conn.close();
			ps.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "add.jsp";
	}
	
	//회원목록조회/수정(select)
	public String selectAll(HttpServletRequest request, HttpServletResponse response) {
		//멤버 엔티티 클래스(DTO)를 담는 역할 (컨트롤러에 보내기 위한 !)
		ArrayList<Member> list = new ArrayList<Member>();
		try {
			conn= getConnection();
			
			//쿼리문 작성 (쿼리문이 길어져서 나누어 작성한 것...)
			String sql = "select custno, custname, phone, address, TO_CHAR(joindate, 'YYYY-MM-DD') joindate,";
			sql+= "DECODE(grade, 'A', 'VIP', 'B', '일반', 'C', '직원')grade, city from member_tbl_02 order by custno";
			
			//쿼리문 실행
			ps = conn.prepareStatement(sql);
			//데이터베이스에서 가져온 데이터를 rs에 넣어준다. (데이터 담아줌)
			rs = ps.executeQuery();
			
			//rs를 실행하고 다음 행이 있는지 없는지 확인 및 반복
			while(rs.next()) {
				Member member = new Member();
				member.setCustno(rs.getInt(1));
				member.setCustname(rs.getString(2));
				member.setPhone(rs.getString(3));
				member.setAddress(rs.getString(4));
				member.setJoindate(rs.getString(5));
				member.setGrade(rs.getString(6));
				member.setCity(rs.getString(7));
				
				list.add(member);
			}
			
			//setAttribute: 리퀘스트, 리스폰스할 때 사용할 수 있는 데이터를 저정함
			request.setAttribute("list", list);
			
			conn.close();
			ps.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "list.jsp";
	}

}
