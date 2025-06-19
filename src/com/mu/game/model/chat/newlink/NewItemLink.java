package com.mu.game.model.chat.newlink;

import com.mu.game.model.item.model.ItemColor;
import com.mu.io.game.packet.WriteOnlyPacket;

public class NewItemLink extends NewChatLink {
   private long itemId;
   private String itemName;
   private int quality;
   private boolean isChat;

   public NewItemLink(int index, long itemId, String itemName, int quality, boolean isChat) {
      super(index, 5);
      this.itemId = itemId;
      this.itemName = itemName;
      this.quality = quality;
      this.isChat = isChat;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeDouble((double)this.itemId);
      packet.writeUTF(this.itemName);
      packet.writeByte(this.quality);
   }

   public String createContent() {
      return this.isChat ? "#F{" + ItemColor.find(this.quality).getNewColor() + ",a=" + this.getIndex() + "}" + this.itemName + "#F" : "#F{" + ItemColor.find(this.quality).getNewColor() + ",a=" + this.getIndex() + "}[" + this.itemName + "]#F";
   }

   public String createNoLinkContent() {
      return "#F{c=" + ItemColor.find(this.quality).getColor() + "}" + this.itemName + "#F";
   }
}
