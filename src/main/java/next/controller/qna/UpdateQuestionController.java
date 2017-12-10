package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.model.Question;
import next.service.QnaService;

public class UpdateQuestionController extends AbstractController {
	private QnaService qnaService = new QnaService();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!UserSessionUtils.isLogined(request.getSession())) {
			return jspView("redirect:/users/loginForm");
		}

		Question newQuestion = new Question();
		newQuestion.setQuestionId(Long.parseLong(request.getParameter("questionId")));
		newQuestion.setWriter(request.getParameter("writer"));
		newQuestion.setTitle(request.getParameter("title"));
		newQuestion.setContents(request.getParameter("contents"));

		qnaService.updateQuestion(UserSessionUtils.getUserFromSession(request.getSession()), newQuestion);

		return jspView("redirect:/");
	}
}
