package com.mu.io.game.packet.imp.sys;

import com.mu.config.MessageText;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemColor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.CommonRegPattern;
import java.util.regex.Matcher;

public class RightMessage extends WriteOnlyPacket {
   public RightMessage() {
      super(1004);
   }

   public static void pushRightMessage(Player player, String msg) {
      try {
         RightMessage rm = new RightMessage();
         rm.writeUTF(msg);
         player.writePacket(rm);
         rm.destroy();
         rm = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void pushRightMessage(Player player, int msgID, Object... params) {
      try {
         String msg = MessageText.getText(msgID);
         if (msg == null || msg.isEmpty()) {
            msg = "Meesage Id = " + msgID;
         }

         StringBuffer sb = new StringBuffer();
         Matcher m = CommonRegPattern.PATTERN_MSG_PARAM.matcher(msg);

         for(int i = 0; params != null && m.find() && i < params.length; ++i) {
            m.appendReplacement(sb, params[i].toString());
         }

         m.appendTail(sb);
         msg = sb.toString();
         RightMessage rm = new RightMessage();
         rm.writeUTF(msg);
         player.writePacket(rm);
         rm.destroy();
         rm = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static void pushItemRightMessage(Player player, Item item) {
      try {
         RightMessage rm = new RightMessage();
         String s = MessageText.getText(20004).replace("%c%", ItemColor.find(item.getQuality()).getColor()).replace("%n%", item.getName());
         rm.writeUTF(s);
         player.writePacket(rm);
         rm.destroy();
         rm = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
