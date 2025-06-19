package com.mu.game.dungeon;

import com.mu.game.CenterManager;
import com.mu.game.dungeon.imp.bigdevil.BigDevilSquare;
import com.mu.game.dungeon.imp.bigdevil.BigDevilSquareLevel;
import com.mu.game.dungeon.imp.bigdevil.BigDevilSquareTemplate;
import com.mu.game.dungeon.imp.bloodcastle.BloodCastle;
import com.mu.game.dungeon.imp.bloodcastle.BloodCastleLevel;
import com.mu.game.dungeon.imp.bloodcastle.BloodCastleTemplate;
import com.mu.game.dungeon.imp.devil.DevilSquare;
import com.mu.game.dungeon.imp.devil.DevilSquareLevel;
import com.mu.game.dungeon.imp.devil.DevilSquareTemplate;
import com.mu.game.dungeon.imp.discovery.Discovery;
import com.mu.game.dungeon.imp.discovery.DiscoveryInfo;
import com.mu.game.dungeon.imp.discovery.DiscoveryTemplate;
import com.mu.game.dungeon.imp.gangboss.GangBoss;
import com.mu.game.dungeon.imp.gangboss.GangBossTemplate;
import com.mu.game.dungeon.imp.luolan.Luolan;
import com.mu.game.dungeon.imp.luolan.LuolanManager;
import com.mu.game.dungeon.imp.luolan.LuolanMap;
import com.mu.game.dungeon.imp.luolan.LuolanTemplate;
import com.mu.game.dungeon.imp.molian.MoLian;
import com.mu.game.dungeon.imp.molian.MoLianMap;
import com.mu.game.dungeon.imp.molian.MoLianTemplate;
import com.mu.game.dungeon.imp.molian.MoLianleLevel;
import com.mu.game.dungeon.imp.personalboss.BossInfo;
import com.mu.game.dungeon.imp.personalboss.PersonalBoss;
import com.mu.game.dungeon.imp.personalboss.PersonalBossTemplate;
import com.mu.game.dungeon.imp.plot.Plot;
import com.mu.game.dungeon.imp.plot.PlotLevel;
import com.mu.game.dungeon.imp.plot.PlotTemplate;
import com.mu.game.dungeon.imp.redfort.RedFort;
import com.mu.game.dungeon.imp.redfort.RedFortTemplate;
import com.mu.game.dungeon.imp.temple.Temple;
import com.mu.game.dungeon.imp.temple.TempleLevel;
import com.mu.game.dungeon.imp.temple.TempleTemplate;
import com.mu.game.dungeon.imp.trial.Trial;
import com.mu.game.dungeon.imp.trial.TrialTemplate;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.map.Map;
import com.mu.game.model.rewardhall.vitality.VitalityTaskType;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.DunTicketPopup;
import com.mu.game.task.schedule.SaveGangContributionTask;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.dungeon.EnterDungeon;
import com.mu.io.game.packet.imp.monster.RefreshSingleBoss;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class DungeonManager {
   public static final int DUNGEON_TYPE_TEAM = 1;
   public static final int DUNGEON_TYPE_SINGLE = 2;
   public static final int Timing_Redfort = 1;
   public static final int Timing_BigDevil = 2;
   public static final int Timing_Luolan = 3;
   public static final int Timing_GangBoss = 4;
   public static final int keepForDrop = 300000;
   private static ConcurrentHashMap dungeonMap = new ConcurrentHashMap(16, 0.75F, 2);
   private static ConcurrentHashMap interrupInfoMaps = new ConcurrentHashMap(16, 0.75F, 2);
   private static int dungeonID = 1;
   public static final int Dungeon_BloodCastle = 1;
   public static final int Dungeon_DevilSquare = 2;
   public static final int Dungeon_Trial = 3;
   public static final int Dungeon_Temple = 4;
   public static final int Dungeon_RedFort = 5;
   public static final int Dungeon_BigDevil = 6;
   public static final int Dungeon_PersonalBoss = 7;
   public static final int Dungeon_Plot = 8;
   public static final int Dungeon_Luolan = 9;
   public static final int Dungeon_GangBoss = 10;
   public static final int Dungeon_MoLian = 11;
   public static final int Dungeon_Discovery = 12;
   public static final int Dungeon_Test = 99;

   public static void startCheckDungeon() {
      ThreadFixedPoolManager.POOL_OTHER.scheduleTask(new Runnable() {
         public void run() {
            DungeonManager.checkDungeons();
         }
      }, 0L, 1000L);
   }

   public static void checkDungeons() {
      long now = System.currentTimeMillis();
      Iterator it = interrupInfoMaps.values().iterator();

      while(it.hasNext()) {
         PlayerInterruptInfo info = (PlayerInterruptInfo)it.next();
         if (now - info.getInterruptTime() >= 300000L) {
            it.remove();
         }
      }

      it = dungeonMap.values().iterator();

      while(it.hasNext()) {
         Dungeon dungeon = (Dungeon)it.next();
         if (dungeon.canDestroy()) {
            try {
               dungeon.destroy();
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }
      }

   }

   public static synchronized int getID() {
      return dungeonID++;
   }

   public static void addDungeon(Dungeon dungeon) {
      dungeonMap.put(dungeon.getID(), dungeon);
   }

   private static void playerInDungeon(Dungeon dun, DungeonLevel level, Player player) {
      dun.addPlayer(player);
      player.switchMap(dun.getFirstMap(), dun.getFirstMap().getDefaultPoint());
      player.getDunLogsManager().finishDungeon(dun.getTemplate().getTemplateID(), 0);
      player.getDunLogsManager().addTotalNumber(dun.getTemplate().getTemplateID(), 0);
      UpdateMenu.updateDungeonMenu(player, dun.getTemplate().getTemplateID());
      EnterDungeon.enterDungeon(player, dun.getTemplate().getTemplateID(), dun.getTemplate().showDynamicMenu());
      Item ticket = level.getReqItem();
      if (ticket != null) {
         OperationResult or = player.getItemManager().deleteItemByModel(ticket.getModelID(), ticket.getCount(), 31);
         if (!or.isOk()) {
            Item gTicket = dun.getTemplate().getGlobalTicket();
            if (gTicket != null) {
               player.getItemManager().deleteItemByModel(gTicket.getModelID(), gTicket.getCount(), 31);
            }
         }
      }

      player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.FB_Enter_Count, dun.getTemplate().getTemplateID());
      player.getVitalityManager().onTaskEvent(VitalityTaskType.FB_Enter_Count, dun.getTemplate().getTemplateID(), 1);
   }

   private static int createBloodCastle(Player player) {
      BloodCastleTemplate template = (BloodCastleTemplate)DungeonTemplateFactory.getTemplate(1);
      BloodCastleLevel level = template.getPlayerFitLevel(player);
      int result = template.canEnter(player, level.getReqItem(), level);
      if (result != 1) {
         if (result == 14009 && noticeBuyTicket(player, level.getReqItem(), 1)) {
            return 14009;
         } else {
            SystemMessage.writeMessage(player, template.getCanotEnterMessage(result, level.getReqItem()), result);
            return result;
         }
      } else {
         BloodCastle castle = new BloodCastle(getID(), template, level);
         castle.init();
         addDungeon(castle);
         playerInDungeon(castle, level, player);
         castle.start();
         return 1;
      }
   }

   private static boolean noticeBuyTicket(Player player, Item ticket, int templateId) {
      ShowPopup.open(player, new DunTicketPopup(player.createPopupID(), templateId, ticket));
      return true;
   }

   private static int createTrial(Player player) {
      TrialTemplate template = (TrialTemplate)DungeonTemplateFactory.getTemplate(3);
      int level = player.getWarComment() + 1;
      int result = template.canEnter(player, (Item)null, level);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
         return result;
      } else {
         Trial trial = new Trial(getID(), template, level);
         trial.init();
         trial.addPlayer(player);
         addDungeon(trial);
         player.switchMap(trial.getFirstMap(), trial.getFirstMap().getDefaultPoint());
         EnterDungeon.enterDungeon(player, ((TrialTemplate)trial.getTemplate()).getTemplateID(), template.showDynamicMenu());
         player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.FB_Enter_Count, Integer.valueOf(3));
         player.getVitalityManager().onTaskEvent(VitalityTaskType.FB_Enter_Count, 3, 1);
         player.getDunLogsManager().addTotalNumber(3, 0);
         return 1;
      }
   }

   private static int enterTemple(Player player) {
      TempleTemplate template = (TempleTemplate)DungeonTemplateFactory.getTemplate(4);
      TempleLevel tl = template.getFitLevel(player);
      Temple temple = template.getTemple(tl.getLevel());
      int result = template.canEnter(player, tl.getReqItem(), new Object[0]);
      if (result != 1) {
         if (result == 14009 && noticeBuyTicket(player, tl.getReqItem(), 4)) {
            return 14009;
         } else {
            SystemMessage.writeMessage(player, template.getCanotEnterMessage(result, tl.getReqItem()), result);
            return result;
         }
      } else {
         Item ticket = tl.getReqItem();
         if (ticket != null) {
            player.getItemManager().deleteItemByModel(ticket.getModelID(), ticket.getCount(), 31);
         }

         temple.addPlayer(player);
         player.switchMap(temple.getFirstMap(), temple.getFirstMap().getDefaultPoint());
         EnterDungeon.enterDungeon(player, template.getTemplateID(), template.showDynamicMenu());
         return 1;
      }
   }

   private static int enterPersonalBoss(Player player, int bossId) {
      PersonalBossTemplate template = (PersonalBossTemplate)DungeonTemplateFactory.getTemplate(7);
      BossInfo info = template.getBossInfo(bossId);
      if (info == null) {
         return 14001;
      } else {
         int result = template.canEnter(player, info.getTicketItem(), info);
         if (result != 1) {
            if (result == 14009 && noticeBuyTicket(player, info.getTicketItem(), 7)) {
               return 14009;
            } else {
               SystemMessage.writeMessage(player, template.getCanotEnterMessage(result, info), result);
               return result;
            }
         } else {
            PersonalBoss pb = new PersonalBoss(getID(), template, info.getBossId());
            pb.init();
            pb.addPlayer(player);
            addDungeon(pb);
            player.switchMap(pb.getFirstMap(), pb.getFirstMap().getDefaultPoint());
            EnterDungeon.enterDungeon(player, ((PersonalBossTemplate)pb.getTemplate()).getTemplateID(), template.showDynamicMenu());
            player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.FB_Enter_Count, Integer.valueOf(7));
            player.getDunLogsManager().finishDungeon(7, bossId);
            player.getDunLogsManager().addTotalNumber(7, bossId);
            int ticketId = info.getTicket();
            if (ticketId != -1) {
               player.getItemManager().deleteItemByModel(ticketId, info.getTicketNumber(), 31);
            }

            RefreshSingleBoss.singleRefresh(info, player);
            return 1;
         }
      }
   }

   private static int enterBigDevil(Player player) {
      BigDevilSquareTemplate template = (BigDevilSquareTemplate)DungeonTemplateFactory.getTemplate(6);
      BigDevilSquareLevel level = template.getPlayerFitLevel(player);
      int result = template.canEnter(player, (Item)null, level);
      if (result != 1) {
         SystemMessage.writeMessage(player, template.getCanotEnterMessage(result, (Item)null), result);
         return result;
      } else {
         BigDevilSquare square = template.getBigDevilManager().getBigDevilSquare(player);
         if (square == null) {
            SystemMessage.writeMessage(player, 14001);
            return 14001;
         } else if (square.getPlayerMap().size() >= template.getMaxPlayer()) {
            SystemMessage.writeMessage(player, 14045);
            return 14045;
         } else {
            square.addPlayer(player);
            player.switchMap(square.getFirstMap(), square.getFirstMap().getDefaultPoint());
            EnterDungeon.enterDungeon(player, template.getTemplateID(), template.showDynamicMenu());
            player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.FB_Enter_Count, Integer.valueOf(6));
            return 1;
         }
      }
   }

   private static int enterLuoLan(Player player) {
      LuolanTemplate template = (LuolanTemplate)DungeonTemplateFactory.getTemplate(9);
      Gang gang = player.getGang();
      Luolan luolan = getLuolanManager().getLuoLan();
      int result = template.canEnter(player, (Item)null, gang, luolan);
      if (result != 1) {
         SystemMessage.writeMessage(player, template.getCanotEnterMessage(result, (Item)null), result);
         return result;
      } else {
         int top = luolan.getGangTop(gang.getId());
         Point point = luolan.getBorPoint(top);
         if (point == null) {
            return 9049;
         } else {
            luolan.addPlayer(player);
            LuolanMap map = (LuolanMap)luolan.getFirstMap();
            player.switchMap(map, point);
            UpdateMenu.updateDungeonMenu(player, 9);
            EnterDungeon.enterDungeon(player, 9, true);
            return 1;
         }
      }
   }

   private static int enterGangBoss(Player player, GangBoss gangBoss) {
      GangBossTemplate template = (GangBossTemplate)gangBoss.getTemplate();
      int result = template.canEnter(player, (Item)null, new Object[0]);
      if (result != 1) {
         SystemMessage.writeMessage(player, template.getCanotEnterMessage(result, (Item)null), result);
         return result;
      } else {
         gangBoss.addPlayer(player);
         DungeonMap map = gangBoss.getFirstMap();
         player.switchMap(map, map.getDefaultPoint());
         EnterDungeon.enterDungeon(player, 10, false);
         player.getDunLogsManager().finishDungeon(10, 0);
         return 1;
      }
   }

   private static synchronized int enterMoLian(Player player, int level) {
      MoLianTemplate template = (MoLianTemplate)DungeonTemplateFactory.getTemplate(11);
      int result = template.canEnter(player, (Item)null, level);
      if (result != 1) {
         SystemMessage.writeMessage(player, template.getCanotEnterMessage(result, (Item)null), result);
         return result;
      } else {
         MoLian molian = template.getManager().getOrCreateMoLian(player);
         if (molian != null) {
            GangMember member = GangManager.getMember(player.getID());
            MoLianleLevel ml = template.getPlayerFitLevel(player);
            if (member != null && ml != null) {
               member.reduceContribution(ml.getContribution());
               SaveGangContributionTask.addMember(member.getId(), member.getCurContribution(), member.getHisContribution());
               molian.addPlayer(player);
               DungeonPlayerInfo pInfo = molian.getDungeonPlayerInfo(player.getID());
               if (pInfo != null) {
                  pInfo.setCostTime(0);
               }

               MoLianMap map = molian.getMoLianMap();
               player.switchMap(map, map.getDefaultPoint());
               EnterDungeon.enterDungeon(player, 11, false);
               player.getDunLogsManager().finishDungeon(11, 0);
               return 1;
            }
         }

         return -1;
      }
   }

   private static int enterRedFort(Player player) {
      RedFortTemplate template = (RedFortTemplate)DungeonTemplateFactory.getTemplate(5);
      int result = 1;
      RedFort redFort = template.getRedFortManager().getRedFort();
      if (redFort == null) {
         result = 14001;
      } else {
         result = template.canEnter(player, (Item)null);
         if (result == 1) {
            if (redFort.isBegin()) {
               SystemMessage.writeMessage(player, 14039);
               return 14039;
            }

            redFort.playerIn(player);
            player.switchMap(redFort.getRedfFortMap(1), redFort.getRedfFortMap(1).getBornPoint());
            EnterDungeon.enterDungeon(player, template.getTemplateID(), template.showDynamicMenu());
            player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.FB_Enter_Count, Integer.valueOf(5));
         } else {
            SystemMessage.writeMessage(player, template.getCanotEnterMessage(result, (Item)null), result);
         }
      }

      return result;
   }

   private static int enterPlot(Player player, int plotId) {
      PlotTemplate template = (PlotTemplate)DungeonTemplateFactory.getTemplate(8);
      PlotLevel pl = template.getPlotLevel(plotId);
      if (pl == null) {
         return 14001;
      } else {
         int result = template.canEnter(player, (Item)null, pl);
         if (result != 1) {
            SystemMessage.writeMessage(player, result);
            return result;
         } else {
            Plot plot = new Plot(getID(), template, pl.getPlotId());
            plot.init();
            plot.addPlayer(player);
            addDungeon(plot);
            player.switchMap(plot.getFirstMap(), plot.getFirstMap().getDefaultPoint());
            return 1;
         }
      }
   }

   private static int createDevilSquare(Player player) {
      DevilSquareTemplate template = (DevilSquareTemplate)DungeonTemplateFactory.getTemplate(2);
      DevilSquareLevel level = template.getPlayerFitLevel(player);
      int result = template.canEnter(player, level.getReqItem(), level);
      if (result != 1) {
         if (result == 14009 && noticeBuyTicket(player, level.getReqItem(), 2)) {
            return 14009;
         } else {
            SystemMessage.writeMessage(player, template.getCanotEnterMessage(result, level.getReqItem()), result);
            return result;
         }
      } else {
         DevilSquare square = new DevilSquare(getID(), template, level);
         square.init();
         addDungeon(square);
         playerInDungeon(square, level, player);
         return 1;
      }
   }

   public static synchronized int createAndEnterDungeon(Player player, int templateID, Object... args) {
      if (templateID != 8 && player.isFighting()) {
         SystemMessage.writeMessage(player, 1048);
         return 1048;
      } else {
         switch(templateID) {
         case 1:
            return createBloodCastle(player);
         case 2:
            return createDevilSquare(player);
         case 3:
            return createTrial(player);
         case 4:
            return enterTemple(player);
         case 5:
            return enterRedFort(player);
         case 6:
            return enterBigDevil(player);
         case 7:
            return enterPersonalBoss(player, ((Integer)args[0]).intValue());
         case 8:
            return enterPlot(player, ((Integer)args[0]).intValue());
         case 9:
            return enterLuoLan(player);
         case 10:
            return enterGangBoss(player, (GangBoss)args[0]);
         case 11:
            return enterMoLian(player, ((Integer)args[0]).intValue());
         default:
            return 14001;
         }
      }
   }

   public static void addInterruptInfo(PlayerInterruptInfo info) {
      interrupInfoMaps.put(info.getPlayerID(), info);
   }

   public static void removeDungeon(int dungeonID) {
      dungeonMap.remove(dungeonID);
   }

   public static Dungeon getDungeon(int dungeonID) {
      return (Dungeon)dungeonMap.get(dungeonID);
   }

   public static PlayerInterruptInfo removeInterruptInfo(long playerID) {
      return (PlayerInterruptInfo)interrupInfoMaps.remove(playerID);
   }

   public static void delayLeft(long rid, int dunId) {
      Player player = CenterManager.getPlayerByRoleID(rid);
      if (player != null) {
         Dungeon dun = getDungeon(dunId);
         if (dun != null) {
            DungeonMap map = player.getDungeonMap();
            if (map != null) {
               Dungeon tmpDun = map.getDungeon();
               if (tmpDun != null && tmpDun.getID() == dun.getID()) {
                  dun.exitForInitiative(player, true);
               }

            }
         }
      }
   }

   public static LuolanManager getLuolanManager() {
      LuolanTemplate template = (LuolanTemplate)DungeonTemplateFactory.getTemplate(9);
      return template.getManager();
   }

   public static synchronized void enterDiscovery(Player player, int discoveyLevel) {
      DiscoveryTemplate template = getDiscoveryTemplate();
      DiscoveryInfo di = template.getRndDiscoveryInfo();
      if (di != null) {
         int result = template.canEnter(player, (Item)null, new Object[0]);
         if (result == 1) {
            Discovery discovery = new Discovery(getID(), template, di, discoveyLevel);
            discovery.init();
            Map map = discovery.getFirstMap();
            player.switchMap(map, map.getDefaultPoint());
            addDungeon(discovery);
            EnterDungeon.enterDungeon(player, template.getTemplateID(), template.showDynamicMenu());
         }
      }
   }

   public static DiscoveryTemplate getDiscoveryTemplate() {
      return (DiscoveryTemplate)DungeonTemplateFactory.getTemplate(12);
   }

   public static HashMap getCanSellMap(Player player) {
      HashMap map = new HashMap();
      BloodCastleTemplate bt = (BloodCastleTemplate)DungeonTemplateFactory.getTemplate(1);
      BloodCastleLevel bl = bt.getPlayerFitLevel(player);
      if (bl != null) {
         int[] in = bl.getCanSellItem();
         if (in != null) {
            for(int i = 0; i < in.length; ++i) {
               map.put(in[i], true);
            }
         }
      }

      DevilSquareTemplate dt = (DevilSquareTemplate)DungeonTemplateFactory.getTemplate(2);
      DevilSquareLevel dl = dt.getPlayerFitLevel(player);
      if (dl != null) {
         int[] in = dl.getCanSellItem();
         if (in != null) {
            for(int i = 0; i < in.length; ++i) {
               map.put(in[i], true);
            }
         }
      }

      return map;
   }
}
