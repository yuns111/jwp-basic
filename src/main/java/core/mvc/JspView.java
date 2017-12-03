package core.mvc;

import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {
	private static final String REDIRECT_PREFIX = "redirect:";
	private String viewName;

	public JspView(String viewName) {
		if(viewName == null) {
			throw new NullPointerException("이동할 url을 입력해주세요");
		}
		this.viewName = viewName;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(viewName.startsWith(REDIRECT_PREFIX)) {
			response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
			return;
		}

		Set<String> keys = model.keySet();
		for(String key: keys) {
			request.setAttribute(key, model.get(key));
		}

		RequestDispatcher rd = request.getRequestDispatcher(viewName);
		rd.forward(request, response);
	}
}
