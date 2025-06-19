package com.mu.game.model.chat.link;

import com.mu.io.game.packet.WriteOnlyPacket;

public class OpenPanelLink extends ChatLink {
   private String linkContent;
   private int bigId;
   private int smallId;

   public OpenPanelLink(int index, String linkContent, int bigId, int smallId) {
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
      return "#a:{" + this.getIndex() + "}" + this.linkContent + "#a";
   }

   public void destroy() {
      super.destroy();
      this.linkContent = null;
   }
}
