package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class ChangeApplyStatus extends ReadAndWritePacket {
   public ChangeApplyStatus(long gangId, boolean isApply) {
      super(10607, (byte[])null);

      try {
         this.writeDouble((double)gangId);
         this.writeBoolean(isApply);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public ChangeApplyStatus(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static void changeStatus(Player player, long gid, boolean b) {
      ChangeApplyStatus cs = new ChangeApplyStatus(gid, b);
      player.writePacket(cs);
      cs.destroy();
      cs = null;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player.getGang() != null) {
         SystemMessage.writeMessage(player, 9001);
      } else {
         long gangId = (long)this.readDouble();
         Gang gang = GangManager.getGang(gangId);
         if (gang == null) {
            SystemMessage.writeMessage(player, 9078);
         } else {
            boolean isApply = this.readBoolean();
            if (isApply) {
               gang.doOperation(player, 1);
            } else {
               gang.doOperation(player, 6);
            }

         }
      }
   }
}
