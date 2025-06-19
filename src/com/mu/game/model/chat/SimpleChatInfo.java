package com.mu.game.model.chat;

import com.mu.game.model.chat.newlink.NewChatLink;

public class SimpleChatInfo {
   private String msg;
   private NewChatLink[] links = null;
   private String time;

   public String getMsg() {
      return this.msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   public NewChatLink[] getLinks() {
      return this.links;
   }

   public void setLinks(NewChatLink[] links) {
      this.links = links;
   }

   public String getTime() {
      return this.time;
   }

   public void setTime(String time) {
      this.time = time;
   }
}
