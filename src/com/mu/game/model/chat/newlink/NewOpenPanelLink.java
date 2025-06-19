package com.mu.game.model.chat.newlink;

import com.mu.io.game.packet.WriteOnlyPacket;

public class NewOpenPanelLink extends NewChatLink {
   private String linkContent;
   private int bigId;
   private int smallId;

   public NewOpenPanelLink(int index, String linkContent, int bigId, int smallId) {
      super(index, 6);
      this.linkContent = linkContent;
      this.bigId = bigId;
      this.smallId = smallId;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeShort(this.bigId);
      packet.writeByte(this.smallId);
   }

   public String createContent() {
      return "#F{c=0x66CC33,a=" + this.getIndex() + "}" + this.linkContent + "#F";
   }

   public void destroy() {
      super.destroy();
      this.linkContent = null;
   }

   public String createNoLinkContent() {
      return null;
   }
}
