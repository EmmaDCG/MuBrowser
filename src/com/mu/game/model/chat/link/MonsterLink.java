package com.mu.game.model.chat.link;

import com.mu.io.game.packet.WriteOnlyPacket;

public class MonsterLink extends ChatLink {
   private int mapId;
   private int x;
   private int y;
   private int modelId;
   private String linkContent;

   public MonsterLink(int index, String linkContent, int mapId, int x, int y, int modelId) {
      super(index, 2);
      this.linkContent = linkContent;
      this.mapId = mapId;
      this.x = x;
      this.y = y;
      this.modelId = modelId;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeShort(this.mapId);
      packet.writeInt(this.x);
      packet.writeInt(this.y);
      packet.writeInt(this.modelId);
   }

   public String createContent() {
      return "#a:{" + this.getIndex() + "}" + this.linkContent + "#a";
   }
}
