package core.mvc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerExecution;
import core.nmvc.HandlerMapping;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

	private LegacyHandlerMapping rm;
	private AnnotationHandlerMapping ahm;
	private List<HandlerMapping> handlerMappingList = Lists.newArrayList();

	@Override
	public void init() throws ServletException {
		rm = new LegacyHandlerMapping();
		ahm = new AnnotationHandlerMapping("next.controller");
		rm.initMapping();
		ahm.initMapping();

		handlerMappingList.add(rm);
		handlerMappingList.add(ahm);

	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
		Object handler = findHandler(req);

		ModelAndView mav;
		try {
			if (handler instanceof Controller) {
				mav = ((Controller)handler).execute(req, resp);
			} else if (handler instanceof HandlerExecution) {
				mav = ((HandlerExecution)handler).handle(req, resp);
			} else {
				throw new ServletException("잘못된 url 입니다.");
			}

			View view = mav.getView();
			view.render(mav.getModel(), req, resp);

		} catch (Throwable e) {
			logger.error("Exception : {}", e);
			throw new ServletException(e.getMessage());
		}
	}

	private Object findHandler(HttpServletRequest request) {
		Object handler = null;

		for (HandlerMapping handlerMapping : handlerMappingList) {
			handler = handlerMapping.getHandler(request);
			if (handler != null) {
				break;
			}
		}
		return handler;
	}
}
