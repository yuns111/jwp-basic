package next.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.UserDao;

public class ListUserController extends AbstractController {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if (!UserSessionUtils.isLogined(req.getSession())) {
			return jspView("redirect:/users/loginForm");
		}

		UserDao userDao = new UserDao();
		req.setAttribute("users", userDao.findAll());
		return jspView("/user/list.jsp").addObject("users", userDao.findAll());
	}
}
