package com.mu.game.model.top;

import com.mu.db.manager.GameDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.buff.imp.role.BuffWorldLevel;
import com.mu.game.model.unit.player.Player;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorldLevelManager {
   private static CopyOnWriteArrayList infoList = new CopyOnWriteArrayList();
   private static int worldLevel = 1;
   private static long updateTime = 0L;

   public static void clearInfo() {
      infoList.clear();
   }

   public static void addInfo(WorldLevelInfo info) {
      infoList.add(info);
   }

   public static void calculateWorldLevel() {
      CopyOnWriteArrayList list = GameDBManager.getWorldLevel();
      CopyOnWriteArrayList oldList = infoList;
      updateTime = System.currentTimeMillis();
      int size = list.size();
      int tmpLevel = 0;
      if (size > 0) {
         for(int i = 0; i < size; ++i) {
            WorldLevelInfo info = (WorldLevelInfo)list.get(i);
            tmpLevel += info.getLevel();
         }

         infoList = list;
         worldLevel = tmpLevel / size;
      }

      oldList.clear();
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         createWorldLevelBuff(player);
      }

   }

   public static void createWorldLevelBuff(Player player) {
      if (player.getLevel() >= 150) {
         int levelGap = worldLevel - player.getLevel();
         List list = null;
         if (levelGap <= 30) {
            list = BuffWorldLevel.createParams(worldLevel, 0, 0);
         } else {
            int expBonus = 1000 * levelGap;
            list = BuffWorldLevel.createParams(worldLevel, levelGap, expBonus);
         }

         player.getBuffManager().createAndStartBuff(player, 80009, 1, true, 0L, list);
         list.clear();
      }
   }

   public static long getUpdateTime() {
      return updateTime;
   }

   public static CopyOnWriteArrayList getInfoList() {
      return infoList;
   }

   public static int getWorldLevel() {
      return worldLevel;
   }
}
