package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DTO.Member;
import DTO.Money;

public class MemberDAO {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	// 데이터 베이스 연결 메소드
	public static Connection getConnection() throws Exception {
		Class.forName("oracle.jdbc.OracleDriver");
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "sys1234");
		return con;
	}

	// 회원 등록
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
			conn = getConnection(); // DB연결
			// 어떤 타입의 데이터가 들어올지 모르므로 타입 부분은 전부 ?로 써준다.
			String sql = "insert into memeber_tbl_02 values(?,?,?,?,to_date(?, 'YYYY-MM-DD'),?,?)";

			ps = conn.prepareStatement(sql);
			ps.setInt(1, custno);
			ps.setString(2, custname);
			ps.setString(3, phone);
			ps.setString(4, address);
			ps.setString(5, joindate);
			ps.setString(6, grade);
			ps.setString(7, city);

			// insert, update, delete : 성공한 레코드의 갯수를 반환.
			result = ps.executeUpdate();

			System.out.println(result);

			// ps와 conn은 리소스이므로 사용 후에는 반드시 닫아줘야 한다.
			conn.close();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "add";
	}

	// 회원번호 자동증가
	public String nextCustno(HttpServletRequest request, HttpServletResponse response) {
		try {
			conn = getConnection();
			String sql = "select max(custno)+1 custno from member_tbl_02";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			int custno = 0;

			if (rs.next())
				custno = rs.getInt(1);
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

	// 회원목록조회 / 수정
	public String selectAll(HttpServletRequest request, HttpServletResponse response) {
		// 멤버 엔티티 클래스(DTO)를 담는 역할 (컨트롤러에 보내기 위한 !)
		ArrayList<Member> list = new ArrayList<Member>();
		try {
			conn = getConnection();

			// 쿼리문 작성 (쿼리문이 길어져서 나누어 작성한 것...)
			String sql = "select custno, custname, phone, address, TO_CHAR(joindate, 'YYYY-MM-DD') joindate,";
			sql += "DECODE(grade, 'A', 'VIP', 'B', '일반', 'C', '직원')grade, city from member_tbl_02 order by custno";

			// 쿼리문 실행
			ps = conn.prepareStatement(sql);
			// 데이터베이스에서 가져온 데이터를 rs에 넣어준다. (데이터 담아줌)
			rs = ps.executeQuery();

			// rs를 실행하고 다음 행이 있는지 없는지 확인 및 반복
			while (rs.next()) {
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

			// setAttribute: 리퀘스트, 리스폰스할 때 사용할 수 있는 데이터를 저정함
			request.setAttribute("list", list);

			conn.close();
			ps.close();
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "list.jsp";
	}

	public String selectResult(HttpServletRequest request, HttpServletResponse response) {
		// 멤버 엔티티 클래스(DTO)를 담는 역할 (컨트롤러에 보내기 위한 !)
		ArrayList<Money> list = new ArrayList<Money>();
		try {
			conn = getConnection();
			//group by를 여러 개를 하면 select로 빼오는 것도 똑같이 맞춰줘야 한다.
			String sql = "select m1.custno, m1.custname, "
					+ "DECODE(grade, 'A', 'VIP', 'B', '일반', 'C', '직원')grade, "
					+ "sum(m2.price) price "
					+ "from member_tbl_02 m1, money_tbl_02 m2 "
					+ "where m1.custno = m2.custno "
					+ "group by(m1.custno, m1.custname, grade) "
					+ "order by price desc";
			
			// 쿼리문 실행
			ps = conn.prepareStatement(sql);
			// 데이터베이스에서 가져온 데이터를 rs에 넣어준다. (데이터 담아줌)
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Money money = new Money();
				money.setCustno(rs.getInt(1));
				money.setCustname(rs.getString(2));
				money.setGrade(rs.getString(3));
				money.setPrice(rs.getInt(4));
				
				list.add(money);
			}
			
			request.setAttribute("list", list);
			
			conn.close();
			ps.close();
			rs.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "result.jsp";
	}

	// 회원정보 수정
	public String modify(HttpServletRequest request, HttpServletResponse response) {
		try {
			conn = getConnection();
			int custno = Integer.parseInt(request.getParameter("custno"));

			// list.jsp 파일을 보면, custno를 매개변수로 받아온다.
			String sql = "select custname, phone, address, TO_CHAR(joindate, 'YYYY-MM-DD') joindate, grade, city ";
			sql += "from member_tbl_02 where custno=" + custno;

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			Member member = new Member();

			if (rs.next()) {
				member.setCustno(custno);
				member.setCustname(rs.getString(1));
				member.setPhone(rs.getString(2));
				member.setAddress(rs.getString(3));
				member.setJoindate(rs.getString(4));
				member.setGrade(rs.getString(5));
				member.setCity(rs.getString(6));
			}

			request.setAttribute("member", member);
			request.setAttribute("custno", custno);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "modify.jsp";
	}

	public int update(HttpServletRequest request, HttpServletResponse response) {
		int custno = Integer.parseInt(request.getParameter("custno"));
		String custname = request.getParameter("custname");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String joindate = request.getParameter("joindate");
		String grade = request.getParameter("grade");
		String city = request.getParameter("city");
		int result = 0;

		try {
			conn = getConnection();

			String sql = "update member_tbl_02 set";
			sql += " custname = ?,";
			sql += " phone = ?,";
			sql += " address = ?,";
			sql += " joindate = to_date(?, 'yyyy-mm-dd'),";
			sql += " grade = ?,";
			sql += " city = ?";
			sql += " where custno = ?";

			ps = conn.prepareStatement(sql);
			ps.setString(1, custname);
			ps.setString(2, phone);
			ps.setString(3, address);
			ps.setString(4, joindate);
			ps.setString(5, grade);
			ps.setString(6, city);
			ps.setInt(7, custno);

			// insert, update, delete : 성공한 레코드의 갯수를 반환.
			// insert, update, delete 구문은 executeUpdate()를 사용해줘야 한다.
			result = ps.executeUpdate();

			conn.close();

			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 성공하면 1이 반환된당 (성공한 레코드의 갯수를 반환하기 때무네)
		return result;
	}

	public int delete(HttpServletRequest request, HttpServletResponse response) {
		int result = 0;
		try {
			conn = getConnection();
			String custno = request.getParameter("custno");
			String sql = "delete from member_tbl_02 where custno = " + custno;

			ps = conn.prepareStatement(sql); // 쿼리문 컴파일
			result = ps.executeUpdate(); // 쿼리문 실행

			ps.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 성공하면 1을 리턴
		return result;
	}

}
