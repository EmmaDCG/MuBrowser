package com.mu.game.model.chat.link;

import com.mu.io.game.packet.WriteOnlyPacket;

public class PointLink extends ChatLink {
   private String linkContent;
   private int mapId;
   private int x;
   private int y;

   public PointLink(int index, String linkContent, int mapId, int x, int y) {
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
      return "#a:{" + this.getIndex() + "}" + this.linkContent + "#a";
   }
}
