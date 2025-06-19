package com.mu.game.model.chat.newlink;

import com.mu.io.game.packet.WriteOnlyPacket;

public class NewPointLink extends NewChatLink {
   private String linkContent;
   private int mapId;
   private int x;
   private int y;

   public NewPointLink(int index, String linkContent, int mapId, int x, int y) {
      super(index, 0);
      this.linkContent = linkContent;
      this.mapId = mapId;
      this.x = x;
      this.y = y;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeShort(this.mapId);
      packet.writeInt(this.x);
      packet.writeInt(this.y);
   }

   public String createContent() {
      return "#F{c=0x66cc33,a=" + this.getIndex() + "}" + this.linkContent + "#F";
   }

   public String createNoLinkContent() {
      return null;
   }
}
