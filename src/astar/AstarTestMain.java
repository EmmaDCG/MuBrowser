package astar;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.db.manager.GlobalDBManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AstarTestMain {
   public static AstarMainFrame mainFrame;
   private static final Logger logger = LoggerFactory.getLogger(AstarTestMain.class);

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
         return true;
      }
   }

   private static boolean initGlobalData() {
      return GlobalDBManager.initData();
   }

   public static void main(String[] args) {
      if (!initConfigs()) {
         System.exit(0);
      }

      mainFrame = new AstarMainFrame();
      mainFrame.launchFrame();
   }
}
