package com.mu.io.game.packet.imp.map;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstEnterMap extends WriteOnlyPacket {
   static Logger logger = LoggerFactory.getLogger(FirstEnterMap.class);

   public FirstEnterMap() {
      super(10105);
   }

   public void writeData(Player player) {
      try {
         this.writeUTF(getPayUrl(player));
         this.writeUTF(getHomeUrl(player));
         this.writeUTF(getForumUrl(player));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private static String getPayUrl(Player player) {
      return "";
   }

   private static String getHomeUrl(Player player) {
      return "";
   }

   private static String getForumUrl(Player player) {
      return "";
   }

   public static void sendToClient(Player player) {
      initialization(player);

      try {
         FirstEnterMap fnt = new FirstEnterMap();
         fnt.writeData(player);
         player.writePacket(fnt);
         fnt.destroy();
         fnt = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private static void initialization(Player player) {
   }
}
