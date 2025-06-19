package com.mu.io.game.packet.imp.spiritOfWar;

import com.mu.game.model.item.Item;
import com.mu.game.model.spiritOfWar.SpiritManager;
import com.mu.game.model.spiritOfWar.SpiritTools;
import com.mu.game.model.spiritOfWar.refine.RefineData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SpiritRefine extends ReadAndWritePacket {
   public static final int Type_Ingot = 0;
   long exp = 0L;

   public SpiritRefine(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public SpiritRefine() {
      super(20607, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      SpiritManager manager = player.getSpiritManager();
      int type = this.readByte();
      int result = 1;
      if (type == 0) {
         result = manager.refineByIngot(false);
      } else {
         int size = this.readByte();
         if (size < 1) {
            return;
         }

         HashMap IDMap = new HashMap();

         for(int i = 0; i < size; ++i) {
            long id = (long)this.readDouble();
            IDMap.put(id, true);
         }

         List itemList = new ArrayList();
         result = this.canRefine(player, IDMap, itemList);
         if (result == 1) {
            result = player.getItemManager().deleteItemList(itemList, 40).getResult();
         }

         if (result == 1) {
            manager.addExp(this.exp, false);
         }

         IDMap.clear();
         IDMap = null;
         itemList.clear();
         itemList = null;
      }

      if (result == 1) {
         this.writeByte(type);
         player.writePacket(this);
      } else if (result != 23310) {
         SystemMessage.writeMessage(player, result);
      }

   }

   private int canRefine(Player player, HashMap IDMap, List itemList) {
      if (IDMap.size() < 1) {
         return 23307;
      } else {
         int result = player.getSpiritManager().canRefine();
         if (result != 1) {
            return result;
         } else {
            Item item;
            for(Iterator var6 = IDMap.keySet().iterator(); var6.hasNext(); this.exp += (long)RefineData.getRefineExp(item, 0)) {
               Long id = (Long)var6.next();
               item = player.getBackpack().getItemByID(id.longValue());
               if (item == null || item.getCount() < 1) {
                  return 3002;
               }

               result = SpiritTools.canRefine(item);
               if (result != 1) {
                  return result;
               }

               itemList.add(item);
            }

            return 1;
         }
      }
   }

   public static void sendToClient(Player player, int type) {
      try {
         SpiritRefine sr = new SpiritRefine();
         sr.writeByte(type);
         player.writePacket(sr);
         sr.destroy();
         sr = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
