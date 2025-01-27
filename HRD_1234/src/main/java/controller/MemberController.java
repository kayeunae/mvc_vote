package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.MemberDAO;

//service메소드 오버라이딩하지 않는 MVC패턴 방식

//디폴트 서블릿(/): 모든 요청이 이쪽으로 옴
@WebServlet("/")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MemberController() {
        super();
    }
    
    //get방식이든, post방식이든 doPro메소드를 실행시킨다.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//컨트롤러로 넘어올 때는 인코딩을 해줘야 한다.
		request.setCharacterEncoding("UTF-8");	
		doPro(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");	
		doPro(request, response);
	}
	
	protected void doPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//매개변수로 받아올 때는 인코딩X 갠춘 !
		
		//context : tomcat에서 생성하는 요소. 웹 어플리케이션 하나당 하나의 servletcontext가 생성된다. 웹어플리케이션의 자원을 관리하고, 종료되면 사라진다.
		String context = request.getContextPath(); //contextPath: 톰캣의 context Path를 가져온다. (server.xml 156행의 path="/HRD_1234")
		String command = request.getServletPath(); //getServletPath : 파일명만 가져온다. (인터넷 주소 경로의 마지막 파일명을 가져옴)
		String site = null;
		
		System.out.println(context + ", " + command);
		
		MemberDAO member = new MemberDAO();
		
		switch (command) {
		case "/home":
			site = "index.jsp";
			break;
		case "/insert":
			site = member.insert(request, response);
			break;
		case "/list":
			site = member.selectAll(request, response);
			break;
		case "/add":
			site = member.nextCustno(request, response);
			break;
		case "/modify":
			site = member.modify(request, response);
			break;
		case "/result":
			site = member.selectResult(request, response);
			break;
		case "/update":
			int result1 = member.update(request, response);
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			if(result1 == 1) {	//업데이트 성공
				//java script 코드를 직접 입력해준다.
				out.println("<script>");	//location.href='/HRD_1234';
				out.println("alert('회원수정이 완료 되었습니다!'); location.href='" + context + "';");
				out.println("</script>");
				out.flush(); 	//위에서 쌓아놨다가 flush로 한꺼번에 내보낸다.
			} else {
				out.println("<script>");	//location.href='/HRD_1234';
				out.println("alert('수정실패!'); location.href='" + context + "';");
				out.println("</script>");
			}
			break;
		case "/delete":
			//DAO에 delete메소드를 만들어줘야 한다.
			int result2 = member.delete(request, response);
			response.setContentType("text/html; charset=UTF-8");
			//위에서 생성한 객체를 그대로 사용한다.
			out = response.getWriter();
			if(result2 == 1) {	//업데이트 성공
				//java script 코드를 직접 입력해준다.
				out.println("<script>");	//location.href='/HRD_1234';
				out.println("alert('회원삭제가 완료 되었습니다!'); location.href='" + context + "';");
				out.println("</script>");
				out.flush(); 	//위에서 쌓아놨다가 flush로 한꺼번에 내보낸다.
			} else {
				out.println("<script>");	//location.href='/HRD_1234';
				out.println("alert('삭제실패!'); location.href='" + context + "';");
				out.println("</script>");
			}
			break;
		}
		getServletContext().getRequestDispatcher("/" + site).forward(request, response);
	}												

}
