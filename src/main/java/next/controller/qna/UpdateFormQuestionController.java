package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;
import next.service.QnaService;

public class UpdateFormQuestionController extends AbstractController {
	private QnaService qnaService = new QnaService();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!UserSessionUtils.isLogined(request.getSession())) {
			return jspView("redirect:/users/loginForm");
		}

		long questionId = Long.parseLong(request.getParameter("questionId"));
		Question question = qnaService.findQuestion(questionId);

		String loginUser = UserSessionUtils.getUserFromSession(request.getSession()).getUserId();
		if (!question.getWriter().equals(loginUser)) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}

		return jspView("/qna/update.jsp").addObject("question", question);
	}
}
