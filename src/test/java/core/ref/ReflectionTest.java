package core.ref;

import java.lang.reflect.Field;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.model.Question;
import next.model.User;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() throws NoSuchFieldException {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        logger.debug("{}",clazz.getDeclaredFields());
        logger.debug("{}",clazz.getConstructors());
    }
    
    @Test
    public void newInstanceWithConstructorArgs() {
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());
    }
    
    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Field fieldName= clazz.getDeclaredField("name");
        Field fieldAge= clazz.getDeclaredField("age");

        fieldName.setAccessible(true);
        fieldAge.setAccessible(true);

        Student student = new Student();
        fieldName.set(student, "성윤");
        fieldAge.set(student, 24);

        logger.debug("이름: {}, 나이: {}",student.getName(), student.getAge());
    }
}
