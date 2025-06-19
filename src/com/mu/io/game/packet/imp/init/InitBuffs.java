package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.BuffDBManager;
import com.mu.game.model.unit.buff.BuffManager;
import com.mu.game.model.unit.buff.model.BuffDBEntry;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.ListPacket;
import java.util.Iterator;
import java.util.List;

public class InitBuffs extends ReadAndWritePacket {
   public InitBuffs(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static void initBuffPackets(ListPacket lp, long roleID) {
      try {
         List buffList = BuffDBManager.searchBuffs(roleID);
         if (buffList != null && buffList.size() > 0) {
            WriteOnlyPacket ii = new InitBuffs(buffList);
            lp.addPacket(ii);
            ii.destroy();
            buffList.clear();
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public InitBuffs(List entries) {
      super(59007, (byte[])null);

      try {
         this.writeByte(entries.size());
         Iterator var3 = entries.iterator();

         while(var3.hasNext()) {
            BuffDBEntry entry = (BuffDBEntry)var3.next();
            this.writeInt(entry.getBuffModelID());
            this.writeShort(entry.getLevel());
            this.writeLong(entry.getDuration());
            this.writeUTF(entry.getPropStr());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      BuffManager manager = player.getBuffManager();
      int size = this.readByte();

      for(int i = 0; i < size; ++i) {
         BuffDBEntry entry = new BuffDBEntry();
         entry.setBuffModelID(this.readInt());
         entry.setLevel(this.readShort());
         entry.setDuration(this.readLong());
         entry.setPropStr(this.readUTF());
         manager.loadBuffFromDB(entry);
      }

   }
}
