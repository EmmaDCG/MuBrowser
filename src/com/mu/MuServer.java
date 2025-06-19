package com.mu;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.db.manager.GameDBManager;
import com.mu.db.manager.GlobalDBManager;
import com.mu.db.manager.MarketDBManager;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.model.team.TeamManager;
import com.mu.game.task.schedule.ScheduleTask;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.game.task.specified.day.ZeroDailyTask;
import com.mu.io.client2gateway.server.ClientSocketServer;
import com.mu.io.game.server.GameSocketServer;
import com.mu.io.game2gateway.server.GatewaySocketClient;
import com.mu.io.http.ServletServer;
import com.mu.io.secure.Security843SocketServer;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuServer {
   private static final Logger logger = LoggerFactory.getLogger(MuServer.class);

   private static boolean initConfigs() {
      return initGlobal() && initDatabase() && initGlobalData();
   }

   private static boolean initGlobal() {
      if (!Global.init()) {
         logger.error("init global properties error,server not start!!!");
         return false;
      } else {
         return true;
      }
   }

   private static boolean initDatabase() {
      if (!Pool.init()) {
         logger.error("init Database error,server not start!!!");
         return false;
      } else {
         Global.checkOpenServer();
         return true;
      }
   }

   private static boolean initGlobalData() {
      return GlobalDBManager.initData();
   }

   private static boolean startServer() {
      Security843SocketServer.start();
      initGameModule();
      MarketDBManager.searchAllMarketItem();
      return GatewaySocketClient.start() && ClientSocketServer.start() && GameSocketServer.start() && ServletServer.start();
   }

   private static void initGameModule() {
      ScheduleTask.start();
      SpecifiedTimeManager.start();
      TeamManager.startBroadcastTask();
      ZeroDailyTask.checkNightProtectedWhenServerStar();
      DungeonManager.startCheckDungeon();
      DungeonTemplateFactory.startTimingDungeon();
   }

   public static void main(String[] args) {
      DOMConfigurator.configure("configs/log4j.xml");
      boolean initConfigs = initConfigs();
      boolean startServer = startServer();
      boolean initGameData = GameDBManager.initGameDataFromDB();
      if ((!initConfigs || !startServer || !initGameData) && !Global.isAllowError()) {
         System.err.println("exit ......");
         System.exit(0);
      }

      Runtime.getRuntime().addShutdownHook(new ShutdownHook());
   }
}
