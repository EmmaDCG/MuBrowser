package com.mu.game.model.chat.link;

import com.mu.config.MessageText;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ExechangeBuyLink extends ChatLink {
   private long id;

   public ExechangeBuyLink(int index, long id) {
      super(index, 9);
      this.id = id;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeDouble((double)this.id);
   }

   public String createContent() {
      return "#c:{0x00ff00}#a:{" + this.getIndex() + "}[" + MessageText.getText(16616) + "]#a#c";
   }
}
