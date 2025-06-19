package com.mu.game.model.chat.link;

import com.mu.io.game.packet.WriteOnlyPacket;

public class MallLink extends ChatLink {
   private long itemId;
   private String name;
   private int count;

   public MallLink(int index, long itemId, String name, int count) {
      super(index, 10);
      this.itemId = itemId;
      this.name = name;
      this.count = count;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeDouble((double)this.itemId);
      packet.writeInt(this.count);
   }

   public String createContent() {
      return "#c:{0x66CC33}#a:{" + this.getIndex() + "}" + this.name + "#a#c";
   }

   public void destroy() {
      super.destroy();
      this.name = null;
   }
}
