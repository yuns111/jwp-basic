package core.nmvc;

import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import core.annotation.Controller;

public class ControllerScanner {
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerScanner.class);
	private Reflections reflections;

	public ControllerScanner(Object... basePackage) {
		reflections = new Reflections(basePackage);
	}

	public Map<Class<?>, Object> getControllers() {
		Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
		return initControllers(controllers);
	}

	Map<Class<?>, Object> initControllers(Set<Class<?>> controllers) {
		Map<Class<?>, Object> controllerInstances = Maps.newHashMap();

		for (Class<?> clazz : controllers) {
			try {
				controllerInstances.put(clazz, clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return controllerInstances;
	}
}
