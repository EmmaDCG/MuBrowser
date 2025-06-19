package com.mu.game.model.chat.newlink;

import com.mu.io.game.packet.WriteOnlyPacket;

public class OpenComposeLink extends NewChatLink {
   private int bigId;
   private int midId;
   private int smallId;
   private String linkContent;

   public OpenComposeLink(int index, int bid, int mid, int sid, String linkContent) {
      super(index, 14);
      this.bigId = bid;
      this.midId = mid;
      this.smallId = sid;
      this.linkContent = linkContent;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeByte(this.bigId);
      packet.writeByte(this.midId);
      packet.writeShort(this.smallId);
   }

   public String createContent() {
      return "#a:{" + this.getIndex() + "}[" + this.linkContent + "]#a";
   }

   public String createNoLinkContent() {
      return null;
   }
}
