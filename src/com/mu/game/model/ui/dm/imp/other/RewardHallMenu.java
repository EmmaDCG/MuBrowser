package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.activity.ActivityChangeDigital;
import java.util.ArrayList;

public class RewardHallMenu extends DynamicMenu {
   public RewardHallMenu() {
      super(11);
   }

   public int getShowNumber(Player player) {
      return player.getOnlineManager().getCanReceiveCount() + player.getSignManager().getCanReceiveCount() + player.getVitalityManager().getCanReceiveCount();
   }

   public void onClick(Player player) {
      super.onClick(player);
      int signNum = player.getSignManager().getCanReceiveCount();
      int onlineNum = player.getOnlineManager().getCanReceiveCount();
      int vitalityNum = player.getVitalityManager().getCanReceiveCount();
      ArrayList list = new ArrayList();
      if (signNum > 0) {
         list.add(new int[]{1, signNum});
      }

      if (onlineNum > 0) {
         list.add(new int[]{2, onlineNum});
      }

      if (vitalityNum > 0) {
         list.add(new int[]{3, vitalityNum});
      }

      if (list.size() > 0) {
         ActivityChangeDigital.pushDigital(player, list);
         list.clear();
      }

      list = null;
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
