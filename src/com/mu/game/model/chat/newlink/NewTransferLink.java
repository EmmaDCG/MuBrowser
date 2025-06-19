package com.mu.game.model.chat.newlink;

import com.mu.io.game.packet.WriteOnlyPacket;

public class NewTransferLink extends NewChatLink {
   private String linkContent;
   private int targetIndex;

   public NewTransferLink(int index, String linkContent, int targetIndex) {
      super(index, 7);
      this.linkContent = linkContent;
      this.targetIndex = targetIndex;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeByte(this.targetIndex);
   }

   public String createContent() {
      return "#F{c=0x66cc33,a=" + this.getIndex() + "}" + this.linkContent + "#F";
   }

   public String createNoLinkContent() {
      return null;
   }
}
