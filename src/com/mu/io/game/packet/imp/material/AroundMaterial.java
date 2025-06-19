package com.mu.io.game.packet.imp.material;

import com.mu.game.model.unit.material.Material;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class AroundMaterial extends WriteOnlyPacket {
   public AroundMaterial(Material m, Player player) {
      super(10700);

      try {
         this.writeShort(1);
         this.writeDetail(m, player);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public AroundMaterial(ArrayList list, Player player) {
      super(10700);

      try {
         this.writeShort(list.size());
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            Material m = (Material)var4.next();
            this.writeDetail(m, player);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void writeDetail(Material m, Player player) throws Exception {
      this.writeDouble((double)m.getID());
      this.writeInt(m.getTemplateID());
      this.writeUTF(m.getName());
      this.writeInt(m.getX());
      this.writeInt(m.getY());
      this.writeShort(m.getModelId());
      this.writeBoolean(m.canClick(player));
   }
}
