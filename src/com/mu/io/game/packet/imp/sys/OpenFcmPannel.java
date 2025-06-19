package com.mu.io.game.packet.imp.sys;

import com.mu.config.Global;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.fcm.FcmManager;
import com.mu.io.game.packet.ReadAndWritePacket;

public class OpenFcmPannel extends ReadAndWritePacket {
   public OpenFcmPannel(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public OpenFcmPannel() {
      super(1002, (byte[])null);
   }

   public static void open(Player player, String msg1, String msg2, String msg3) {
      try {
         OpenFcmPannel op = new OpenFcmPannel();
         op.writeUTF(msg1);
         op.writeUTF(msg2);
         op.writeUTF(msg3);
         player.writePacket(op);
         op.destroy();
         op = null;
         FcmManager.getPushInfo(player.getID()).setLastPushTime(System.currentTimeMillis());
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void process() throws Exception {
      OpenUrl.open(this.getPlayer(), Global.getFcmUrl());
   }
}
