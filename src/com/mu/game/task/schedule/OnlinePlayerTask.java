package com.mu.game.task.schedule;

import com.mu.config.Global;
import com.mu.db.manager.GameDBManager;
import com.mu.db.manager.GlobalLogDBManager;
import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executor;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.Time;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.HashMap;
import java.util.Iterator;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlinePlayerTask extends ScheduleTask {
   private static final Logger logger = LoggerFactory.getLogger(OnlinePlayerTask.class);
   private static final int period = 60;
   private static final int SaveOnlineRate = 5;
   private static int times = 0;

   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 60000L, 60000L);
   }

   public void doLocalTask() {
      long now = System.currentTimeMillis();
      PlayerDBManager.savePlayerVolatileInfo();
      ++times;
      int num = CenterManager.getOnlinePlayerSize();
      if (times % 5 == 0) {
         this.saveGameOnline(now, num);
         Global.getLoginParser().doOnlineNum(num);
         GlobalLogDBManager.saveRoleOnlineLog(300);
      }

      logger.info("save player cost time {},player size is {}", System.currentTimeMillis() - now, num);
   }

   private void saveGameOnline(long time, int num) {
      if (num > 0) {
         int ipNum = this.getIpNumber();
         String lastStr = Time.getTimeStr(time - 60000L);
         int newUser = 0;
         if (!Global.isInterServiceServer()) {
            newUser = GameDBManager.getNewUser(lastStr);
         }

         GlobalLogDBManager.saveOnline(num, ipNum, Time.getTimeStr(time), newUser, Global.getServerID());
      }

   }

   private int getIpNumber() {
      HashMap map = new HashMap();
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         Channel channel = player.getChannel();
         if (channel != null) {
            String ip = CenterManager.getIpByChannel(channel);
            if (ip != null && !map.containsKey(ip)) {
               map.put(ip, true);
            }
         }
      }

      int size = map.size();
      map.clear();
      map = null;
      return size;
   }

   public void doInterTask() {
      Iterator it = CenterManager.getAllPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player p = (Player)it.next();

         try {
            WriteOnlyPacket packet = Executor.SaveOnlinePlayer.toPacket(p);
            p.writePacket(packet);
            packet.destroy();
            packet = null;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }
}
