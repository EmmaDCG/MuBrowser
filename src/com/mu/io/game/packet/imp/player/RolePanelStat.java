package com.mu.io.game.packet.imp.player;

import com.mu.game.model.properties.NumConversion;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.Map.Entry;

public class RolePanelStat extends WriteOnlyPacket {
   public RolePanelStat() {
      super(10206);
   }

   public static void sendToClient(Player player) {
      RolePanelStat rps = new RolePanelStat();

      try {
         HashMap statMap = StatList2Client.getPanelStatMap();
         int size = statMap.size();
         if (statMap.containsKey(Integer.valueOf(1))) {
            --size;
         }

         rps.writeByte(size);
         Iterator it = statMap.entrySet().iterator();

         label56:
         while(true) {
            Entry entry;
            int sortID;
            do {
               if (!it.hasNext()) {
                  NumConversion conversion = NumConversion.getNumConversion(player.getProfessionID());
                  HashMap affectMaps = conversion.getFirstAffectMaps();
                  if (affectMaps == null) {
                     rps.writeByte(0);
                     break label56;
                  } else {
                     rps.writeByte(affectMaps.size());
                     List firstList = StatList2Client.getFirstLevelSatList();
                     Iterator var17 = firstList.iterator();

                     while(true) {
                        StatEnum stat;
                        SortedMap entry2;
                        do {
                           if (!var17.hasNext()) {
                              break label56;
                           }

                           stat = (StatEnum)var17.next();
                           entry2 = (SortedMap)affectMaps.get(stat);
                        } while(entry2 == null);

                        rps.writeShort(stat.getStatId());
                        rps.writeByte(entry2.size());
                        Iterator var11 = entry2.keySet().iterator();

                        while(var11.hasNext()) {
                           StatEnum sStat = (StatEnum)var11.next();
                           rps.writeShort(sStat.getStatId());
                        }
                     }
                  }
               }

               entry = (Entry)it.next();
               sortID = ((Integer)entry.getKey()).intValue();
            } while(sortID == 1);

            String name = StatList2Client.getPanelStatName(sortID);
            rps.writeUTF(name);
            List statList = (List)entry.getValue();
            rps.writeByte(statList.size());
            Iterator var10 = statList.iterator();

            while(var10.hasNext()) {
               StatEnum stat = (StatEnum)var10.next();
               rps.writeShort(stat.getStatId());
            }
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }

      player.writePacket(rps);
      rps.destroy();
      rps = null;
   }
}
