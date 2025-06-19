package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;
import java.util.Iterator;

public class YxlbMenu extends DynamicMenu {
   public YxlbMenu() {
      super(25);
   }

   public int getShowNumber(Player player) {
      return 1;
   }

   public boolean isShow(Player player) {
      Activity activity = ActivityManager.getActivity(9);
      if (activity != null && activity.isOpen()) {
         ArrayList list = activity.getElementList();
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            ActivityElement ae = (ActivityElement)var5.next();
            if (ae.getReceiveStatus(player) != 2) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean hasEffect(Player player, int showNumber) {
      return true;
   }
}
