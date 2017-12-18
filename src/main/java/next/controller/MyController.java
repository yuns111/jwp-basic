package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.mvc.ModelAndView;

@Controller
public class MyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyController.class);

	@RequestMapping("/users/findUserId")
	public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.debug("findUserId");
		return null;
	}
}
