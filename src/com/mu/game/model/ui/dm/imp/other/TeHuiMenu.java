package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;
import java.util.Iterator;

public class TeHuiMenu extends DynamicMenu {
   public TeHuiMenu() {
      super(21);
   }

   public int getShowNumber(Player player) {
      int num = 0;
      Activity th = ActivityManager.getActivity(6);
      if (th != null && th.isOpen()) {
         Iterator var5 = th.getElementList().iterator();

         while(var5.hasNext()) {
            ActivityElement ae = (ActivityElement)var5.next();
            if (ae.getReceiveStatus(player) == 1) {
               ++num;
            }
         }

         return num;
      } else {
         return 0;
      }
   }

   public boolean isShow(Player player) {
      if (super.isShow(player)) {
         return this.getShowNumber(player) > 0;
      } else {
         return false;
      }
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
