package com.mu.game.model.chat.link;

import com.mu.io.game.packet.WriteOnlyPacket;

public class TransferLink extends ChatLink {
   private String linkContent;
   private int targetIndex;

   public TransferLink(int index, String linkContent, int targetIndex) {
      super(index, 7);
      this.linkContent = linkContent;
      this.targetIndex = targetIndex;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeByte(this.targetIndex);
   }

   public String createContent() {
      return "#a:{" + this.getIndex() + "}" + this.linkContent + "#a";
   }
}
