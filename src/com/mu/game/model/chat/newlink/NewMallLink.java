package com.mu.game.model.chat.newlink;

import com.mu.io.game.packet.WriteOnlyPacket;

public class NewMallLink extends NewChatLink {
   private long itemId;
   private String name;
   private int count;

   public NewMallLink(int index, long itemId, String name, int count) {
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
      return "#F{c=0x66CC33,a=" + this.getIndex() + "}" + this.name + "#F";
   }

   public void destroy() {
      super.destroy();
      this.name = null;
   }

   public String createNoLinkContent() {
      return null;
   }
}
