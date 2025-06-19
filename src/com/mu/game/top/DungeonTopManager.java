package com.mu.game.top;

import com.mu.db.manager.TopDBManager;
import com.mu.game.model.unit.player.Player;
import java.util.concurrent.CopyOnWriteArrayList;

public class DungeonTopManager {
   private static CopyOnWriteArrayList bigDevilList = new CopyOnWriteArrayList();

   public static void restBigdevilList() {
      CopyOnWriteArrayList oldList = bigDevilList;
      bigDevilList = TopDBManager.getBigDevilTop();
      oldList.clear();
      oldList = null;
   }

   public static CopyOnWriteArrayList getBigDevilList() {
      return bigDevilList;
   }

   public static int getBigDevilPlayerTop(Player player) {
      CopyOnWriteArrayList list = getBigDevilList();
      if (list == null) {
         return 0;
      } else {
         for(int i = 0; i < list.size(); ++i) {
            BigDevilTopInfo info = (BigDevilTopInfo)list.get(i);
            if (info.getRid() == player.getID()) {
               return i + 1;
            }
         }

         return 0;
      }
   }

   public static BigDevilTopInfo getBigDevilTopInfo(int top) {
      CopyOnWriteArrayList list = getBigDevilList();
      return list != null && list.size() >= top ? (BigDevilTopInfo)list.get(top - 1) : null;
   }
}
