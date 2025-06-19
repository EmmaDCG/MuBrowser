package com.mu.io.game.packet.imp.magicItem;

import com.mu.game.CenterManager;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.item.box.magic.MagicRecord;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.List;

public class GetMagicItemRecord extends ReadAndWritePacket {
   public GetMagicItemRecord(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GetMagicItemRecord() {
      super(20028, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      List recordList = MagicRecord.getRecordList();
      this.setData(recordList);
      player.writePacket(this);
      recordList.clear();
      recordList = null;
   }

   public void setData(List recordList) throws Exception {
      if (recordList == null) {
         this.writeByte(0);
      } else {
         this.writeByte(recordList.size());
         Iterator var3 = recordList.iterator();

         while(var3.hasNext()) {
            MagicRecord record = (MagicRecord)var3.next();
            ChatProcess.writeNewLinkMessage(record.getDes(), record.getLink(), this);
         }
      }

   }

   public static void sendToClient(List recordList) {
      try {
         GetMagicItemRecord gmr = new GetMagicItemRecord();
         gmr.setData(recordList);
         Iterator it = CenterManager.getAllPlayerIterator();

         while(it.hasNext()) {
            Player p = (Player)it.next();
            p.writePacket(gmr);
         }

         gmr.destroy();
         gmr = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
