package com.mu.io.game.packet.imp.material;

import com.mu.game.model.unit.material.Material;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class ChangeMaterialClickStatus extends WriteOnlyPacket {
   public ChangeMaterialClickStatus(Material m, boolean b) {
      super(10702);

      try {
         this.writeShort(1);
         this.writeDouble((double)m.getID());
         this.writeBoolean(b);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public ChangeMaterialClickStatus(ArrayList list, boolean b) {
      super(10702);

      try {
         this.writeShort(list.size());
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            Material m = (Material)var4.next();
            this.writeDouble((double)m.getID());
            this.writeBoolean(b);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
