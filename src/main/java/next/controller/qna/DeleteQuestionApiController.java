package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.model.Result;
import next.service.QnaService;

public class DeleteQuestionApiController extends AbstractController {
	private QnaService qnaService = new QnaService();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = jsonView();
		if (!UserSessionUtils.isLogined(request.getSession())) {
			return mv.addObject("result", Result.fail("로그인 후에 이용해주세요"));
		}

		long questionId = Long.parseLong(request.getParameter("questionId"));

		try{
			boolean deleteResult = qnaService.deleteQuestion(UserSessionUtils.getUserFromSession(request.getSession()), questionId);
			if(deleteResult) {
				return mv.addObject("result", Result.ok());
			}
			return mv.addObject("result", Result.fail("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다."));

		} catch (IllegalStateException e) {
			return mv.addObject("result", Result.fail(e.getMessage()));
		}
	}
}
