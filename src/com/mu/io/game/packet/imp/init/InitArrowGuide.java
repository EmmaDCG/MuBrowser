package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.PlayerDBManager;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class InitArrowGuide extends ReadAndWritePacket {
   public InitArrowGuide(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static InitArrowGuide createGuide(long rid) {
      InitArrowGuide guide = new InitArrowGuide(10023, (byte[])null);
      ArrayList list = PlayerDBManager.getGuideList(rid);

      try {
         guide.writeByte(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            int[] in = (int[])var5.next();
            guide.writeByte(in[0]);
            guide.writeByte(in[1]);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      list.clear();
      return guide;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      ArrowGuideManager guide = player.getArrowGuideManager();
      if (guide != null) {
         int size = this.readUnsignedByte();

         for(int i = 0; i < size; ++i) {
            int id = this.readUnsignedByte();
            int times = this.readUnsignedByte();
            guide.putGuide(id, times);
         }

      }
   }
}
