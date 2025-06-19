package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.bigpay.BigPayActivity;
import com.mu.game.model.activity.imp.bigpay.BigPayElement;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;
import java.util.Iterator;

public class BigPayMenu extends DynamicMenu {
   public BigPayMenu() {
      super(29);
   }

   public int getShowNumber(Player player) {
      Activity acitivty = ActivityManager.getActivity(13);
      if (acitivty != null && acitivty.isOpen()) {
         BigPayActivity ba = (BigPayActivity)acitivty;
         ArrayList list = ba.getElementList();
         int num = 0;

         BigPayElement be;
         for(Iterator var7 = list.iterator(); var7.hasNext(); num += be.getCanReceiveTimes(player)) {
            ActivityElement element = (ActivityElement)var7.next();
            be = (BigPayElement)element;
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
         Activity acitivty = ActivityManager.getActivity(13);
         return acitivty != null && acitivty.isOpen();
      }
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
