package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.chat.newlink.NewOpenPanelLink;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.awt.Point;

public class ToDepotPopup extends Popup {
   private static final int mapId = 10001;
   private static final int x = 11982;
   private static final int y = 15729;
   private static String content = MessageText.getText(4032);
   private static NewOpenPanelLink link = new NewOpenPanelLink(0, MessageText.getText(4033), 137, 0);

   static {
      content = content.replace("%s%", link.getContent());
   }

   public ToDepotPopup(int id) {
      super(id);
   }

   public String getTitle() {
      return MessageText.getText(4034);
   }

   public String getContent() {
      return null;
   }

   public void dealLeftClick(Player player) {
      if (player.isInDungeon()) {
         SystemMessage.writeMessage(player, 4035);
      } else {
         player.getMap().switchMap(player, 10001, new Point(11982, 15729));
      }

   }

   public void dealRightClick(Player player) {
   }

   public void writeContent(WriteOnlyPacket packet, Player player) throws Exception {
      ChatProcess.writeNewLinkMessage(content, new NewChatLink[]{link}, packet);
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 36;
   }
}
