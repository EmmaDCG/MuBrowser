package com.mu.io.game.packet.imp.pkModel;

import com.mu.config.VariableConstant;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class SetPkMode extends ReadAndWritePacket {
   public SetPkMode(int mode) {
      super(32010, (byte[])null);

      try {
         this.writeByte(mode);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public SetPkMode(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int modeID = this.readByte();
      int result = this.canManulChange(player, PkEnum.find(modeID));
      PkEnum lastEnum = player.getPkMode().getCurrentPKMode();
      if (result == 1) {
         player.getPkMode().changePkMode(PkEnum.find(modeID));
      }

      this.writeByte(player.getPkMode().getPKModeID());
      player.writePacket(this);
      if (result == 1) {
         noticeChange(player, lastEnum);
      } else {
         SystemMessage.writeMessage(player, result);
      }

   }

   private int canManulChange(Player player, PkEnum newModeEnum) {
      if (newModeEnum.getModeID() == player.getPkMode().getPKModeID()) {
         return 8032;
      } else if (!player.getMap().canChangePkMode()) {
         return 8033;
      } else {
         return player.getLevel() < VariableConstant.Pk_Level ? 8034 : 1;
      }
   }

   public static void noticeChange(Player player, PkEnum lastEnum) {
      ChangePkMode.sendToClient(player, true);
      ChangePkView.sendWhenChangePkMode(player, lastEnum);
   }
}
