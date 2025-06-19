package com.mu.game.model.chat.link;

import com.mu.config.BroadcastManager;
import com.mu.config.MessageText;
import com.mu.game.model.chat.ChatLinkInfo;
import com.mu.io.game.packet.WriteOnlyPacket;

public class CharactorLink extends ChatLink {
   private long rid;
   private String name;
   private int vipLevel;
   private boolean isChat = true;

   public CharactorLink(int index, long rid, String name, int vipLevel, boolean isChat) {
      super(index, 4);
      this.rid = rid;
      this.name = name;
      this.vipLevel = vipLevel;
      this.isChat = isChat;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeDouble((double)this.rid);
      packet.writeUTF(this.name);
   }

   public String createContent() {
      ChatLinkInfo info = BroadcastManager.getChatLinkInfo(this.getType());
      if (info != null && this.isChat) {
         return this.vipLevel > 0 ? "#c:" + info.getColor() + "#a:{" + this.getIndex() + "}" + info.getValue().replace("%v%", MessageText.getText(17001).replace("%v%", String.valueOf(this.vipLevel))).replace("%n%", this.name) + "#a#c：" : "#c:" + info.getColor() + "#a:{" + this.getIndex() + "}" + info.getValue().replace("%v%", "").replace("%n%", this.name) + "#a#c：";
      } else {
         return "#a:{" + this.getIndex() + "}" + this.name + "#a";
      }
   }
}
