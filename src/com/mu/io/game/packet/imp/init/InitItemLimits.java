package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.ItemDBManager;
import com.mu.game.model.item.operation.ItemManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.ListPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class InitItemLimits extends ReadAndWritePacket {
   public InitItemLimits(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitItemLimits(HashMap itemlimits) {
      super(59006, (byte[])null);

      try {
         this.writeShort(itemlimits.size());
         Iterator var3 = itemlimits.entrySet().iterator();

         while(var3.hasNext()) {
            Entry entry = (Entry)var3.next();
            int modelID = ((Integer)entry.getKey()).intValue();
            int limitCount = ((Integer)entry.getValue()).intValue();
            this.writeInt(modelID);
            this.writeInt(limitCount);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void addItemLimitPacket(long roleID, ListPacket lp) {
      try {
         HashMap itemLimits = ItemDBManager.searchItemLimit(roleID);
         if (itemLimits != null && itemLimits.size() > 0) {
            InitItemLimits iil = new InitItemLimits(itemLimits);
            lp.addPacket(iil);
            iil.destroy();
            iil = null;
            itemLimits.clear();
         }

         itemLimits = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      ItemManager manager = player.getItemManager();
      int size = this.readShort();

      for(int i = 0; i < size; ++i) {
         int modelID = this.readInt();
         int limitCount = this.readInt();
         manager.loadItemLimit(modelID, limitCount);
      }

   }
}
