package com.mu.io.game.packet.imp.rewardhall.vitality;

import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.rewardhall.RewardHallConfigManager;
import com.mu.game.model.rewardhall.vitality.VitalityTaskData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.OpenPanel;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class VitalityEnter extends ReadAndWritePacket {
   public VitalityEnter(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int id = this.readByte();
      VitalityTaskData data = RewardHallConfigManager.getVitalityTaskData(id);
      if (data == null) {
         SystemMessage.writeMessage(player, 24101);
      } else if (data.isHasEnter()) {
         switch(data.getEnterType()) {
         case 1:
            if (data.getEnterFunction() != 0 && !FunctionOpenManager.isOpen(player, data.getEnterFunction())) {
               SystemMessage.writeMessage(player, 24102);
            } else {
               OpenPanel.open(player, data.getEnterPanelId1(), data.getEnterPanelId2());
            }
         default:
         }
      }
   }
}
