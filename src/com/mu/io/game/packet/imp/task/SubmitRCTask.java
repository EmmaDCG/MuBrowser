package com.mu.io.game.packet.imp.task;

import com.mu.config.MessageText;
import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class SubmitRCTask extends ReadAndWritePacket {
   public SubmitRCTask(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      boolean more = this.readBoolean();
      Player player = this.getPlayer();
      PlayerTaskManager ptm = player.getTaskManager();
      more = false;
      ptm.submitRC(more);
   }

   public static void sendMsgResult(Player player, int multiple, long exp, int money) {
      try {
         SubmitRCTask packet = new SubmitRCTask(40007, (byte[])null);
         packet.writeBoolean(true);
         packet.writeUTF(String.format(MessageText.getText(6014), multiple));
         packet.writeDouble((double)exp);
         packet.writeInt(money);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
