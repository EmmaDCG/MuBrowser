package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.DropDBManager;
import com.mu.game.model.drop.model.KillRecord;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.ListPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class InitFirstkill extends ReadAndWritePacket {
   public InitFirstkill(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitFirstkill(HashMap killMap) {
      super(59004, (byte[])null);

      try {
         this.writeShort(killMap.size());
         Iterator it = killMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            this.writeInt(((Integer)entry.getKey()).intValue());
            this.writeInt(((Integer)entry.getValue()).intValue());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void addFirstKill(ListPacket lp, long roleID) {
      try {
         HashMap killMap = DropDBManager.searchKillNumber(roleID);
         if (killMap != null && killMap.size() > 0) {
            InitFirstkill fk = new InitFirstkill(killMap);
            lp.addPacket(fk);
            fk.destroy();
            killMap.clear();
         }

         killMap = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int size = this.readShort();

      for(int i = 1; i <= size; ++i) {
         int templateID = this.readInt();
         int killNumber = this.readInt();
         player.getDropManager().loadKillRecord(new KillRecord(templateID, killNumber));
      }

   }
}
