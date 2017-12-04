#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
* 톰캣 서버가 시작할 때 Servlet Context를 생성하여 초기화한다.
* 서블릿 컨테이너는 ServletContextListener 인터페이스를 구현하고 @WebListener 애노테이션이 붙어있는 클래스의 contextInitialized() 메서드를 호출함으로서 초기화 작업을 시작한다.
* 그 다음 설정에 따라 서블릿 컨테이너가 시작하는 시점 혹은 해당 서블릿이 호출되는 시점에 서블릿 인스턴스 생성과 초기화가 진행된다.
* 서블릿 인스턴스 생성후 init() 메서드를 호출함에 따라 초기화가 진행된다.
* loadOnStartup 설정을 함으로서 서블릿 컨테이너가 시작하는 시점에 생성과 초기화가 진행되고, 설정 숫자 값이 낮은 순으로 초기화가 된다.

#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
* localhost:8080으로 접근시 초기화된 서블릿중 DispatcherServlet의 url 패턴에 매핑되어 해당 서블릿의 service() 메서드가 호출된다.
* RequestMapping을 통해 해당 url에 대한 컨트롤러(Home Controller)를 찾아 실행한다.
* 컨트롤러의 execute 메서드를 통해 로직 수행 후 ModelAndView를 리턴한다. 이는 뷰 타입에 jsp 혹은 json view로 나뉜다.
* Jsp View는 JspView 클래스의 render() 메서드를 통해 해당 뷰로 이동한다.
* Json View 는 JsonView 클래스의 render() 메서드를 통해 json 데이터를 응답한다.

#### 7. next.web.qna package의 ShowController는 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* 
