package next.controller.qna;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.jdbc.DataAccessException;
import core.mvc.AbstractController;
import core.mvc.Controller;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.model.Result;

public class DeleteAnswerController extends AbstractController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAnswerController.class);

	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Long answerId = Long.parseLong(req.getParameter("answerId"));
		AnswerDao answerDao = new AnswerDao();

		try{
			answerDao.delete(answerId);
			return jsonView().addObject("result", Result.ok());

		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage(), e);
			return jsonView().addObject("result", Result.fail("Delete Fail"));
		}
	}
}
