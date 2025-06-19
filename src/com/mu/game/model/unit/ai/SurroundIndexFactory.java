package com.mu.game.model.unit.ai;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.monster.Monster;
import com.mu.utils.Tools;
import java.util.concurrent.ConcurrentHashMap;

public class SurroundIndexFactory {
   private static ConcurrentHashMap surroundMap = Tools.newConcurrentHashMap();

   public static int getIndex(Monster owner, Creature target, int index) {
      Monster[] surroundArr = (Monster[])surroundMap.get(target);
      if (surroundArr == null) {
         surroundMap.putIfAbsent(target, new Monster[16]);
         surroundArr = (Monster[])surroundMap.get(target);
      }

      index = Math.min(surroundArr.length - 1, Math.max(0, index));
      if (check(owner, target, index, surroundArr)) {
         return index;
      } else {
         for(int i = 1; i < surroundArr.length / 2 + 1; ++i) {
            int left = index - i < 0 ? surroundArr.length + index - i : index - i;
            if (check(owner, target, left, surroundArr)) {
               return left;
            }

            int right = index + i >= surroundArr.length ? index + i - surroundArr.length : index + i;
            if (check(owner, target, right, surroundArr)) {
               return right;
            }
         }

         return index;
      }
   }

   private static boolean check(Monster owner, Creature target, int index, Monster[] surroundArr) {
      try {
         if (surroundArr[index] == null || surroundArr[index] == owner) {
            surroundArr[index] = owner;
            clear(surroundArr, owner, index);
            return true;
         }

         if (surroundArr[index].isDestroy() || surroundArr[index].isDie() || surroundArr[index].getMapID() != owner.getMapID() || surroundArr[index].getAggroList().getMostHated() != target) {
            surroundArr[index] = owner;
            clear(surroundArr, owner, index);
            return true;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return false;
   }

   private static void clear(Monster[] surroundArr, Monster owner, int index) {
      for(int i = 0; i < surroundArr.length; ++i) {
         if (i != index && surroundArr[i] == owner) {
            surroundArr[i] = null;
         }
      }

   }

   public static void remove(Creature target) {
      Monster[] surroundArr = (Monster[])surroundMap.remove(target);
      if (surroundArr != null) {
         for(int i = 0; i < surroundArr.length; ++i) {
            surroundArr[i] = null;
         }

         surroundArr = null;
      }

   }
}
