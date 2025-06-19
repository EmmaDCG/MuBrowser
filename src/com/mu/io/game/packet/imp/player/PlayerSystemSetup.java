package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.SystemSetup;
import com.mu.io.game.packet.ReadAndWritePacket;

public class PlayerSystemSetup extends ReadAndWritePacket {
   public PlayerSystemSetup(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public PlayerSystemSetup() {
      super(10011, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      SystemSetup setup = player.getSystemSetup();
      setup.setAutoInTeamWhenBeInvite(this.readBoolean());
      setup.setAutoInTeamWhenBeApply(this.readBoolean());
      setup.setShowPlayer(this.readBoolean());
      setup.setShowMonster(this.readBoolean());
      setup.setShowPet(this.readBoolean());
      setup.setShowEffect(this.readBoolean());
      pushSetup(player);
   }

   public static void pushSetup(Player player) {
      try {
         SystemSetup setup = player.getSystemSetup();
         PlayerSystemSetup ps = new PlayerSystemSetup();
         ps.writeBoolean(setup.isAutoInTeamWhenBeInvite());
         ps.writeBoolean(setup.isAutoInTeamWhenBeApply());
         ps.writeBoolean(setup.isShowPlayer());
         ps.writeBoolean(setup.isShowMonster());
         ps.writeBoolean(setup.isShowPet());
         ps.writeBoolean(setup.isShowEffect());
         player.writePacket(ps);
         ps.destroy();
         ps = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
