package contoller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.VoteDAO;

@WebServlet("/")
public class VoteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public VoteController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");	
		doPro(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");	
		doPro(request, response);
	}
	
	protected void doPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//dao에서 쿼리문 작성해서 데이터 끌어오는 것
		
		
		String context = request.getContextPath();	//VOTE_1234
		String command = request.getServletPath();	//url 마지막 경로
													//topmenu의 <a>태그 href가 보냄 !
		String site = null;
		
		System.out.println(context + " " +command );
		
		VoteDAO vote = new VoteDAO();
		
		//경로나누기
		switch (command) {
		case "/home" :
			site = "";
			break;
		case "/member" :
			site = vote.selectMember(request, response);
			break;
		case "/doVote" :
			site = "doVote.jsp";
			break;
		case "/voting" :
			int result = vote.voting(request, response);
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			System.out.println("controller" + result);
			if(result == 1) {
				out.println("<script>");
				out.println("alert('투표하기 정보가 정상적으로 등록 되었습니다!'); location.href='"+context+"';");
				out.println("</script>");
				out.flush();
			} else {
				out.println("<script>");
				out.println("alert('투표 실패!'); location.href='"+context+"';");
				out.println("</script>");
				out.flush();
			}
			break;
		case "/view": 
			site = vote.selectVoter(request, response); 
			break;
		case "/rate":
			site = vote.rating(request, response);
		}
		getServletContext().getRequestDispatcher("/" + site).forward(request, response);
	}

}
