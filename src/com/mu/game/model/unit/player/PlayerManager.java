package com.mu.game.model.unit.player;

import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.config.VariableConstant;
import com.mu.db.log.IngotChangeType;
import com.mu.db.log.LogDBManager;
import com.mu.db.log.LogManager;
import com.mu.db.manager.PayDBManager;
import com.mu.executor.imp.player.SaveBindIngotExector;
import com.mu.executor.imp.player.SaveIngotExecutor;
import com.mu.executor.imp.player.SavePayLogExecutor;
import com.mu.executor.imp.player.SaveRedeemPointExector;
import com.mu.game.CenterManager;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.guide.LevelUpManager;
import com.mu.game.model.packet.RolePacketService;
import com.mu.game.model.properties.CreatureProperties;
import com.mu.game.model.properties.levelData.PlayerLevelData;
import com.mu.game.model.rewardhall.vitality.VitalityTaskType;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.fcm.FcmManager;
import com.mu.game.model.unit.player.fcm.PlayerPushInfo;
import com.mu.game.model.unit.player.tips.SystemFunctionTipConfig;
import com.mu.game.model.vip.PlayerVIPManager;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import com.mu.io.game.packet.imp.player.PlayerLevelUp;
import com.mu.io.game.packet.imp.sys.AddMonsterExp;
import com.mu.io.game.packet.imp.sys.RightMessage;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
   public static boolean isLocalSever(Player player) {
      return true;
   }

   public static int allocatePotential(Player player, int str, int dex, int con, int intell) {
      int result = canAllocate(player, str, dex, con, intell);
      return result != 1 ? result : changeBasicProperty(player, str, dex, con, intell, true);
   }

   public static int changeBasicProperty(Player player, int str, int dex, int con, int intell, boolean changePotencial) {
      int result = 1;
      int all = str + dex + con + intell;
      player.getProperty().changeLoadedStat(StatEnum.STR, str, false);
      player.getProperty().changeLoadedStat(StatEnum.DEX, dex, false);
      player.getProperty().changeLoadedStat(StatEnum.CON, con, false);
      player.getProperty().changeLoadedStat(StatEnum.INT, intell, true);
      if (changePotencial) {
         player.setPoSTR(player.getPoSTR() + str);
         player.setPoDEX(player.getPoDEX() + dex);
         player.setPoCON(player.getPoCON() + con);
         player.setPoINT(player.getPoINT() + intell);
      }

      if (changePotencial) {
         player.setPotential(player.getPotential() - all);
      }

      RolePacketService.noticeGatewayWhenPotentialChange(player);
      dealWhenBasicPropertyChange(player, changePotencial);
      return result;
   }

   public static void dealWhenBasicPropertyChange(Player player, boolean changePotencial) {
      List stats = new ArrayList();
      boolean change = player.getEquipment().calWhenBasicPropertyChange();
      if (changePotencial) {
         stats.add(StatEnum.POTENTIAL);
      }

      if (!change) {
         stats.addAll(StatList2Client.getFirstPanelStats());
      }

      if (stats.size() > 0) {
         PlayerAttributes.sendToClient(player, (List)stats);
      }

      stats.clear();
      stats = null;
   }

   public static int canAllocate(Player player, int str, int dex, int con, int intell) {
      if (str < 0) {
         return 1006;
      } else if (dex < 0) {
         return 1006;
      } else if (con < 0) {
         return 1006;
      } else if (intell < 0) {
         return 1006;
      } else {
         int all = str + dex + con + intell;
         if (all <= 0) {
            return 1006;
         } else if (player.getPotential() < all) {
            return 1005;
         } else if (player.getStatValue(StatEnum.STR) + str > 3000) {
            return 1007;
         } else if (player.getStatValue(StatEnum.DEX) + dex > 3000) {
            return 1007;
         } else if (player.getStatValue(StatEnum.CON) + con > 3000) {
            return 1007;
         } else {
            return player.getStatValue(StatEnum.INT) + intell > 3000 ? 1007 : 1;
         }
      }
   }

   public static int washPotential(Player player, boolean needItem) {
      if (player.getPoSTR() < 1 && player.getPoDEX() < 1 && player.getPoCON() < 1 && player.getPoINT() < 1) {
         return 1008;
      } else {
         int result = 1;
         if (needItem) {
            result = player.getItemManager().deleteItemByModel(2143, 1, 20).getResult();
         }

         if (result != 1) {
            return result;
         } else {
            int all = player.getPoSTR() + player.getPoDEX() + player.getPoCON() + player.getPoINT();
            CreatureProperties property = player.getProperty();
            property.changeLoadedStat(StatEnum.STR, -player.getPoSTR(), false);
            property.changeLoadedStat(StatEnum.DEX, -player.getPoDEX(), false);
            property.changeLoadedStat(StatEnum.CON, -player.getPoCON(), false);
            property.changeLoadedStat(StatEnum.INT, -player.getPoINT(), true);
            player.setPoSTR(0);
            player.setPoDEX(0);
            player.setPoCON(0);
            player.setPoINT(0);
            player.setPotential(player.getPotential() + all);
            RolePacketService.noticeGatewayWhenPotentialChange(player);
            dealWhenBasicPropertyChange(player, true);
            return 1;
         }
      }
   }

   public static boolean hasEnoughMoney(Player player, int money) {
      return player.getMoney() >= money;
   }

   public static void addBindMoney(Player player, int bindMoney) {
      addMoney(player, bindMoney);
   }

   private static int getFcmMoney(Player player, int money) {
      PlayerPushInfo info = FcmManager.getPushInfo(player.getID());
      if (info != null && info.getPushId() >= 3) {
         return info.getPushId() >= 3 && info.getPushId() < 7 ? money / 2 : 0;
      } else {
         return money;
      }
   }

   public static void addMoney(Player player, int money) {
      if (money < 0) {
         System.err.println("金钱系统出现问题  ，添加值为 " + money);
      }

      if (Global.isFcm() && player.getUser().isNeedAntiAddiction()) {
         money = getFcmMoney(player, money);
      }

      long allMoney = (long)player.getMoney() * 1L + (long)money * 1L;
      if (allMoney >= 2147483647L) {
         money = Integer.MAX_VALUE - player.getMoney() - 1;
      }

      player.setMoney(player.getMoney() + money);
      player.getTaskManager().onEventCheckValue(TargetType.ValueType.MONEY);
      PlayerAttributes.sendToClient(player, StatEnum.MONEY);
      RightMessage.pushRightMessage(player, MessageText.getText(20003).replace("%n%", String.valueOf(money)));
   }

   public static void addIngot(Player player, int ingot, int addType) {
      if (ingot < 0) {
         System.out.println("添加钻石出错 ，添加值 = " + ingot);
      } else {
         long allIngot = (long)(player.getIngot() + ingot);
         if (allIngot >= 2147483647L) {
            ingot = Integer.MAX_VALUE - player.getIngot();
         }

         player.getUser().setIngot(player.getIngot() + ingot);
         SaveIngotExecutor.saveIngot(player);
         PlayerAttributes.sendToClient(player, StatEnum.INGOT);
         LogManager.addIngotLog(player, ingot, addType);
      }
   }

   public static void addBindIngot(Player player, int bindIngot, int addType) {
      if (bindIngot < 0) {
         System.out.println("扣除绑定钻石出错 ，值 = " + bindIngot);
      } else {
         long allBindIngot = (long)(player.getBindIngot() + bindIngot);
         if (allBindIngot >= 2147483647L) {
            bindIngot = Integer.MAX_VALUE - player.getBindIngot();
         }

         player.setBindIngot(player.getBindIngot() + bindIngot);
         SaveBindIngotExector.saveBindIngot(player);
         PlayerAttributes.sendToClient(player, StatEnum.BIND_INGOT);
         LogManager.addBindIngotLog(player, bindIngot, addType);
      }
   }

   public static int reduceMoney(Player player, int money) {
      if (money < 0) {
         System.out.println("减少金钱出错 ，减少值 = " + money);
         return 2;
      } else {
         int result = hasEnoughMoney(player, money) ? 1 : 1011;
         if (result != 1) {
            return result;
         } else {
            player.setMoney(player.getMoney() - money);
            player.getTaskManager().onEventCheckValue(TargetType.ValueType.MONEY);
            PlayerAttributes.sendToClient(player, StatEnum.MONEY);
            return 1;
         }
      }
   }

   public static int reduceBindIngot(Player player, int bindIngot, IngotChangeType reduceType, String reduceDetail) {
      if (bindIngot < 0) {
         System.out.println("减少绑定钻石出错 ，减少值 = " + bindIngot);
         return 2;
      } else if (player.getBindIngot() < bindIngot) {
         return 1015;
      } else {
         player.setBindIngot(player.getBindIngot() - bindIngot);
         SaveBindIngotExector.saveBindIngot(player);
         PlayerAttributes.sendToClient(player, StatEnum.BIND_INGOT);
         player.getVitalityManager().onTaskEvent(VitalityTaskType.XFBZ, 0, bindIngot);
         LogManager.reduceBindIngotLog(player, bindIngot, reduceType.getType(), reduceDetail);
         return 1;
      }
   }

   public static int reduceIngot(Player player, int ingot, IngotChangeType ingotReduceType, String reduceDetail) {
      if (ingot < 0) {
         System.out.println("扣除钻石出错 ，值 = " + ingot);
         return 2;
      } else if (ingot == 0) {
         return 1;
      } else if (player.getIngot() < ingot) {
         return 1015;
      } else {
         player.getUser().setIngot(player.getIngot() - ingot);
         PlayerVIPManager pvm = player.getVIPManager();
         if (IngotChangeType.PersonTransaction != ingotReduceType && IngotChangeType.Market != ingotReduceType && IngotChangeType.LuckyTurnTable != ingotReduceType && pvm != null && !pvm.isTimeOut()) {
            player.getVIPManager().increaseExp(ingot, true);
         }

         SaveIngotExecutor.saveIngot(player);
         PlayerAttributes.sendToClient(player, StatEnum.INGOT);
         LogManager.reduceIngotLog(player, ingot, ingotReduceType.getType(), reduceDetail);
         Global.getLoginParser().doConsume(player, ingot);
         return 1;
      }
   }

   public static boolean hasEnoughRedeemPoints(Player player, int points) {
      return player.getRedeemPoints() >= points;
   }

   public static int reduceRedeemPoints(Player player, int points) {
      if (!hasEnoughRedeemPoints(player, points)) {
         return 1016;
      } else {
         player.setRedeemPoints(player.getRedeemPoints() - points);
         PlayerAttributes.sendToClient(player, StatEnum.REDEEM_POINTS);
         SaveRedeemPointExector.saveRedeemPoint(player);
         return 1;
      }
   }

   public static void addRedeemPoints(Player player, int points) {
      long allPoints = (long)(player.getRedeemPoints() + points);
      if (allPoints >= 2147483647L) {
         points = Integer.MAX_VALUE - player.getRedeemPoints();
      }

      if (points >= 1) {
         player.setRedeemPoints(player.getRedeemPoints() + points);
         PlayerAttributes.sendToClient(player, StatEnum.REDEEM_POINTS);
         SaveRedeemPointExector.saveRedeemPoint(player);
      }
   }

   private static long getFcmExp(Player player, long exp) {
      PlayerPushInfo info = FcmManager.getPushInfo(player.getID());
      if (info != null && info.getPushId() >= 3) {
         return info.getPushId() >= 3 && info.getPushId() < 7 ? exp / 2L : 0L;
      } else {
         return exp;
      }
   }

   public static long addExp(Player player, long exp, long monsterId) {
      if (exp == 0L) {
         return 0L;
      } else {
         if (Global.isFcm() && player.getUser().isNeedAntiAddiction()) {
            exp = getFcmExp(player, exp);
         }

         boolean isLevelMax = isMaxLevel(player);
         int maxLevel = getMaxLevel(player);
         long maxExp = Long.MAX_VALUE;
         if (player.getLevel() >= maxLevel) {
            maxExp = PlayerLevelData.getNeedExp(maxLevel);
         }

         if (player.getCurretntExp() >= maxExp) {
            return 0L;
         } else {
            exp = Math.min(exp, Math.min(Long.MAX_VALUE - player.getCurretntExp(), maxExp - player.getCurretntExp()));
            if (exp < 1L) {
               return 0L;
            } else {
               player.setCurrentExp(player.getCurretntExp() + exp);
               if (!isLevelMax) {
                  playerLevelUp(player, true);
               }

               PlayerAttributes.sendToClient(player, StatEnum.EXP);
               if (monsterId != -1L) {
                  AddMonsterExp.sendToClient(player, exp, monsterId);
               }

               return exp;
            }
         }
      }
   }

   public static int playerLevelUp(Player player, boolean auto) {
      boolean isLevelMax = isMaxLevel(player);
      if (isLevelMax) {
         return 1017;
      } else {
         int maxLevel = getMaxLevel(player);
         long everyNeedExp = PlayerLevelData.getNeedExp(player.getLevel());
         if (player.getCurretntExp() < everyNeedExp) {
            return 1019;
         } else {
            int nextLevel = player.getLevel() + 1;
            long allNeedExp = everyNeedExp;
            if (auto && nextLevel < maxLevel) {
               while(allNeedExp < player.getCurretntExp()) {
                  everyNeedExp = PlayerLevelData.getNeedExp(nextLevel);
                  if (player.getCurretntExp() - allNeedExp < everyNeedExp) {
                     break;
                  }

                  allNeedExp += everyNeedExp;
                  ++nextLevel;
                  if (nextLevel >= maxLevel) {
                     break;
                  }
               }
            }

            long remainExp = player.getCurretntExp() - allNeedExp;
            remainExp = Math.min(remainExp, PlayerLevelData.getNeedExp(nextLevel) - 1L);
            levelUpDetail(player, nextLevel, player.getCurretntExp() - remainExp);
            PlayerLevelUp.sendToClient(player);
            return 1;
         }
      }
   }

   public static void levelUpDetail(Player player, int newLevel, long deductionExp) {
      try {
         int oldLevel = player.getLevel();
         player.setLevel(newLevel);
         player.setCurrentExp(player.getCurretntExp() - deductionExp);
         setPlayerLevelData(player, oldLevel);
         int addPotential = 0;
         if (newLevel > VariableConstant.MinLevelToAddPotential) {
            addPotential = Profession.getPotentailByProID(player.getProfessionID());
            if (newLevel > oldLevel + 1) {
               addPotential *= newLevel - (oldLevel > VariableConstant.MinLevelToAddPotential ? oldLevel : VariableConstant.MinLevelToAddPotential);
            }
         }

         if (addPotential > 0) {
            player.addPotential(addPotential, false, false);
         }

         player.fullResume();
         PlayerAttributes.sendToClient(player);
         RolePacketService.noticeGatewaySaveLevelChange(player);
         SystemFunctionTipConfig.potencailTips(player);
         LevelUpManager.levelUp(player, oldLevel, newLevel);
         player.getTaskManager().onEventCheckValue(TargetType.ValueType.RoleLevel);
         player.getFinancingManager().onPlayerLevelUp();
         player.beginTransfer();
         FunctionOpenManager.checkLevel(player, oldLevel, newLevel);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   private static void setPlayerLevelData(Player player, int oldLevel) {
      PlayerLevelData newData = PlayerLevelData.getLevelData(player.getProType(), player.getLevel());
      PlayerLevelData oldData = PlayerLevelData.getLevelData(player.getProType(), oldLevel);
      int str = player.getProperty().getLoadedStat(StatEnum.STR) + (newData.getStr() - oldData.getStr());
      int dex = player.getProperty().getLoadedStat(StatEnum.DEX) + (newData.getDex() - oldData.getDex());
      int con = player.getProperty().getLoadedStat(StatEnum.CON) + (newData.getCon() - oldData.getCon());
      int intell = player.getProperty().getLoadedStat(StatEnum.INT) + (newData.getIntell() - oldData.getIntell());
      int minAtk = player.getProperty().getLoadedStat(StatEnum.ATK_MIN) + (newData.getMinAtk() - oldData.getMinAtk());
      int maxAtk = player.getProperty().getLoadedStat(StatEnum.ATK_MAX) + (newData.getMaxAtk() - oldData.getMaxAtk());
      int def = player.getProperty().getLoadedStat(StatEnum.DEF) + (newData.getDef() - oldData.getDef());
      int hit = player.getProperty().getLoadedStat(StatEnum.HIT) + (newData.getHit() - oldData.getHit());
      int avd = player.getProperty().getLoadedStat(StatEnum.AVD) + (newData.getAvd() - oldData.getAvd());
      int maxHp = player.getProperty().getLoadedStat(StatEnum.MAX_HP) + (newData.getMaxHp() - oldData.getMaxHp());
      int maxMp = player.getProperty().getLoadedStat(StatEnum.MAX_MP) + (newData.getMaxMp() - oldData.getMaxMp());
      int maxSD = player.getProperty().getLoadedStat(StatEnum.MAX_SD) + (newData.getMaxSD() - oldData.getMaxSD());
      int maxAP = player.getProperty().getLoadedStat(StatEnum.MAX_AP) + (newData.getMaxAP() - oldData.getMaxAP());
      player.getProperty().playerInits(str, dex, con, intell, minAtk, maxAtk, def, hit, avd, maxHp, maxMp, maxSD, maxAP, true, newData);
   }

   public static boolean isMaxLevel(Player player) {
      return player.getLevel() >= getMaxLevel(player);
   }

   public static int getMaxLevel(Player player) {
      return getRoleMaxLevel();
   }

   public static int getRoleMaxLevel() {
      return Math.min(VariableConstant.Max_Level_2Exp, VariableConstant.Max_Level_2Limit);
   }

   public static void pay(String orderId, String userName, int serverId, int money, int gold, String time, String des, int type, int addType) {
      Player player = CenterManager.getPlayerByUserName(userName, serverId);
      if (player != null) {
         addIngot(player, gold, addType);
         player.getUser().addPay(gold, Time.getTimeStringToMills(time));
         ActivityManager.refreshDynamicMenu(1, player);
         UpdateMenu.update(player, 29);
         UpdateMenu.update(player, 34);
         SavePayLogExecutor.savePayLog(orderId, userName, serverId, (float)money, gold, time, player);
      } else {
         LogDBManager.addIngotLog(userName, -1L, "", money, addType, serverId, Global.getServerID());
         PayDBManager.intoPayLog(orderId, userName, serverId, (float)money, gold, time, des, type, "CNY");
      }

   }
}
