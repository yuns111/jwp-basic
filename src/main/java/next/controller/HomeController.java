package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.Controller;
import core.mvc.JsonView;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import next.dao.QuestionDao;

public class HomeController extends AbstractController {
    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        QuestionDao questionDao = new QuestionDao();

        return jspView("home.jsp").addObject("questions", questionDao.findAll());
    }
}
