package com.mu.game.model.unit.player.module;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.player.PlayerModulePacket;

public abstract class PlayerModule {
   private int id;

   public PlayerModule(int id) {
      this.id = id;
   }

   public abstract void init(Player var1, PlayerModulePacket var2);

   public abstract byte[] getData(long var1);

   public PlayerModulePacket getInitPlayerPacket(long rid) {
      PlayerModulePacket packet = new PlayerModulePacket();

      try {
         packet.writeShort(this.id);
         packet.writeBytes(this.getData(rid));
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return packet;
   }

   public int getID() {
      return this.id;
   }
}
