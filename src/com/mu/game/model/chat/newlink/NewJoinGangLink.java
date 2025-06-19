package com.mu.game.model.chat.newlink;

import com.mu.io.game.packet.WriteOnlyPacket;

public class NewJoinGangLink extends NewChatLink {
   private long gangId;
   private String joinContent;

   public NewJoinGangLink(int index, long gangId, String joinContent) {
      super(index, 11);
      this.gangId = gangId;
      this.joinContent = joinContent;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeDouble((double)this.gangId);
   }

   public String createContent() {
      return "#F{e=7,a=" + this.getIndex() + "}" + this.joinContent + "#F";
   }

   public void destroy() {
      super.destroy();
      this.joinContent = null;
   }

   public String createNoLinkContent() {
      return null;
   }
}
