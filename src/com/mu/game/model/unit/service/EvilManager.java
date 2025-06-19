package com.mu.game.model.unit.service;

import com.mu.config.VariableConstant;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;

public class EvilManager {
   public static void killPlayer(Player player, Player attacker) {
      if (player != null && attacker != null) {
         if (player.getMap().isPkPunishment()) {
            doEvil(player, attacker);
         }

      }
   }

   public static void doEvil(Player player, Player attacker) {
      if (player.getEvil() < VariableConstant.PK_Evil_Red) {
         if (!player.getBuffManager().hasBuff(80001)) {
            if (attacker.getEvil() < VariableConstant.Pk_Evil_Max) {
               int evil = attacker.getEvil();
               evil += VariableConstant.Pk_Evil_Every;
               attacker.changeEvil(evil);
            }
         }
      }
   }

   public static int getShowEvil(int curEvil) {
      if (curEvil == 0) {
         return curEvil;
      } else {
         int value = curEvil % VariableConstant.Pk_Evil_Every == 0 ? curEvil / VariableConstant.Pk_Evil_Every : curEvil / VariableConstant.Pk_Evil_Every + 1;
         return value;
      }
   }

   public static int canFightByEvil(Player player) {
      Map map = player.getMap();
      return map.isPkPunishment() && getShowEvil(player.getEvil()) >= getShowEvil(VariableConstant.Pk_Evil_Max) ? 8035 : 1;
   }
}
