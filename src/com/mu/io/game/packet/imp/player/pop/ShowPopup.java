package com.mu.io.game.packet.imp.player.pop;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ShowPopup extends WriteOnlyPacket {
   public ShowPopup() {
      super(10005);
   }

   public static void directDoLeftClick(Player player, Popup pop) {
      pop.dealLeftClick(player);
      player.removePopup(pop.getID());
      pop.destroy();
   }

   public static void open(Player player, Popup pop) {
      try {
         if (player.popNoAgain(pop.getType())) {
            directDoLeftClick(player, pop);
            return;
         }

         player.addPopup(pop);
         ShowPopup op = new ShowPopup();
         op.writeInt(pop.getID());
         op.writeUTF(pop.getTitle());
         pop.writeContent(op, player);
         op.writeUTF(pop.getLeftButtonContent());
         op.writeUTF(pop.getRightButtonContent());
         op.writeBoolean(pop.isShowAgain(player));
         op.writeInt(pop.getRemainTime());
         player.writePacket(op);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
