package core.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
	private View view;
	private Map<String, Object> model = new HashMap<>();

	public ModelAndView(View view) {
		this.view = view;
	}

	public ModelAndView addObject(String attributeName, Object attribueValue) {
		model.put(attributeName, attribueValue);
		return this;
	}

	public Map<String, Object> getModel() {
		return Collections.unmodifiableMap(model);
	}

	public View getView() {
		return view;
	}
}
