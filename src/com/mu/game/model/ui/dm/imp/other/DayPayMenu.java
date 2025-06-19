package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;
import java.util.Iterator;

public class DayPayMenu extends DynamicMenu {
   public DayPayMenu() {
      super(34);
   }

   public int getShowNumber(Player player) {
      Activity ac = ActivityManager.getActivity(17);
      if (ac != null && ac.isOpen()) {
         int num = 0;
         ArrayList list = ac.getElementList();
         Iterator var6 = list.iterator();

         while(var6.hasNext()) {
            ActivityElement ae = (ActivityElement)var6.next();
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
      Activity ac = ActivityManager.getActivity(17);
      if (ac != null && ac.isOpen()) {
         ArrayList list = ac.getElementList();
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
      return showNumber > 0;
   }
}
