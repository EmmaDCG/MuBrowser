package com.mu.game.top;

import com.mu.config.MessageText;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;

public class LevelTopInfo extends TopInfo implements Comparable {
   private long levelUpTime;

   public long getLevelUpTime() {
      return this.levelUpTime;
   }

   public void setLevelUpTime(long levelUpTime) {
      this.levelUpTime = levelUpTime;
   }

   public String getvariable() {
      Gang gang = null;
      GangMember member = GangManager.getMember(this.getRid());
      if (member != null) {
         gang = GangManager.getGang(member.getGangId());
      }

      return gang != null ? gang.getName() : MessageText.getText(1039);
   }

   public int compareTo(LevelTopInfo o) {
      if (this.getLevel() != o.getLevel()) {
         return o.getLevel() - this.getLevel();
      } else {
         return o.getLevelUpTime() >= this.getLevelUpTime() ? -1 : 1;
      }
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
