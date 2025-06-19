package com.mu.io.game.packet.imp.monster;

import com.mu.io.game.packet.WriteOnlyPacket;

public class ForceAttackMonster extends WriteOnlyPacket {
   public ForceAttackMonster(int templateId) {
      super(10301);

      try {
         this.writeInt(templateId);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
