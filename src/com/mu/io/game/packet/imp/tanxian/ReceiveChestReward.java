package com.mu.io.game.packet.imp.tanxian;

import com.mu.game.dungeon.imp.discovery.DiscoveryMap;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ReceiveChestReward extends ReadAndWritePacket {
   public ReceiveChestReward(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ReceiveChestReward() {
      super(48007, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Map map = player.getMap();
      if (map != null && map instanceof DiscoveryMap) {
         DiscoveryMap dm = (DiscoveryMap)map;
         dm.rewardChest(player);
      }

   }

   public static void receiveSuccess(Player player) {
      try {
         ReceiveChestReward rr = new ReceiveChestReward();
         rr.writeBoolean(true);
         player.writePacket(rr);
         rr.destroy();
         rr = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
