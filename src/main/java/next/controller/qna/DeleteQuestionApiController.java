package next.controller.qna;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.jdbc.DataAccessException;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.Result;

public class DeleteQuestionApiController extends AbstractController {
	private QuestionDao questionDao = new QuestionDao();
	private AnswerDao answerDao = new AnswerDao();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = jsonView();
		if (!UserSessionUtils.isLogined(request.getSession())) {
			return mv.addObject("result", Result.fail("로그인 후에 이용해주세요"));
		}

		long questionId = Long.parseLong(request.getParameter("questionId"));
		Question question = questionDao.findById(questionId);
		if (question == null) {
			return mv.addObject("result", Result.fail("존재하지 않는 질문입니다."));
		}

		String loginUser = UserSessionUtils.getUserFromSession(request.getSession()).getUserId();
		if (!question.getWriter().equals(loginUser)) {
			return mv.addObject("result", Result.fail("다른 사용자가 쓴 글을 삭제할 수 없습니다."));
		}

		List<Answer> answers = answerDao.findAllByQuestionId(questionId);
		if (answers.isEmpty()) {
			questionDao.deleteQuestion(questionId);
			return mv.addObject("result", Result.ok());
		}

		boolean canDelete = true;
		for (Answer answer : answers) {
			String writer = question.getWriter();
			if (!writer.equals(answer.getWriter())) {
				canDelete = false;
				break;
			}
		}

		if (!canDelete) {
			return mv.addObject("result", Result.fail("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다."));
		}

		return mv;
	}
}
