package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangLevelData;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class RequstGangLevelupInfo extends ReadAndWritePacket {
   public RequstGangLevelupInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         GangLevelData data = GangManager.getLevelData(gang.getLevel());
         GangLevelData nextData = GangManager.getNextLevelData(gang.getLevel());
         if (data != null) {
            if (nextData == null) {
               this.writeBoolean(false);
            } else {
               this.writeBoolean(true);
               this.writeByte(nextData.getMaxMember());
               this.writeInt(data.getLevelUpNeedMoney());
               this.writeInt(data.getLevelUpNeedIngot());
            }

            player.writePacket(this);
         }
      }
   }
}
