package com.mu.io.secure;

import com.mu.config.Global;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Security843SocketServer {
   private static final Logger logger = LoggerFactory.getLogger(Security843SocketServer.class);
   private static ServerBootstrap bootstrap = null;

   public static void start() {
      if (Global.isOpen843Server()) {
         try {
            bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
            bootstrap.setOption("tcpNoDelay", true);
            bootstrap.setPipelineFactory(new Security843SocketServerPipelineFactory());
            bootstrap.bind(new InetSocketAddress(843));
            logger.info("security server bind port {} success", Integer.valueOf(843));
         } catch (Exception var1) {
            logger.error(var1.toString());
         }

      }
   }

   public static void stop() {
      try {
         if (bootstrap != null) {
            bootstrap.releaseExternalResources();
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }
}
