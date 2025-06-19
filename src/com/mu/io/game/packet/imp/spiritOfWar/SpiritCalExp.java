package com.mu.io.game.packet.imp.spiritOfWar;

import com.mu.game.model.item.Item;
import com.mu.game.model.spiritOfWar.SpiritTools;
import com.mu.game.model.spiritOfWar.refine.RefineData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.HashMap;
import java.util.Iterator;

public class SpiritCalExp extends ReadAndWritePacket {
   public SpiritCalExp(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int size = this.readByte();
      HashMap ids = new HashMap();

      int result;
      for(result = 0; result < size; ++result) {
         long id = (long)this.readDouble();
         ids.put(id, Integer.valueOf(1));
      }

      result = 1;
      int exp = 0;

      Item item;
      for(Iterator var8 = ids.keySet().iterator(); var8.hasNext(); exp += RefineData.getRefineExp(item, 0)) {
         long itemID = ((Long)var8.next()).longValue();
         item = player.getBackpack().getItemByID(itemID);
         if (item == null) {
            result = 3002;
            break;
         }

         result = SpiritTools.canRefine(item);
         if (result != 1) {
            break;
         }
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

      this.writeDouble((double)exp);
      player.writePacket(this);
      ids.clear();
      ids = null;
   }
}
