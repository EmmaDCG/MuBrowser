package com.mu.io.game.packet.imp.storage;

import com.mu.game.model.item.container.BackpackExpandData;
import com.mu.game.model.item.container.imp.Backpack;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.ExpandBackpackPopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class BackpackExpand extends ReadAndWritePacket {
   public BackpackExpand(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int gridIndex = this.readByte();
      gridIndex = gridIndex + 1;
      Backpack backpack = player.getBackpack();
      int result = BackpackExpandData.canOpen(player, gridIndex);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      } else if (backpack.getCurWaitOpenGrid() > gridIndex) {
         ExpandBackpackPopup.openGrid(player, gridIndex);
      } else {
         long[] rewardAndNeed = BackpackExpandData.getRewardAndNeed(player, gridIndex);
         ExpandBackpackPopup pop = new ExpandBackpackPopup(player.createPopupID(), gridIndex - backpack.getLimit(), rewardAndNeed);
         ShowPopup.open(player, pop);
      }
   }
}
