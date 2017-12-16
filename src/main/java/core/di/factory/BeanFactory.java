package core.di.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import core.annotation.Controller;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }
    
    public Map<Class<?>, Object> getControllers() {
    	Map<Class<?>, Object> controllers = Maps.newHashMap();
    	for(Class<?> clazz : preInstanticateBeans) {
    		Annotation annotation = clazz.getAnnotation(Controller.class);
    		if(annotation != null) {
    			controllers.put(clazz, beans.get(clazz));
    		}
    	}
    	return controllers;
    }

    public void initialize() {
    	for(Class clazz: preInstanticateBeans) {
    		instantiateClass(clazz);
    	}
    }
    
    private Object instantiateClass(Class<?> clazz) {
    	Object bean = beans.get(clazz);
		if(bean != null) {
			return bean;
		}

		Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
		bean = instantiateConstructor(constructor);
		
    	if(bean == null) {
    		bean = BeanUtils.instantiate(clazz);
    		beans.put(clazz, bean);
    	}
    	
    	if(beans.get(clazz) == null) {
    		beans.put(clazz, bean);
    	}
    	
    	return bean;
    }
    
    private Object instantiateConstructor(Constructor<?> constructor) {
    	if(constructor == null) {
			return null;
		}
    	List<Object> args = Lists.newArrayList();
    	Class<?>[] paramClass = constructor.getParameterTypes();
    	
    	for(Class clazz : paramClass) {
    		clazz = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
    		Object bean = beans.get(clazz);
    		
    		bean = instantiateClass(clazz);
    		args.add(bean);
       	}
    	return BeanUtils.instantiateClass(constructor, args.toArray());
    }
}
