package com.mu.game.model.chat.newlink;

import com.mu.config.BroadcastManager;
import com.mu.game.model.chat.ChatLinkInfo;
import com.mu.io.game.packet.WriteOnlyPacket;

public class NewCharactorLink extends NewChatLink {
   private static final String defaultColor = "c=0x66CC33";
   private long rid;
   private String name;
   private String imgText;
   private boolean isChat = true;

   public NewCharactorLink(int index, long rid, String name, String imgText, boolean isChat) {
      super(index, 4);
      this.rid = rid;
      this.name = name;
      this.imgText = imgText;
      this.isChat = isChat;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      packet.writeByte(this.getType());
      packet.writeDouble((double)this.rid);
      packet.writeUTF(this.name);
   }

   public String createContent() {
      ChatLinkInfo info = BroadcastManager.getChatLinkInfo(this.getType());
      return info != null && this.isChat ? this.imgText + "#F{" + info.getNewColor() + ",a=" + this.getIndex() + "}" + this.name + "#F" : this.imgText + "#F{" + "c=0x66CC33" + ",a=" + this.getIndex() + "}" + this.name + "#F";
   }

   public String createNoLinkContent() {
      ChatLinkInfo info = BroadcastManager.getChatLinkInfo(this.getType());
      return info != null && this.isChat ? this.imgText + "#F{c=0x66cc33}" + this.name + "#F" : this.imgText + "#F{c=0x66cc33}" + this.name + "#F";
   }
}
