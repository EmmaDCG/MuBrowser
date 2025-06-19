package com.mu.io.game.packet.imp.luckyTurnTabel;

import com.mu.game.model.luckyTurnTable.LuckyTurnTableManager;
import com.mu.game.model.luckyTurnTable.TurnTable;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.SortedMap;

public class RequestLuckyTurnTableInfo extends ReadAndWritePacket {
   public RequestLuckyTurnTableInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int time = LuckyTurnTableManager.getRemainTime();
      this.writeInt(time);
      SortedMap tableMap = LuckyTurnTableManager.getTableMap();
      this.writeByte(tableMap.size());
      Iterator var5 = tableMap.values().iterator();

      while(var5.hasNext()) {
         TurnTable table = (TurnTable)var5.next();
         this.writeByte(table.getTableID());
         this.writeShort(table.getSource());
         this.writeUTF(table.getName());
      }

      int remainCount = LuckyTurnTableManager.getRemainCount(player);
      this.writeByte(remainCount);
      int money = LuckyTurnTableManager.getNeedIngotByShow(player.getTurnTableCount() + 1);
      this.writeInt(money);
      this.writeUTF(LuckyTurnTableManager.ruleDes);
      player.writePacket(this);
   }
}
