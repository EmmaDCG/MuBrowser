package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;
import java.util.Iterator;

public class TxLevelUpMenu extends DynamicMenu {
   public TxLevelUpMenu() {
      super(23);
   }

   public int getShowNumber(Player player) {
      int num = 0;
      Activity th = ActivityManager.getActivity(8);
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
      Activity ba = ActivityManager.getActivity(8);
      if (ba != null && ba.isOpen()) {
         Iterator var4 = ba.getElementList().iterator();

         while(var4.hasNext()) {
            ActivityElement element = (ActivityElement)var4.next();
            if (element.getReceiveStatus(player) != 2) {
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
