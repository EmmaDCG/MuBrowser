package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.chat.newlink.NewItemLink;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ShowItemManager;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class AfterAllocateEquipItemPopup extends Popup {
   private long itemID;
   private String itemName;
   private int str;
   private int con;
   private int dex;
   private int intell;

   public AfterAllocateEquipItemPopup(int id, long itemID, String itemName, int str, int con, int dex, int intell) {
      super(id);
      this.itemID = itemID;
      this.itemName = itemName;
      this.str = str;
      this.con = con;
      this.dex = dex;
      this.intell = intell;
   }

   public String getTitle() {
      return MessageText.getText(4013);
   }

   public String getContent() {
      String s = MessageText.getText(4014);
      String tmpStr = "";
      if (this.str > 0) {
         tmpStr = tmpStr + this.str + MessageText.getText(4012) + StatEnum.STR.getName();
      }

      if (this.con > 0) {
         tmpStr = tmpStr + this.con + MessageText.getText(4012) + StatEnum.CON.getName();
      }

      if (this.dex > 0) {
         tmpStr = tmpStr + this.dex + MessageText.getText(4012) + StatEnum.DEX.getName();
      }

      if (this.intell > 0) {
         tmpStr = tmpStr + this.intell + MessageText.getText(4012) + StatEnum.INT.getName();
      }

      s = s.replace("%s%", tmpStr);
      return s;
   }

   public void dealLeftClick(Player player) {
      ShowItemManager.removeAddDestroyItem(this.itemID);
      Item item = player.getBackpack().getItemByID(this.itemID);
      if (item == null) {
         SystemMessage.writeMessage(player, 3002);
      } else {
         int result = player.getItemManager().useItem(item, 1, true).getResult();
         if (result != 1) {
            SystemMessage.writeMessage(player, result);
         }

      }
   }

   public void writeContent(WriteOnlyPacket packet, Player player) throws Exception {
      Item item = player.getBackpack().getItemByID(this.itemID);
      String tmpStr = this.getContent();
      if (item == null) {
         tmpStr = tmpStr.replace("%SS%", this.itemName);
         packet.writeUTF(tmpStr);
         packet.writeByte(0);
      } else {
         NewItemLink link = new NewItemLink(0, item.getID(), item.getName(), item.getQuality(), false);
         tmpStr = tmpStr.replace("%SS%", link.getContent());
         ChatProcess.writeNewLinkMessage(tmpStr, new NewChatLink[]{link}, packet);
         link.destroy();
         ShowItemManager.addShowItem(item);
      }

   }

   public void dealRightClick(Player player) {
      ShowItemManager.removeAddDestroyItem(this.itemID);
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public void destroy() {
      ShowItemManager.removeAddDestroyItem(this.itemID);
      super.destroy();
   }

   public int getType() {
      return 17;
   }
}
