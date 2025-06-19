package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class LockTransaction extends WriteOnlyPacket {
   public LockTransaction() {
      super(13015);
   }

   public static void lockTransaction(Player owner, Player target) {
      try {
         LockTransaction lt = new LockTransaction();
         lt.writeByte(1);
         owner.writePacket(lt);
         lt.destroy();
         lt = null;
         lt = new LockTransaction();
         lt.writeByte(2);
         target.writePacket(lt);
         lt.destroy();
         lt = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
