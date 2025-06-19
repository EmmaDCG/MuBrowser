package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.vipgift.VipGiftActivity;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;
import java.util.Iterator;

public class VipGiftMenu extends DynamicMenu {
   public VipGiftMenu() {
      super(32);
   }

   public int getShowNumber(Player player) {
      Activity acitivty = ActivityManager.getActivity(16);
      if (acitivty != null && acitivty.isOpen()) {
         VipGiftActivity ba = (VipGiftActivity)acitivty;
         ArrayList list = ba.getElementList();
         int num = 0;
         Iterator var7 = list.iterator();

         while(var7.hasNext()) {
            ActivityElement element = (ActivityElement)var7.next();
            if (element.getReceiveStatus(player) == 1) {
               ++num;
            }
         }

         return num;
      } else {
         return 0;
      }
   }

   public boolean isShow(Player player) {
      if (!super.isShow(player)) {
         return false;
      } else {
         Activity acitivty = ActivityManager.getActivity(16);
         return acitivty != null && acitivty.isOpen();
      }
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
