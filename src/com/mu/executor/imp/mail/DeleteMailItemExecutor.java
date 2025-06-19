package com.mu.executor.imp.mail;

import com.mu.db.manager.MailDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class DeleteMailItemExecutor extends Executable {
   public DeleteMailItemExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      MailDBManager.deleteMailItem(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      ArrayList list = (ArrayList)obj[0];
      packet.writeShort(list.size());
      Iterator var6 = list.iterator();

      while(var6.hasNext()) {
         long id = ((Long)var6.next()).longValue();
         packet.writeLong(id);
      }

   }

   public static void deleteItem(Player player, ArrayList mailId) {
      WriteOnlyPacket packet = Executor.DeleteMailItem.toPacket(mailId);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
