package next.controller.qna;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;

public class DeleteQuestionController extends AbstractController {
	private QuestionDao questionDao = new QuestionDao();
	private AnswerDao answerDao = new AnswerDao();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!UserSessionUtils.isLogined(request.getSession())) {
			return jspView("redirect:/users/loginForm");
		}

		long questionId = Long.parseLong(request.getParameter("questionId"));
		Question question = questionDao.findById(questionId);
		if (question == null) {
			throw new IllegalStateException("존재하지 않는 질문입니다.");
		}

		String loginUser = UserSessionUtils.getUserFromSession(request.getSession()).getUserId();
		if (!question.getWriter().equals(loginUser)) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 삭제할 수 없습니다.");
		}

		List<Answer> answers = answerDao.findAllByQuestionId(questionId);
		if (answers.isEmpty()) {
			questionDao.deleteQuestion(questionId);
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
			throw new IllegalStateException("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
		}

		return jspView("redirect:/");
	}
}
