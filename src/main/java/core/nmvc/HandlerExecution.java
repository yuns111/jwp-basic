package core.nmvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import core.annotation.RequestMethod;
import core.mvc.ModelAndView;

public class HandlerExecution {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerExecution.class);
    private Object declaredObject;
    private Method method;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = null;
        try {
            mv = (ModelAndView) method.invoke(declaredObject, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return mv;
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());

        return handlerExecutions.get(new HandlerKey(uri, method));
    }
}
