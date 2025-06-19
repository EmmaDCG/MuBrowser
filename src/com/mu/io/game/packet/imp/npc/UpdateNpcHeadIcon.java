package com.mu.io.game.packet.imp.npc;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UpdateNpcHeadIcon extends WriteOnlyPacket {
   public UpdateNpcHeadIcon() {
      super(10407);
   }

   public static void updatePlayerSeeNpcHeadIcon(Player player, long npcId) {
      Map map = player.getMap();
      Npc npc = map.getNpc(npcId);
      writeNpcHeadIcon(player, true, npc);
   }

   public static UpdateNpcHeadIcon writeNpcHeadIcon(Player player, Collection npcCollection) {
      UpdateNpcHeadIcon update = null;

      try {
         HashMap npcMap = new HashMap();
         Iterator it = npcCollection.iterator();

         while(it.hasNext()) {
            Npc npc = (Npc)it.next();
            int icon = npc.queryPlayerSeeHeader(player);
            if (icon != 0) {
               npcMap.put(npc.getID(), icon);
            }
         }

         if (npcMap.isEmpty()) {
            return update;
         }

         update = new UpdateNpcHeadIcon();
         update.writeByte(npcMap.size());
         it = npcMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            update.writeDouble((double)((Long)entry.getKey()).longValue());
            update.writeShort(((Integer)entry.getValue()).intValue());
         }

         npcMap.clear();
         npcMap = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return update;
   }

   public static void writeNpcHeadIcon(Player player, boolean hasNormal, Npc npc) {
      if (npc != null) {
         try {
            int icon = npc.queryPlayerSeeHeader(player);
            if (hasNormal || icon != 0) {
               UpdateNpcHeadIcon update = new UpdateNpcHeadIcon();
               update.writeByte(1);
               update.writeDouble((double)npc.getID());
               update.writeShort(icon);
               player.writePacket(update);
               update.destroy();
               update = null;
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      }
   }
}
