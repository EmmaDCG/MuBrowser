package com.mu.io.game2gateway.server;

import com.mu.config.Global;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GatewaySocketClient {
   private static final Logger logger = LoggerFactory.getLogger(GatewaySocketClient.class);
   private static ClientBootstrap bootstrap;

   public static final boolean start() {
      if (Global.isInterServiceServer()) {
         return true;
      } else {
         try {
            bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
            bootstrap.setOption("tcpNoDelay", true);
            bootstrap.setOption("keepAlive", true);
            bootstrap.setPipelineFactory(new GatewayClientPipelineFactory());
            logger.info("GatewaySocketClient is inited");
            return true;
         } catch (Exception var1) {
            var1.printStackTrace();
            return false;
         }
      }
   }

   public static final void connect(Channel channel, int counter, String host, int port, int initTypr) {
      try {
         ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
         future.addListener(new GatewayConnectionFutureListener(channel, counter, host, port, initTypr));
      } catch (Exception var6) {
         var6.printStackTrace();
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
