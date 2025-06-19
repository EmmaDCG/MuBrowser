package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestDuraRepairMoney extends ReadAndWritePacket {
   public RequestDuraRepairMoney(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long targetItemID = (long)this.readDouble();
      int size = this.readByte();
      List itemIDList = new ArrayList();

      for(int i = 0; i < size; ++i) {
         long itemID = (long)this.readDouble();
         itemIDList.add(itemID);
      }

      HashMap itemMap = new HashMap();
      int[] results = DuraRepair.canRepair(player, itemIDList, itemMap, targetItemID);
      this.writeInt(results[2]);
      this.writeInt(results[1]);
      if (results[0] != 1) {
         SystemMessage.writeMessage(player, results[0]);
      }

      player.writePacket(this);
      itemMap.clear();
      itemMap = null;
      itemIDList.clear();
      itemIDList = null;
   }
}
