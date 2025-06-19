package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class UnLockTransaction extends WriteOnlyPacket {
   public UnLockTransaction() {
      super(13017);
   }

   public static void unLockTransaction(Player owner, Player target) {
      try {
         UnLockTransaction lt = new UnLockTransaction();
         lt.writeByte(1);
         owner.writePacket(lt);
         lt.destroy();
         lt = null;
         SystemMessage.writeMessage(owner, 15015);
         lt = new UnLockTransaction();
         lt.writeByte(2);
         target.writePacket(lt);
         lt.destroy();
         lt = null;
         SystemMessage.writeMessage(target, owner.getName() + MessageText.getText(15016), 15016);
         lt = new UnLockTransaction();
         lt.writeByte(1);
         target.writePacket(lt);
         lt.destroy();
         lt = null;
         lt = new UnLockTransaction();
         lt.writeByte(2);
         owner.writePacket(lt);
         lt.destroy();
         lt = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
