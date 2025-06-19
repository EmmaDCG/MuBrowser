package com.mu.game.model.chat.link;

import com.mu.io.game.packet.WriteOnlyPacket;

public abstract class ChatLink {
   public static final int LINK_POINT = 0;
   public static final int LINK_NPC = 1;
   public static final int LINK_MONSTER = 2;
   public static final int LINK_GATHER = 3;
   public static final int LINK_CHARACTOR = 4;
   public static final int LINK_ITEM = 5;
   public static final int LINK_OPEN_FORM = 6;
   public static final int LINK_FLY = 7;
   public static final int LINK_TASK_COMMIT = 8;
   public static final int LINK_Exchange_Buy = 9;
   public static final int LINK_Market_Item = 10;
   public static final int LINK_Join_Gang = 11;
   public static final int LINK_Open_Boss = 13;
   public static final int LINK_Compose = 14;
   public static final int LINK_Exchange_Item = 99;
   private int index;
   private int type;
   private String content;

   public ChatLink(int index, int type) {
      this.index = index;
      this.type = type;
   }

   public abstract void writeDetail(WriteOnlyPacket var1) throws Exception;

   public abstract String createContent();

   public int getType() {
      return this.type;
   }

   public String getContent() {
      if (this.content == null) {
         this.content = this.createContent();
      }

      return this.content;
   }

   public int getIndex() {
      return this.index;
   }

   public void destroy() {
      this.content = null;
   }
}
