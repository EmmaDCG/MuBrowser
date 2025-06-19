package com.mu.io.game.packet.imp.friend;

import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.AddToBlackPopup;
import com.mu.game.model.unit.player.popup.imp.FriendToBlackPopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class AddBlack extends ReadAndWritePacket {
   public AddBlack(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      long fid = (long)this.readDouble();
      Player player = this.getPlayer();
      Player other = CenterManager.getPlayerByRoleID(fid);
      if (other == null) {
         SystemMessage.writeMessage(player, 1021);
      } else if (other.getID() == player.getID()) {
         SystemMessage.writeMessage(player, 1056);
      } else {
         if (player.getFriendManager().isFriend(fid)) {
            ShowPopup.open(player, new FriendToBlackPopup(player.createPopupID(), other));
         } else {
            ShowPopup.open(player, new AddToBlackPopup(player.createPopupID(), other));
         }

      }
   }
}
