package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DTO.MemParty;
import DTO.Member;
import DTO.Party;

public class VoteDAO {
	Connection conn = null;
	PreparedStatement ps = null; //명령어 정의(타입에 ? 사용)하고 인수를 전달 받아 명령어를 실행. executeQuery() 메소드에 의해 실행
	ResultSet rs = null; //sql문이 실행되어 결과로 반환한 데이터 관리(select한 결과값을 가지고 있음). get~함수를 이용해 레코드 내의 필드를 데이터 타입에 맞게 읽어올 수 있음.
						 //레코드를 이동하는 메소드를 가지고 있음. ex) next() 메소드 이용하여 다음 레코드로 이동. 다음 레코드가 없으면 false 반환.
						 //executeQuery() : select문처럼 실행 후 화면에 표시할 결과가 있을 때 사용. 리턴 타입: ResultSet형
						 //executeUpdate() : insert, update, delete처럼 출력 결과 없을 때
	
	//시험지에 있는 것 그대로. DB 연결
	public static Connection getConnection() throws Exception {
		Class.forName("oracle.jdbc.OracleDriver");
		//Connection 변수명 = DriverManager.getConnection("url", "id", "pw");
		Connection con = DriverManager.getConnection
				("jdbc:oracle:thin:@//localhost:1521/xe", "system", "sys1234");
		//con: 연결된 DB 정보를 담은 객체
		return con;
	}
	
	public String selectMember(HttpServletRequest request, HttpServletResponse response) {
		//컨트롤러에 보내기 위해 Member.java (엔티티 클래스)를 arraylist에 담는다.
		ArrayList<MemParty> list = new ArrayList<MemParty>();
	
		try {
			conn = getConnection();	//DB 연결
			
			//쿼리문 작성
			String sql = "select t1.m_no , t1.m_name, t2.p_name, decode(t1.p_school, '1', '고졸', '2', '학사', '3', '석사') p_school, substr(t1.m_jumin,1,6)||'-'||substr(t1.m_jumin,7)m_jumin, t1.m_city, t2.p_tel1||'-'||t2.p_tel2||'-'||(substr(t2.p_tel3,4)||substr(t2.p_tel3,4)||substr(t2.p_tel3,4)||substr(t2.p_tel3,4)) as p_tel1 from tbl_member_202005 t1 join tbl_party_202005 t2 on t1.p_code = t2.p_code";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				MemParty mp = new MemParty();
				
				mp.setM_no(rs.getString(1));
				mp.setM_name(rs.getString(2));
				mp.setP_name(rs.getString(3));
				mp.setP_school(rs.getString(4));
				mp.setM_jumin(rs.getString(5));
				mp.setM_city(rs.getString(6));
				mp.setP_tel1(rs.getString(7));
			
				list.add(mp);
			}
			System.out.println(list);
			request.setAttribute("list", list);
			
			conn.close();
			ps.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "member.jsp";
	}
	
	public int voting(HttpServletRequest request, HttpServletResponse response) {
		String v_jumin = request.getParameter("v_jumin");
		String v_name = request.getParameter("v_name");
		String m_no = request.getParameter("m_no");
		String v_time = request.getParameter("v_time");
		String v_area = request.getParameter("v_area");
		String v_confirm = request.getParameter("v_confirm");
		
		int result = 0;
		
		try {
			conn = getConnection();
			String sql = "insert into tbl_vote_202005 values(?, ?, ?, ?, ?, ?)";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, v_jumin);
			ps.setString(2, v_name);
			ps.setString(3, m_no);
			ps.setString(4, v_time);
			ps.setString(5, v_area);
			ps.setString(6, v_confirm);
			
//			System.out.println(v_jumin  + ":" +v_name + ":" +m_no + ":" +v_time + ":" +v_area + ":" +v_confirm);
			result = ps.executeUpdate();
			
			System.out.println("DAO" + result);
			
			ps.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
