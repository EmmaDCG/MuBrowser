package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.star.StarSetModel;
import com.mu.game.model.item.container.imp.Equipment;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;

public class StarSetDes extends WriteOnlyPacket {
   public StarSetDes() {
      super(20222);
   }

   public static void sendToClient(Player player) {
      try {
         StarSetDes ssd = new StarSetDes();
         Equipment equipment = player.getEquipment();
         int starTotal = equipment.getTotalStar();
         StarSetModel curModel = StarSetModel.getActiveModel(starTotal);
         ssd.writeShort(starTotal);
         ssd.writeByte(curModel.getShowLevelList().size());
         Iterator var6 = curModel.getShowLevelList().iterator();

         while(var6.hasNext()) {
            Integer starLevel = (Integer)var6.next();
            StarSetModel model = StarSetModel.getActiveModel(starLevel.intValue());
            String s = model.getStarLevel() <= starTotal ? model.getActiveStr() : model.getInactiveStr();
            ssd.writeUTF(s);
         }

         player.writePacket(ssd);
         ssd.destroy();
         ssd = null;
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }
}
