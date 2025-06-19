package com.mu.io.game.packet.imp.market;

import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.market.MarketManager;
import com.mu.game.model.market.record.MarketRecord;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MarketRecords extends ReadAndWritePacket {
   public MarketRecords(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public MarketRecords() {
      super(20406, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      List recordList = MarketManager.getRecordList(player.getID());
      this.setData(recordList, player);
      player.writePacket(this);
   }

   public void setData(List recordList, Player player) throws Exception {
      if (recordList == null) {
         this.writeByte(0);
      } else {
         this.writeByte(recordList.size());

         MarketRecord record;
         for(Iterator var4 = recordList.iterator(); var4.hasNext(); this.writeDouble((double)record.getTransactionTime())) {
            record = (MarketRecord)var4.next();
            GetItemStats.writeItem(record.getItem(), this);
            if (record.getBuyerID() == player.getID()) {
               ChatProcess.writeBuyRecord(record, this);
            } else {
               ChatProcess.writeSellRecord(record, this);
            }
         }
      }

   }

   public static void sendToClient(Player player, MarketRecord record) {
      try {
         List records = new ArrayList();
         records.add(record);
         MarketRecords mr = new MarketRecords();
         mr.setData(records, player);
         player.writePacket(mr);
         mr.destroy();
         mr = null;
         records.clear();
         records = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
