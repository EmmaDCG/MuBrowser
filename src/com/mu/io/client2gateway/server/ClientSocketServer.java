package com.mu.io.client2gateway.server;

import com.mu.config.Global;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSocketServer {
   private static final Logger logger = LoggerFactory.getLogger(ClientSocketServer.class);
   private static ServerBootstrap bootstrap = null;

   public static boolean start() {
      if (Global.isInterServiceServer()) {
         return true;
      } else {
         try {
            bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
            bootstrap.setOption("tcpNoDelay", true);
            bootstrap.setPipelineFactory(new ClientServerPipelineFactory());
            bootstrap.bind(new InetSocketAddress(Global.getGatewayPort()));
            logger.info("gateway server bind port {} success", Global.getGatewayPort());
            return true;
         } catch (Exception var1) {
            var1.printStackTrace();
            return false;
         }
      }
   }

   public static void stop() {
      try {
         bootstrap.releaseExternalResources();
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }
}
