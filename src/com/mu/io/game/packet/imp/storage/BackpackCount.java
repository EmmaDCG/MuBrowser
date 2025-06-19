package com.mu.io.game.packet.imp.storage;

import com.mu.game.model.item.container.imp.Backpack;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class BackpackCount extends ReadAndWritePacket {
   public BackpackCount(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public BackpackCount(int currentIndex, int curWaitIndex, int remainTime, int needTime) {
      super(20009, (byte[])null);

      try {
         this.writeByte(currentIndex);
         this.writeByte(curWaitIndex);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void sendToClient(Player player) {
      Backpack backpack = player.getBackpack();
      int currentIndex = backpack.getLimit() - 1;
      int curWaitIndex = backpack.getCurWaitOpenGrid() - 1;
      int cooledTime = backpack.getCurGridTime();
      int needTime = backpack.getNeedTime();
      cooledTime = needTime - cooledTime;
      BackpackCount bc = new BackpackCount(currentIndex, curWaitIndex, cooledTime, needTime);
      player.writePacket(bc);
      bc.destroy();
      bc = null;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      sendToClient(player);
   }
}
