package com.mu.io.game.packet.imp.luckyTurnTabel;

import com.mu.game.CenterManager;
import com.mu.game.model.luckyTurnTable.LuckyTurnTableManager;
import com.mu.game.model.luckyTurnTable.TurnTableRecord;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LuckyTurnTabelRecord extends ReadAndWritePacket {
   public LuckyTurnTabelRecord(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public LuckyTurnTabelRecord() {
      super(20612, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      List list = LuckyTurnTableManager.getAllRecordList();
      this.setData(list);
      player.writePacket(this);
      list.clear();
      list = null;
   }

   public void setData(List list) throws Exception {
      this.writeByte(list.size());
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         TurnTableRecord record = (TurnTableRecord)var3.next();
         this.writeDouble((double)record.getTime());
         this.writeDouble((double)record.getRoleID());
         this.writeUTF(record.getName());
         this.writeInt(record.getMultile());
         this.writeInt(record.getSuccessIngot());
      }

   }

   public static void sendToOther(Player player, TurnTableRecord record) {
      try {
         List list = new ArrayList();
         list.add(record);
         LuckyTurnTabelRecord ltt = new LuckyTurnTabelRecord();
         ltt.setData(list);
         Iterator it = CenterManager.getAllPlayerIterator();

         while(it.hasNext()) {
            Player p = (Player)it.next();
            if (p.getID() != player.getID()) {
               p.writePacket(ltt);
            }
         }

         ltt.destroy();
         ltt = null;
         list.clear();
         list = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
