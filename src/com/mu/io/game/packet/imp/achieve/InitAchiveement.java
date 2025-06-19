package com.mu.io.game.packet.imp.achieve;

import com.mu.db.manager.AchievementDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.achievement.AchievementManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class InitAchiveement extends ReadAndWritePacket {
   public InitAchiveement(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitAchiveement() {
      super(10047, (byte[])null);
   }

   public static InitAchiveement createInitAchiveement(long rid) {
      InitAchiveement ia = new InitAchiveement();
      ArrayList list = AchievementDBManager.getAchievement(rid);

      try {
         ia.writeShort(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            int[] in = (int[])var5.next();
            ia.writeShort(in[0]);
            ia.writeInt(in[1]);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      list.clear();
      list = null;
      return ia;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      AchievementManager manager = player.getAchievementManager();
      int size = this.readShort();

      for(int i = 0; i < size; ++i) {
         manager.addAchievement(this.readShort(), this.readInt());
      }

   }
}
