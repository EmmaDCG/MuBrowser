package com.mu.io.game.packet.imp.buff;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.Buff;
import com.mu.io.game.packet.WriteOnlyPacket;

public class BuffUpdateDydata extends WriteOnlyPacket {
   public BuffUpdateDydata(Creature owner, Buff buff) {
      super(31002);

      try {
         this.writeByte(owner.getType());
         this.writeDouble((double)owner.getID());
         this.writeInt(buff.getModelID());
         AddBuff.writeDyDatas(buff, this);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Creature owner, Buff buff) {
      BuffUpdateDydata ud = new BuffUpdateDydata(owner, buff);
      owner.getMap().sendPacketToAroundPlayer(ud, owner.getActualPosition());
      ud.destroy();
      ud = null;
   }
}
