package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.chat.newlink.NewMallLink;
import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.mall.MallItemData;
import com.mu.game.model.mall.ShortcutBuy;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.WriteOnlyPacket;

public class RenewPopup extends Popup {
   private int shortcutBuyID = 1;
   private String shortcutBuyStr = "";

   public RenewPopup(int id, int shortcutBuyID, String shortcutBuyStr) {
      super(id);
      this.shortcutBuyID = shortcutBuyID;
      this.shortcutBuyStr = shortcutBuyStr;
   }

   public String getTitle() {
      return MessageText.getText(4018);
   }

   public String getContent() {
      return this.shortcutBuyStr;
   }

   public void writeContent(WriteOnlyPacket packet, Player player) throws Exception {
      MallItemData data = MallConfigManager.getData(ShortcutBuy.getModel(this.shortcutBuyID));
      String content = "%m%";
      NewMallLink link = new NewMallLink(0, data.getItem().getID(), this.shortcutBuyStr, 1);
      content = content.replace("%m%", link.getContent());
      ChatProcess.writeNewLinkMessage(content, new NewChatLink[]{link}, packet);
      link.destroy();
      link = null;
   }

   public void dealLeftClick(Player player) {
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 22;
   }
}
