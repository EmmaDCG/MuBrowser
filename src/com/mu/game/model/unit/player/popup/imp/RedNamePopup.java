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

public class RedNamePopup extends Popup {
   private int pkValue = 1;

   public RedNamePopup(int id, int pkValue) {
      super(id);
      this.pkValue = pkValue;
   }

   public String getTitle() {
      return MessageText.getText(4015);
   }

   public String getContent() {
      return MessageText.getText(4016).replace("%s%", String.valueOf(this.pkValue));
   }

   public void writeContent(WriteOnlyPacket packet, Player player) throws Exception {
      MallItemData data = MallConfigManager.getData(ShortcutBuy.getModel(1));
      String content = this.getContent();
      NewMallLink link = new NewMallLink(0, data.getItem().getID(), MessageText.getText(4017), this.pkValue);
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
      return 20;
   }
}
