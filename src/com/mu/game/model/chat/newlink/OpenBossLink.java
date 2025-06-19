package com.mu.game.model.chat.newlink;

import com.mu.io.game.packet.WriteOnlyPacket;

public class OpenBossLink extends NewChatLink {
   private String linkContent;
   private int bossType;
   private int bossId;

   public OpenBossLink(int index, String lc, int bossType, int bossId) {
      super(index, 13);
      this.linkContent = lc;
      this.bossType = bossType;
      this.bossId = bossId;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeByte(this.bossType);
      packet.writeShort(this.bossId);
   }

   public String createContent() {
      return "#a:{" + this.getIndex() + "}[" + this.linkContent + "]#a";
   }

   public String createNoLinkContent() {
      return null;
   }
}
