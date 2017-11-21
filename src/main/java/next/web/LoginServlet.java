package next.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.db.DataBase;
import next.model.User;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");

		User loginUser = DataBase.findUserById(userId);
		if(loginUser == null || !password.equals(loginUser.getPassword())) {
			resp.sendRedirect("/user/login_failed.jsp");
			return;
		}

		HttpSession session = req.getSession();
		session.setAttribute("user", loginUser);

		resp.sendRedirect("/index.jsp");
	}
}
