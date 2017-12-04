package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;

public class AddQuestionController extends AbstractController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AddQuestionController.class);
	private QuestionDao questionDao = new QuestionDao();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		if (!UserSessionUtils.isLogined(session)) {
			return jspView("redirect:/users/loginForm");
		}

		User user = (User)session.getAttribute("user");
		Question question = new Question(user.getUserId(), request.getParameter("title"),
			request.getParameter("contents"));
		LOGGER.debug("question: {}" , question);

		questionDao.insert(question);
		return jspView("redirect:/");
	}
}
