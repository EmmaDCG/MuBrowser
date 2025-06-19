package com.mu.io.game.packet.imp.chat;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ChatSiLiao extends WriteOnlyPacket {
   public ChatSiLiao() {
      super(10503);
   }

   public static ChatSiLiao createGangPacket(Player player, String msg, byte[] data) {
      ChatSiLiao cs = new ChatSiLiao();

      try {
         cs.writeByte(2);
         cs.writeDouble((double)System.currentTimeMillis());
         cs.writeUTF(msg);
         cs.writeByte(0);
         if (data == null) {
            cs.writeShort(0);
         } else {
            cs.writeShort(data.length);
            cs.writeBytes(data);
         }

         cs.writeByte(2);
         cs.writeBoolean(false);
         cs.writeDouble((double)player.getID());
         cs.writeUTF(player.getName());
         cs.writeShort(player.getHeader());
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return cs;
   }

   public static ChatSiLiao createPersionalPacket(Player player, long targetId, String msg, byte[] data) {
      ChatSiLiao cs = new ChatSiLiao();

      try {
         cs.writeByte(2);
         cs.writeDouble((double)System.currentTimeMillis());
         cs.writeUTF(msg);
         cs.writeByte(0);
         if (data == null) {
            cs.writeShort(0);
         } else {
            cs.writeShort(data.length);
            cs.writeBytes(data);
         }

         cs.writeByte(1);
         cs.writeBoolean(false);
         cs.writeDouble((double)targetId);
         cs.writeUTF(player.getName());
         cs.writeShort(player.getHeader());
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return cs;
   }

   public static ChatSiLiao createPersionalPacket(long targetId, String msg, byte[] data) {
      ChatSiLiao cs = new ChatSiLiao();

      try {
         cs.writeByte(2);
         cs.writeDouble((double)System.currentTimeMillis());
         cs.writeUTF(msg);
         cs.writeByte(0);
         if (data == null) {
            cs.writeShort(0);
         } else {
            cs.writeShort(data.length);
            cs.writeBytes(data);
         }

         cs.writeByte(1);
         cs.writeBoolean(true);
         cs.writeDouble((double)targetId);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return cs;
   }
}
