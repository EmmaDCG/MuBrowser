package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class GangDonate extends ReadAndWritePacket {
   public GangDonate(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GangDonate(boolean success) {
      super(10635, (byte[])null);

      try {
         this.writeBoolean(success);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void writeFail(Player player) {
      GangDonate gd = new GangDonate(false);
      player.writePacket(gd);
      gd.destroy();
      gd = null;
   }

   public static void writeSuccess(Gang gang) {
      try {
         GangDonate gd = new GangDonate(true);
         gd.writeDouble((double)gang.getContribution());
         gd.writeDouble((double)gang.getHisContribution());
         gang.broadcast(gd);
         gd.destroy();
         gd = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      int num = this.readInt();
      if (gang == null) {
         SystemMessage.writeMessage(player, 9034);
         writeFail(player);
      } else {
         gang.doOperation(player, 17, num);
      }
   }
}
