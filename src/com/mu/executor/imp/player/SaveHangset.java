package com.mu.executor.imp.player;

import com.mu.db.manager.HangDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.hang.GameHang;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveHangset extends Executable {
   public SaveHangset(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      HangDBManager.updateHangset(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      GameHang hang = player.getGameHang();
      packet.writeLong(player.getID());
      packet.writeUTF(hang.getCycleStr());
      packet.writeUTF(hang.getPickupOtherStr());
      packet.writeUTF(hang.getEquipQualityStr());
      packet.writeUTF(hang.getHangSkillStr());
      packet.writeUTF(hang.getHangSaleStr());
   }
}
