package com.mu.io.game.packet.imp.attack;

import com.mu.config.VariableConstant;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.team.TeamManager;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.model.BuffConstant;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.EvilEnum;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.game.model.unit.service.EvilManager;
import com.mu.game.model.unit.unitevent.imp.PlayerAttackEvent;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;

public class AttackCreature extends ReadAndWritePacket {
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum;

   public AttackCreature(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int type = this.readByte();
      long targetID = (long)this.readDouble();
      Creature target = getAttackTarget(player.getMap(), type, targetID);
      boolean result = PlayerAttackEvent.canDo(player, System.currentTimeMillis());
      if (result) {
         player.attack(target);
      }

   }

   public static Creature getAttackTarget(Map map, int type, long targetID) {
      switch(type) {
      case 1:
         return map.getPlayer(targetID);
      case 2:
         return map.getMonster(targetID);
      case 3:
      default:
         return null;
      case 4:
         return map.getPet(targetID);
      }
   }

   public static int distanceCheck(Point point, Point otherPoint, int range) {
      int distance = MathUtil.getDistance(point, otherPoint);
      return distance > range + 2400 ? 8014 : 1;
   }

   public static int getPkCheckResult(Player player, Player attacked) {
      if (player.getID() == attacked.getID()) {
         return 8015;
      } else {
         Map pMap = player.getMap();
         if (!pMap.isCanPk()) {
            return 8016;
         } else if (!pMap.isSafe(attacked) && !player.getMap().isSafe(player)) {
            int result = EvilManager.canFightByEvil(player);
            if (result != 1) {
               return result;
            } else if (!pMap.equals(attacked.getMap())) {
               return 8018;
            } else if (player.getLevel() < VariableConstant.Pk_Level) {
               return 8019;
            } else if (attacked.getLevel() < VariableConstant.Pk_Level) {
               return 8020;
            } else {
               int[] var7 = BuffConstant.PkProtectIDs;
               int var6 = BuffConstant.PkProtectIDs.length;

               for(int var5 = 0; var5 < var6; ++var5) {
                  int protectBuffID = var7[var5];
                  if (attacked.getBuffManager().hasBuff(protectBuffID)) {
                     return 8036;
                  }
               }

               switch($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum()[player.getPkMode().getCurrentPKMode().ordinal()]) {
               case 1:
                  if (TeamManager.isTeammate(player, attacked)) {
                     return 8024;
                  }

                  if (GangManager.inSameGang(player.getID(), attacked.getID())) {
                     return 8025;
                  }

                  switch($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum()[attacked.getSelfEvilEnum().ordinal()]) {
                  case 3:
                  case 4:
                     return 1;
                  default:
                     return 8022;
                  }
               case 2:
                  if (TeamManager.isTeammate(player, attacked)) {
                     return 8024;
                  }

                  if (GangManager.inSameGang(player.getID(), attacked.getID())) {
                     return 8025;
                  }
               case 3:
               }

               return 1;
            }
         } else {
            return 8017;
         }
      }
   }

   public static int getPkViewByMode(Player sawer, Player attacked) {
      if (sawer.getID() == attacked.getID()) {
         return 8015;
      } else if (!sawer.getMap().equals(attacked.getMap())) {
         return 8018;
      } else if (attacked.getLevel() < VariableConstant.Pk_Level) {
         return 8019;
      } else {
         int result = 1;
         switch($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum()[sawer.getPkMode().getCurrentPKMode().ordinal()]) {
         case 2:
            if (TeamManager.isTeammate(sawer, attacked)) {
               return 8024;
            }

            if (GangManager.inSameGang(sawer.getID(), attacked.getID())) {
               return 8025;
            }
         case 3:
            break;
         default:
            if (TeamManager.isTeammate(sawer, attacked)) {
               return 8024;
            }

            if (GangManager.inSameGang(sawer.getID(), attacked.getID())) {
               return 8025;
            }

            switch($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum()[attacked.getSelfEvilEnum().ordinal()]) {
            case 3:
            case 4:
               break;
            default:
               return 8022;
            }
         }

         return result;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum;
      if ($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum != null) {
         return var10000;
      } else {
         int[] var0 = new int[EvilEnum.values().length];

         try {
            var0[EvilEnum.Evil_Gray.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[EvilEnum.Evil_Orange.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[EvilEnum.Evil_Red.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[EvilEnum.Evil_White.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum = var0;
         return var0;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum;
      if ($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum != null) {
         return var10000;
      } else {
         int[] var0 = new int[PkEnum.values().length];

         try {
            var0[PkEnum.Mode_Force.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[PkEnum.Mode_Massacre.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[PkEnum.Mode_Peace.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum = var0;
         return var0;
      }
   }
}
