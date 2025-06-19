package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.excellent.ExcellentCountEffect;
import com.mu.game.model.item.container.imp.Equipment;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;

public class ExcellentCountDes extends WriteOnlyPacket {
   public ExcellentCountDes() {
      super(20232);
   }

   public static void sendToClient(Player player) {
      try {
         ExcellentCountDes ecd = new ExcellentCountDes();
         Equipment equipment = player.getEquipment();
         int excellentCount = equipment.getTotalExcellentCount();
         ExcellentCountEffect curEffect = ExcellentCountEffect.getActiveEffect(excellentCount);
         ecd.writeByte(excellentCount);
         ecd.writeByte(curEffect.getShowCountList().size());
         Iterator var6 = curEffect.getShowCountList().iterator();

         while(var6.hasNext()) {
            Integer count = (Integer)var6.next();
            ExcellentCountEffect effect = ExcellentCountEffect.getEffect(count.intValue());
            String s = effect.getCount() <= excellentCount ? effect.getActiveStr() : effect.getInactiveStr();
            ecd.writeUTF(s);
         }

         player.writePacket(ecd);
         ecd.destroy();
         ecd = null;
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }
}
