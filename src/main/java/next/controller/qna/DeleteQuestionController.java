package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.service.QnaService;

public class DeleteQuestionController extends AbstractController {
	private QnaService qnaService = new QnaService();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!UserSessionUtils.isLogined(request.getSession())) {
			return jspView("redirect:/users/loginForm");
		}

		long questionId = Long.parseLong(request.getParameter("questionId"));
		boolean deleteResult = qnaService.deleteQuestion(UserSessionUtils.getUserFromSession(request.getSession()), questionId);

		if (!deleteResult) {
			throw new IllegalStateException("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
		}

		return jspView("redirect:/");
	}
}
