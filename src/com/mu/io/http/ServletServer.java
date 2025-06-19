package com.mu.io.http;

import com.mu.config.Global;
import com.mu.io.http.servlet.GmServlet;
import com.mu.io.http.servlet.MuServlet;
import com.mu.io.http.servlet.plat.Platform;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.IPAccessHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletServer {
   private static Logger logger = LoggerFactory.getLogger(ServletServer.class);
   private static Server server = null;
   private static IPAccessHandler accessHander = new IPAccessHandler();

   public static void stop() {
      try {
         server.stop();
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }

   public static boolean start() {
      try {
         QueuedThreadPool threadPool = new QueuedThreadPool();
         threadPool.setMinThreads(1);
         server = new Server(threadPool);
         ServerConnector connector = new ServerConnector(server);
         connector.setPort(Global.getServletPort());
         server.setConnectors(new Connector[]{connector});
         ServletContextHandler context = new ServletContextHandler(1);
         context.setContextPath("/");
         context.addServlet(new ServletHolder(new GmServlet()), "/gm");
         context.addServlet(new ServletHolder(new MuServlet()), "/mu");
         Platform.createPlatServlet(context);
         accessHander.setHandler(context);
         server.setHandler(accessHander);
         server.start();
         return true;
      } catch (Exception var3) {
         var3.printStackTrace();
         return false;
      }
   }

   public static IPAccessHandler getHander() {
      return accessHander;
   }
}
