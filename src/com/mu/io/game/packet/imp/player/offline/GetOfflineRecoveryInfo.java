package com.mu.io.game.packet.imp.player.offline;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.offline.DunRecoveryInfo;
import com.mu.game.model.unit.player.offline.OfflineManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;

public class GetOfflineRecoveryInfo extends ReadAndWritePacket {
   public GetOfflineRecoveryInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GetOfflineRecoveryInfo() {
      super(10151, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      pushAllRecoveryInfo(player, this);
   }

   private static void pushAllRecoveryInfo(Player player, WriteOnlyPacket packet) {
      try {
         OfflineManager manager = player.getOffLineManager();
         HashMap infoMap = OfflineManager.getInfoMap();
         packet.writeByte(infoMap.size());
         Iterator it = infoMap.values().iterator();

         while(it.hasNext()) {
            DunRecoveryInfo info = (DunRecoveryInfo)it.next();
            packet.writeByte(info.getDunId());
            packet.writeUTF(info.getName());
            packet.writeByte(manager.getCanRecoverTimes(info.getDunId()));
            packet.writeInt(info.getIngot());
            packet.writeInt(info.getBindIngot());
         }

         player.writePacket(packet);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void pushAllRecoveryInfo(Player player) {
      GetOfflineRecoveryInfo gi = new GetOfflineRecoveryInfo();
      pushAllRecoveryInfo(player, gi);
      gi.destroy();
      gi = null;
   }
}
