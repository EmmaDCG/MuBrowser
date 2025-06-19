package com.mu.io.game.packet.imp.map;

import com.mu.db.manager.PlayerDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.RoleInfo;
import com.mu.game.plat.LoginUtil;
import com.mu.io.game.packet.imp.sys.ClientConfig;
import com.mu.io.game.packet.imp.sys.ListPacket;

class enterMapThread implements Runnable {
   private static ListPacket lp = null;
   private static ClientConfig mapConfig = null;
   Player player;

   static {
      try {
         lp = ListPacket.forClient();
         mapConfig = ClientConfig.getConfig();
         lp.addPacket(mapConfig);
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }

   enterMapThread(Player player) {
      this.player = player;
   }

   private void writeClientConfig(Player player) {
      try {
         player.writePacket(lp);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void run() {
      this.writeClientConfig(this.player);

      try {
         RoleInfo info = PlayerDBManager.getRoleInfo(this.player.getID());
         if (info == null) {
            this.player.destroy();
            return;
         }

         LoginUtil.initRole(this.player, info, 1);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.player = null;
   }
}
