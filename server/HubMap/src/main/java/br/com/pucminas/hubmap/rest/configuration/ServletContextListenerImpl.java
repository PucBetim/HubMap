package br.com.pucminas.hubmap.rest.configuration;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.pucminas.hubmap.application.service.python.PythonService;

public class ServletContextListenerImpl implements ServletContextListener {

	private ExecutorService executor;
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		getLoggerFromClass(ServletContextListenerImpl.class).info("HubMap is shutdown...");
		PythonService pyServ = PythonService.getInstance();
		pyServ.destroy();
		executor.shutdown();
	}
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		getLoggerFromClass(ServletContextListenerImpl.class).info("HubMap is starting...");
		
		executor = Executors.newSingleThreadExecutor();
		PythonService pyServ = PythonService.getInstance();
		executor.execute(pyServ);
	}
}
