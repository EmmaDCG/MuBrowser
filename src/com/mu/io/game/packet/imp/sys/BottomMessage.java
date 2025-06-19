package com.mu.io.game.packet.imp.sys;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import org.jboss.netty.channel.Channel;

public class BottomMessage extends WriteOnlyPacket {
   public BottomMessage() {
      super(1003);
   }

   public static void pushMessage(Player player, String msg, int msgId) {
      if (msg != null && !msg.equals("")) {
         if (player.getTimeLimit().canPushButtom(msgId)) {
            try {
               BottomMessage bm = new BottomMessage();
               bm.writeUTF(msg);
               player.writePacket(bm);
               bm.destroy();
               bm = null;
            } catch (Exception var4) {
               var4.printStackTrace();
            }

         }
      }
   }

   public static void pushMessage(Player player, int msgId) {
      try {
         String msg = MessageText.getText(msgId);
         pushMessage(player, msg, msgId);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void pushMessage(Channel channel, String msg) {
      try {
         try {
            BottomMessage bm = new BottomMessage();
            bm.writeUTF(msg);
            channel.write(bm.toBuffer());
            bm.destroy();
            bm = null;
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
