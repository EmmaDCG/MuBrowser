package com.mu.io.game.packet.imp.sys;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import org.jboss.netty.channel.Channel;

public class SystemMessage extends WriteOnlyPacket {
   public SystemMessage(String msg) {
      super(1000);

      try {
         this.writeUTF(msg);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void writeMessage(Channel channel, String msg) {
      try {
         if (!msg.equals("")) {
            SystemMessage sm = new SystemMessage(msg);
            channel.write(sm.toBuffer());
            sm.destroy();
            sm = null;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void writeMessage(Player player, int msgID) {
      try {
         writeMessage(player, MessageText.getText(msgID), msgID);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void writeMessage(Player player, String msg, int msgId) {
      try {
         if (msg == null || msg.equals("")) {
            return;
         }

         SystemMessage sm = new SystemMessage(msg);
         player.writePacket(sm);
         sm.destroy();
         sm = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
