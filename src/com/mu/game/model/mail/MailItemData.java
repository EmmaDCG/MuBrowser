package com.mu.game.model.mail;

import com.mu.game.model.item.ItemSaveAide;

public class MailItemData extends ItemSaveAide {
   private long mailId;
   private int index;

   public MailItemData(long mailId, int index) {
      this.mailId = mailId;
      this.index = index;
   }

   public long getMailId() {
      return this.mailId;
   }

   public void setMailId(long mailId) {
      this.mailId = mailId;
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }
}
