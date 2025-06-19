package com.mu;

import com.mu.game.CenterManager;
import com.mu.io.client2gateway.server.ClientSocketServer;
import com.mu.io.game.server.GameSocketServer;
import com.mu.io.game2gateway.server.GatewaySocketClient;
import com.mu.io.http.ServletServer;
import com.mu.io.secure.Security843SocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownHook extends Thread {
   private static Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

   public void run() {
      try {
         CenterManager.dropAllPlayers();
         Security843SocketServer.stop();
         ServletServer.stop();
         Thread.sleep(60000L);
         GameSocketServer.stop();
         ClientSocketServer.stop();
         GatewaySocketClient.stop();
         logger.info("server is shutdown...");
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
