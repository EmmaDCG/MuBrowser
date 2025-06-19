package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.config.MessageText;
import com.mu.game.CenterManager;
import com.mu.game.model.transaction.Transaction;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class ClosePanel extends ReadAndWritePacket {
   public ClosePanel(int code, byte[] readData) {
      super(code, readData);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Transaction action = TransactionManager.getTransAcTransaction(player.getID());
      if (action != null) {
         Player targetPlayer = CenterManager.getPlayerByRoleID(action.getOtherID(player.getID()));
         if (targetPlayer != null) {
            ClosePanelByServer.closePanel(targetPlayer, player.getName());
            SystemMessage.writeMessage(targetPlayer, player.getName() + MessageText.getText(15027), 15027);
            SystemMessage.writeMessage(player, 15025);
         }

         TransactionManager.closeTransaction(player.getID());
      }

   }
}
