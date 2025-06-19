package com.mu.io.game.server;

import com.mu.config.Global;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameSocketServer {
   private static final Logger logger = LoggerFactory.getLogger(GameSocketServer.class);
   private static ServerBootstrap bootstrap = null;

   public static boolean start() {
      try {
         bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
         bootstrap.setOption("tcpNoDelay", true);
         bootstrap.setPipelineFactory(new GameServerPipelineFactory());
         bootstrap.bind(new InetSocketAddress(Global.getGamePort()));
         logger.info("game server bind port {} success", Global.getGamePort());
         return true;
      } catch (Exception var1) {
         var1.printStackTrace();
         return false;
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
