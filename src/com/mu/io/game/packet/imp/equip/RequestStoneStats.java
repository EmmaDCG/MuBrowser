package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.newStone.StoneDataManager;
import com.mu.game.model.equip.newStone.StoneStatAtom;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.Iterator;
import java.util.List;

public class RequestStoneStats extends ReadAndWritePacket {
   public RequestStoneStats(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long stoneID = (long)this.readDouble();
      Item stone = player.getBackpack().getItemByID(stoneID);
      if (stone == null) {
         SystemMessage.writeMessage(player, 3002);
      } else {
         List statList = StoneDataManager.getStoneStats(stone.getModelID());
         if (statList == null) {
            SystemMessage.writeMessage(player, 3043);
         } else {
            StringBuffer sb = new StringBuffer();
            Iterator var8 = statList.iterator();

            while(var8.hasNext()) {
               StoneStatAtom atom = (StoneStatAtom)var8.next();
               sb.append(StoneDataManager.assemStoneStat(atom.getEquipStat()));
               sb.append("#b");
            }

            this.writeDouble((double)stoneID);
            String s = sb.toString();
            this.writeUTF(s);
            player.writePacket(this);
            statList = null;
         }
      }
   }
}
