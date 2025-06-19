package com.mu.io.game.packet.imp.pkModel;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ChangePkMode extends WriteOnlyPacket {
   public ChangePkMode(long roleID, int modeID) {
      super(32009);

      try {
         this.writeDouble((double)roleID);
         this.writeByte(modeID);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendToClient(Player player, boolean onlySelf) {
      ChangePkMode cpm = new ChangePkMode(player.getID(), player.getPkMode().getPKModeID());
      if (onlySelf) {
         player.writePacket(cpm);
      } else {
         player.getMap().sendPacketToAroundPlayer(cpm, player, true);
      }

      cpm.destroy();
      cpm = null;
   }

   public static void change(Player player, int modelId) {
      PkEnum lastEnum = player.getPkMode().getCurrentPKMode();
      player.getPkMode().changePkMode(PkEnum.find(modelId));
      SetPkMode sm = new SetPkMode(player.getPkMode().getPKModeID());
      player.writePacket(sm);
      sm.destroy();
      sm = null;
      SetPkMode.noticeChange(player, lastEnum);
   }
}
