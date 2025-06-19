package com.mu.game.model.chat.link;

import com.mu.io.game.packet.WriteOnlyPacket;

public class JoinGangLink extends ChatLink {
   private long gangId;
   private String joinContent;

   public JoinGangLink(int index, long gangId, String joinContent) {
      super(index, 11);
      this.gangId = gangId;
      this.joinContent = joinContent;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeDouble((double)this.gangId);
   }

   public String createContent() {
      return "#c:{0x00ff00}#a:{" + this.getIndex() + "}" + this.joinContent + "#a#c";
   }

   public void destroy() {
      super.destroy();
      this.joinContent = null;
   }
}
