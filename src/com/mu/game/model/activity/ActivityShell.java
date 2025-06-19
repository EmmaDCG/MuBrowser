package com.mu.game.model.activity;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class ActivityShell {
   private ArrayList activityList = new ArrayList();

   public void addActivity(Activity activity) {
      this.activityList.add(activity);
   }

   public abstract int getMenuId();

   public abstract int getShellId();

   public abstract void init(InputStream var1) throws Exception;

   public void reload(InputStream in) throws Exception {
      Iterator var3 = this.activityList.iterator();

      while(var3.hasNext()) {
         Activity ac = (Activity)var3.next();
         ActivityManager.removeAndDestroyActivity(ac.getId());
      }

      this.init(in);
      this.refreshIcon();
   }

   public ArrayList getActivityList() {
      return this.activityList;
   }

   public void refreshIcon() {
      UpdateMenu.allPlayerUpdate(this.getMenuId());
   }

   public void refreshIcon(Player player) {
      UpdateMenu.update(player, this.getMenuId());
   }
}
