package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class SummonOrEnterBoss extends ReadAndWritePacket {
   public SummonOrEnterBoss(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public SummonOrEnterBoss() {
      super(10643, (byte[])null);
   }

   public void process() throws Exception {
      int bossId = this.readByte();
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         gang.doOperation(player, 20, Integer.valueOf(bossId));
      }

   }

   public static void writeSummon(Gang gang, int bossId, int leftTimes, int time) {
      try {
         SummonOrEnterBoss st = new SummonOrEnterBoss();
         st.writeByte(bossId);
         st.writeByte(leftTimes);
         st.writeBoolean(true);
         st.writeInt(time);
         gang.broadcast(st);
         st.destroy();
         st = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
