package com.mu.game.top;

import com.mu.config.MessageText;
import com.mu.game.model.trial.TrialConfigs;

public class WarCommentTopInfo extends TopInfo implements Comparable {
   private int warComment;
   private long warCommentTime;

   public String getvariable() {
      TrialConfigs configs = TrialConfigs.getConfig(this.warComment);
      return configs != null ? configs.getName() : MessageText.getText(1039);
   }

   public int getWarComment() {
      return this.warComment;
   }

   public void setWarComment(int warComment) {
      this.warComment = warComment;
   }

   public long getWarCommentTime() {
      return this.warCommentTime;
   }

   public void setWarCommentTime(long warCommentTime) {
      this.warCommentTime = warCommentTime;
   }

   public int compareTo(WarCommentTopInfo o) {
      if (this.getWarComment() != o.getWarComment()) {
         return o.getWarComment() - this.getWarComment();
      } else {
         return o.getWarCommentTime() >= this.getWarCommentTime() ? -1 : 1;
      }
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
