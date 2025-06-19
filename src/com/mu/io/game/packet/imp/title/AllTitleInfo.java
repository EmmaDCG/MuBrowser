package com.mu.io.game.packet.imp.title;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.title.Title;
import com.mu.game.model.unit.player.title.TitleManager;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class AllTitleInfo extends WriteOnlyPacket {
   public AllTitleInfo() {
      super(15002);
   }

   public static void pushInfo(Player player) {
      try {
         TitleManager tm = player.getTitleManager();
         ConcurrentHashMap map = tm.getTitleMap();
         AllTitleInfo ti = new AllTitleInfo();
         ti.writeByte(map.size());
         Iterator it = map.values().iterator();

         while(it.hasNext()) {
            Title title = (Title)it.next();
            ti.writeByte(title.getId());
            ti.writeDouble((double)title.getExpiredTime());
         }

         HashMap lightMap = tm.getAllLightAttr();
         ti.writeByte(lightMap.size());
         it = lightMap.values().iterator();

         while(it.hasNext()) {
            int[] in = (int[])it.next();
            ti.writeShort(in[0]);
            ti.writeDouble((double)in[1]);
            ti.writeBoolean(in[2] == 1);
         }

         ti.writeByte(tm.getEquipId());
         player.writePacket(ti);
         ti.destroy();
         ti = null;
         lightMap.clear();
         it = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
