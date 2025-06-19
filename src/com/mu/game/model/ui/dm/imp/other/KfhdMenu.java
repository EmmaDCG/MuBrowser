package com.mu.game.model.ui.dm.imp.other;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.shell.KfShell;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.activity.ActivityChangeDigital;
import java.util.ArrayList;
import java.util.Iterator;

public class KfhdMenu extends DynamicMenu {
   public KfhdMenu() {
      super(15);
   }

   public boolean isShow(Player player) {
      if (Global.isInterServiceServer()) {
         return false;
      } else {
         return !super.isShow(player) ? false : ActivityManager.showKfMenu();
      }
   }

   public int getShowNumber(Player player) {
      if (!this.isShow(player)) {
         return 0;
      } else {
         int num = 0;
         Activity ba = ActivityManager.getActivity(2);
         Activity la = ActivityManager.getActivity(3);
         Activity pa = ActivityManager.getActivity(4);
         Activity eq = ActivityManager.getActivity(7);
         Activity zy = ActivityManager.getActivity(14);
         Activity qh = ActivityManager.getActivity(15);
         ActivityElement e;
         Iterator var10;
         if (ba.isOpen()) {
            var10 = ba.getElementList().iterator();

            while(var10.hasNext()) {
               e = (ActivityElement)var10.next();
               if (e.canReceive(player, false)) {
                  ++num;
               }
            }
         }

         if (la.isOpen()) {
            var10 = la.getElementList().iterator();

            while(var10.hasNext()) {
               e = (ActivityElement)var10.next();
               if (e.canReceive(player, false)) {
                  ++num;
               }
            }
         }

         if (pa.isOpen()) {
            var10 = pa.getElementList().iterator();

            while(var10.hasNext()) {
               e = (ActivityElement)var10.next();
               if (e.canReceive(player, false)) {
                  ++num;
               }
            }
         }

         if (eq.isOpen()) {
            var10 = eq.getElementList().iterator();

            while(var10.hasNext()) {
               e = (ActivityElement)var10.next();
               if (e.canReceive(player, false)) {
                  ++num;
               }
            }
         }

         if (zy.isOpen()) {
            var10 = zy.getElementList().iterator();

            while(var10.hasNext()) {
               e = (ActivityElement)var10.next();
               if (e.canReceive(player, false)) {
                  ++num;
               }
            }
         }

         if (qh.isOpen()) {
            var10 = qh.getElementList().iterator();

            while(var10.hasNext()) {
               e = (ActivityElement)var10.next();
               if (e.canReceive(player, false)) {
                  ++num;
               }
            }
         }

         return num;
      }
   }

   public void onClick(Player player) {
      super.onClick(player);
      ActivityShell shell = ActivityManager.getShell(2);
      if (shell != null) {
         KfShell kfShell = (KfShell)shell;
         ArrayList list = kfShell.getDigitalList(player);
         if (list != null && list.size() > 0) {
            ActivityChangeDigital.pushDigital(player, list);
            list.clear();
         }
      }

   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
