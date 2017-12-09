package next.controller.qna;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;

public class UpdateQuestionController extends AbstractController {
	private QuestionDao questionDao = new QuestionDao();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!UserSessionUtils.isLogined(request.getSession())) {
			return jspView("redirect:/users/loginForm");
		}

		long questionId = Long.parseLong(request.getParameter("questionId"));
		Question question = questionDao.findById(questionId);

		if (!question.getWriter().equals(UserSessionUtils.getUserFromSession(request.getSession()))) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}

		Question newQuestion = new Question(question.getWriter(), request.getParameter("title"), request.getParameter("contents"));
		newQuestion.setQuestionId(questionId);
		questionDao.updateQuestion(newQuestion);

		return jspView("redirect:/");
	}
}
