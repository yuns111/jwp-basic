package next.service;

import java.util.List;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;

public class QnaService {
	private QuestionDao questionDao = new QuestionDao();
	private AnswerDao answerDao = new AnswerDao();

	public void addQuestion(Question question) {
		questionDao.insert(question);
	}

	public Question findQuestion(long questionId) {
		return questionDao.findById(questionId);
	}

	public void updateQuestion(User user, Question updateQuestion) throws IllegalStateException {
		long questionId = updateQuestion.getQuestionId();
		Question question = this.findQuestion(questionId);

		String loginUser = user.getUserId();
		if (!question.getWriter().equals(loginUser)) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}
		questionDao.updateQuestion(updateQuestion);
	}

	public boolean deleteQuestion(User user, long questionId) throws IllegalStateException {
		Question question = this.findQuestion(questionId);
		if (question == null) {
			throw new IllegalStateException("존재하지 않는 질문입니다.");
		}

		String loginUser = user.getUserId();
		if (!question.getWriter().equals(loginUser)) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 삭제할 수 없습니다.");
		}

		List<Answer> answers = answerDao.findAllByQuestionId(questionId);
		if (answers.isEmpty()) {
			questionDao.deleteQuestion(questionId);
			return true;
		}

		for (Answer answer : answers) {
			String writer = question.getWriter();
			if (!writer.equals(answer.getWriter())) {
				return false;
			}
		}

		questionDao.deleteQuestion(questionId);
		return true;
	}

	public Answer addAnswer(Answer answer){
		Answer savedAnswer = answerDao.insert(answer);
		questionDao.updateCountOfAnswer(answer.getQuestionId());

		return savedAnswer;
	}

	public void deleteAnswer(long answerId) {
		Answer answer = answerDao.findById(answerId);

		answerDao.delete(answerId);
		questionDao.updateDeleteAnswer(answer.getQuestionId());
	}
}
