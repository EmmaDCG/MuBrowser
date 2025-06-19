package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.DropDBManager;
import com.mu.game.model.drop.PlayerDropManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.ListPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class InitRoleDropCounts extends ReadAndWritePacket {
   public InitRoleDropCounts(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitRoleDropCounts(HashMap dropCounts) {
      super(59005, (byte[])null);

      try {
         this.writeShort(dropCounts.size());
         Iterator var3 = dropCounts.entrySet().iterator();

         while(var3.hasNext()) {
            Entry entry = (Entry)var3.next();
            this.writeInt(((Integer)entry.getKey()).intValue());
            this.writeInt(((Integer)entry.getValue()).intValue());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void addRoleDropCount(ListPacket lp, long roleID) {
      HashMap dropCounts = DropDBManager.searchRoleDropCounts(roleID);

      try {
         if (dropCounts != null && dropCounts.size() > 0) {
            InitRoleDropCounts rdc = new InitRoleDropCounts(dropCounts);
            lp.addPacket(rdc);
            dropCounts.clear();
         }

         dropCounts = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      PlayerDropManager dropManager = player.getDropManager();
      int size = this.readShort();

      for(int i = 0; i < size; ++i) {
         int dropID = this.readInt();
         int count = this.readInt();
         dropManager.loadPersonDrop(dropID, count);
      }

   }
}
