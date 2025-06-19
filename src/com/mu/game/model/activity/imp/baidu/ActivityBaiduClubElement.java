package com.mu.game.model.activity.imp.baidu;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ActivityBaiduClubElement extends ActivityElement {
   private int clubLevel = 1;

   public ActivityBaiduClubElement(int id, ActivityBaiduClub father, int cLevel) {
      super(id, father);
      this.clubLevel = cLevel;
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
   }

   public boolean canReceive(Player player, boolean notice) {
      return false;
   }

   public int getReceiveStatus(Player player) {
      return 0;
   }
}
