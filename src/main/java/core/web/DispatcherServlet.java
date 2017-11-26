package core.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.controller.Controller;
import next.controller.HomeController;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
	private RequestMapping requestMapping;

	@Override
	public void init() {
		requestMapping = new RequestMapping();
		requestMapping.initMapping();
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String uri = request.getRequestURI();
		Controller controller = requestMapping.findController(uri);

		if(controller == null) {
			controller = new HomeController();
		}

		try {
			String view = controller.execute(request, response);
			String redirectMessage = "redirect:";

			if(view.startsWith(redirectMessage)) {
				response.sendRedirect(view.substring(redirectMessage.length()));
				return;
			}

			RequestDispatcher rd = request.getRequestDispatcher(view);
			rd.forward(request, response);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage());
		}
	}

}
