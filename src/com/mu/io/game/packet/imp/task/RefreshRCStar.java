package com.mu.io.game.packet.imp.task;

import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.List;

public class RefreshRCStar extends ReadAndWritePacket {
   public RefreshRCStar(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      PlayerTaskManager ptm = player.getTaskManager();
      ptm.refreshRCStar();
   }

   public static void sendResult(Player player, int star, List rewardList) {
      try {
         RefreshRCStar inform = new RefreshRCStar(40009, (byte[])null);
         inform.writeByte(star);
         TaskInform.writePacket_RewardData(inform, rewardList);
         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
