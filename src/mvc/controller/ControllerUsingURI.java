package mvc.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerUsingURI extends HttpServlet {
	
	// <커맨드, 핸들러인스턴스> 매핑 정보 저장
    private HashMap<String, CommandHandler> commandHandlerMap = new HashMap<String, CommandHandler>();

    
    public void init() throws ServletException {
    	
    	///WEB-INF/CommandHandler.properties
        String configFile = getInitParameter("configFile");
        Properties prop = new Properties();
        String configFilePath = getServletContext().getRealPath(configFile);
        
        
        try (FileReader fis = new FileReader(configFilePath)) {
            prop.load(fis);
        } catch (IOException e) {
            throw new ServletException(e);
        }
        
        
        Iterator keyIter = prop.keySet().iterator();
        while (keyIter.hasNext()) {
        	
        	
            String command = (String) keyIter.next();//key이름 : 명령어 /simple.do
            //mvc.simple.SimpleHan\
            
            String handlerClassName = prop.getProperty(command);//key값 : handler클래스
            
            
            try {
                Class<?> handlerClass = Class.forName(handlerClassName);
                CommandHandler handlerInstance = 
                        (CommandHandler) handlerClass.newInstance();//동적 class 로딩
                commandHandlerMap.put(command, handlerInstance);
                
                
              
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
               
            	/*InstantiationException => 짧게 말해서 newInstance 메소드로 객체를 생성하려는 대상이 추상클래스이면 
                 * 이 에러가 난다는 이야기다. 왜? 라고 묻는 분는... 자바문법책을 한번 더 읽어보셔야 한다. 
                 * 추상클래스(abstract) 혹은 인터페이스는 new 연산자를 이용하여 직접 객체를 생성할 수 없다. 
                 * 추상클래스 혹은 인터페이스를 상속받아 추상메소드들을 구현해놓은 그 클래스로 객체를 생성할 수 있다.*/
            	
            	
            	/*IllegalAccessException => 어플리케이션이 클래스를 로드했을 때, 그 클래스가 public 나오지 않기도 하고, 
            	 *다른 패키지에 들어가 있거나 하기 위해서, 실행중의 메소드가 지정된 클래스의 정의에 액세스 할 수 없는 경우에 슬로우 되는 예외입니다.		
				     또, 이 클래스의 인스턴스는, 
				     어플리케이션이 Class 클래스의 newInstance 메소드를 사용해 클래스의 인스턴스의 생성을 시도할 경우나, 
				     현재의 메소드가 적절한 인수 없음의 constructor에의 액세스를 가지지 않는 경우에도 슬로우 됩니다.*/
            	
            	
            	throw new ServletException(e);
            	//ServletException => Defines a general exception a servlet can throw when it encounters difficulty.
            }
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException {
    	//주소창의 uri 정보를 가지고 온다.
    	//uri : /chap18/hello.do
    	//우리에게 필요한 매핑정보는 contextPath(/chap18)를 제외한 부분이므로 string에서 contextPath를 제외하고 가져온다.
		String command = request.getRequestURI();
		if (command.indexOf(request.getContextPath()) == 0) {
			command = command.substring(request.getContextPath().length());
		}
		
		//key에 해당하는 handler를 가져온다.
        CommandHandler handler = commandHandlerMap.get(command);
        if (handler == null) {
            handler = new NullHandler();
        }
        
        
        String viewPage = null;
        try {
            viewPage = handler.process(request, response);
        } catch (Throwable e) {
            throw new ServletException(e);
        }
        if (viewPage != null) {
	        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
	        dispatcher.forward(request, response);
        }
    }
}
