package com.mu.executor.imp.mail;

import com.mu.db.manager.MailDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReadMailExecutor extends Executable {
   public ReadMailExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      MailDBManager.readMail(packet.readLong());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long id = ((Long)obj[0]).longValue();
      packet.writeLong(id);
   }

   public static void readMail(Player player, long mailId) {
      WriteOnlyPacket packet = Executor.ReadMail.toPacket(mailId);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
