package com.mu.io.game.packet.imp.hallows;

import com.mu.game.model.hallow.HallowManager;
import com.mu.game.model.hallow.model.PartModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ShowHallowStats extends ReadAndWritePacket {
   public ShowHallowStats(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      HallowManager manager = player.getHallowsManager();
      PartModel model = PartModel.getPartModel(manager.getRank(), manager.getLevel());
      this.writeUTF(model.getModifyString());
      player.writePacket(this);
   }
}
