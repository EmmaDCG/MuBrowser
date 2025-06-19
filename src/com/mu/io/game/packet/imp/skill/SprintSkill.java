package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.Creature;
import com.mu.io.game.packet.WriteOnlyPacket;

public class SprintSkill extends WriteOnlyPacket {
   public SprintSkill() {
      super(30010);
   }

   public static void sendToClient(Creature owner, int endX, int endY) {
      try {
         SprintSkill ss = new SprintSkill();
         ss.writeByte(owner.getType());
         ss.writeDouble((double)owner.getID());
         ss.writeInt(endX);
         ss.writeInt(endY);
         owner.getMap().sendPacketToAroundPlayer(ss, owner.getActualPosition());
         ss.destroy();
         ss = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
