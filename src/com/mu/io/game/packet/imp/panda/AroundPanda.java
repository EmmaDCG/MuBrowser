package com.mu.io.game.packet.imp.panda;

import com.mu.game.model.panda.Panda;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import java.util.Iterator;
import java.util.List;

public class AroundPanda extends WriteOnlyPacket {
   public AroundPanda(Player player, Panda panda) {
      super(45000);

      try {
         this.writeShort(1);
         this.writePandaDetail(player, panda);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public AroundPanda(Player player, List list) {
      super(45000);

      try {
         if (list == null || list.isEmpty()) {
            this.writeShort(0);
            return;
         }

         this.writeShort(list.size());
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            Panda panda = (Panda)var4.next();
            this.writePandaDetail(player, panda);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void writePandaDetail(Player player, Panda panda) throws Exception {
      this.writeDouble((double)panda.getID());
      this.writeInt(panda.getX());
      this.writeInt(panda.getY());
      this.writeShort(panda.getModelId());
      this.writeDouble((double)panda.getOwner().getID());
      this.writeShort(1);
      PlayerAttributes.writeStat(StatEnum.SPEED, (long)player.getStatValue(StatEnum.SPEED), this);
   }
}
