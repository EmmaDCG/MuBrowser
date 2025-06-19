package com.mu.game.model.chat.newlink;

import com.mu.config.MessageText;
import com.mu.io.game.packet.WriteOnlyPacket;

public class NewExechangeBuyLink extends NewChatLink {
   private long id;

   public NewExechangeBuyLink(int index, long id) {
      super(index, 9);
      this.id = id;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeDouble((double)this.id);
   }

   public String createContent() {
      return "#F{e=7,a=" + this.getIndex() + "}[" + MessageText.getText(16616) + "]#F";
   }

   public String createNoLinkContent() {
      return null;
   }
}
