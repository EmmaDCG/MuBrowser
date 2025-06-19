package com.mu.config;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.collection.Collection;
import com.mu.game.model.dialog.DialogConfigManager;
import com.mu.game.model.equip.compositenew.CompositLabel;
import com.mu.game.model.equip.durability.DurabilityManager;
import com.mu.game.model.equip.newStone.StoneDataManager;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.fp.FunctionPreviewManager;
import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.gameguide.GameGuide;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gift.GiftSnManager;
import com.mu.game.model.guide.LevelUpManager;
import com.mu.game.model.guide.TaskActionManager;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.hallow.model.HallowModel;
import com.mu.game.model.item.GetItemWayManager;
import com.mu.game.model.item.box.BoxManager;
import com.mu.game.model.item.container.DeportExpandData;
import com.mu.game.model.item.model.ItemSort;
import com.mu.game.model.item.model.ItemSource;
import com.mu.game.model.item.other.ItemOtherManager;
import com.mu.game.model.luckyTurnTable.LuckyTurnTableManager;
import com.mu.game.model.mall.ShortcutBuy;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.market.MarketManager;
import com.mu.game.model.properties.levelData.PlayerLevelData;
import com.mu.game.model.shop.ShopConfigure;
import com.mu.game.model.spiritOfWar.SpiritTools;
import com.mu.game.model.trial.TrialConfigs;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.unit.attack.AttackDataManager;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.monster.MonsterStar;
import com.mu.game.model.unit.monster.worldboss.WorldBossManager;
import com.mu.game.model.unit.player.CreateProfessionInfo;
import com.mu.game.model.unit.player.achievement.AchievementManager;
import com.mu.game.model.unit.player.bluevip.BlueVip;
import com.mu.game.model.unit.player.extarget.ExtargetManager;
import com.mu.game.model.unit.player.fcm.FcmManager;
import com.mu.game.model.unit.player.offline.OfflineManager;
import com.mu.game.model.unit.player.sevenDay.SevenDayTreasureData;
import com.mu.game.model.unit.player.title.TitleManager;
import com.mu.game.model.unit.robot.RobotManager;
import com.mu.game.qq.pay.Qqpay;
import com.mu.game.top.TopManager;
import com.mu.utils.DFA;
import com.mu.utils.RndNames;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class SystemScript {
   private static HashMap scriptMap = new HashMap();
   public static final int Config_Message_Text = 1;
   public static final int Config_Keyword_Filter = 2;
   public static final int Config_Random_Name = 3;
   public static final int Config_World_Map = 4;
   public static final int Config_VariableConstant = 5;
   public static final int Config_Message_statname = 6;
   public static final int Config_Monster_Distribution = 7;
   public static final int Config_Monster_Star = 8;
   public static final int Config_PlayerLevelData = 9;
   public static final int Config_Npc_Distribution = 10;
   public static final int Config_SmallMap_Element = 11;
   public static final int Config_Item = 12;
   public static final int Config_Dialog = 13;
   public static final int Config_shop = 14;
   public static final int Config_ItemSort = 15;
   public static final int Config_RolePanelStat = 16;
   public static final int Config_Skill = 17;
   public static final int Config_Task = 18;
   public static final int Config_Creature_Template = 19;
   public static final int Config_Gang = 20;
   public static final int Config_Buff = 21;
   public static final int Config_EquipStatRule = 22;
   public static final int Config_Material_Template = 25;
   public static final int Config_Material_Distribution = 26;
   public static final int Config_Drop = 27;
   public static final int Config_Stone = 28;
   public static final int Config_Composite = 29;
   public static final int Config_Redeem = 30;
   public static final int Config_DropControl = 31;
   public static final int Config_Durability = 32;
   public static final int Config_Forging = 33;
   public static final int Config_ConstantDes = 34;
   public static final int Config_Fighting = 35;
   public static final int Config_VIP = 37;
   public static final int Config_Trigger = 38;
   public static final int Config_TaskAction = 39;
   public static final int Config_Chat = 40;
   public static final int Config_Fcm = 41;
   public static final int Config_LevelUpAction = 42;
   public static final int Config_Pet = 43;
   public static final int Config_DynamicMenu = 44;
   public static final int Config_Trial = 45;
   public static final int Config_Transfer = 46;
   public static final int Config_FunctionOpen = 47;
   public static final int Config_DefaultEquipment = 48;
   public static final int Config_WorldBoss = 49;
   public static final int Config_BoxItem = 50;
   public static final int Config_ExpandBackpage = 51;
   public static final int Config_Market = 52;
   public static final int Config_NotAutoTasks = 53;
   public static final int Config_ItemOther = 54;
   public static final int Config_Arrow = 55;
   public static final int Config_Mall = 56;
   public static final int Config_RewardHall = 57;
   public static final int Config_SnGift = 58;
   public static final int Config_ShortcutBuy = 59;
   public static final int Config_FunctionPreview = 60;
   public static final int Config_Shield = 61;
   public static final int Config_Create = 62;
   public static final int Config_Financing = 63;
   public static final int Config_Kfhd = 64;
   public static final int Config_FirstPay = 65;
   public static final int Config_CloseTest = 66;
   public static final int Config_Collection = 67;
   public static final int Config_GameGuide = 68;
   public static final int Config_BaiduClub = 69;
   public static final int Config_Title = 70;
   public static final int Config_TeHui = 71;
   public static final int Config_Robot = 72;
   public static final int Config_ItemWay = 73;
   public static final int Config_TxLevelUp = 74;
   public static final int Config_ExTarget = 75;
   public static final int Config_Yxlb = 76;
   public static final int Config_BlueVip = 77;
   public static final int Config_BlueVip_Icon = 78;
   public static final int Config_Qq_pay = 79;
   public static final int Config_BlueVip_Renew = 80;
   public static final int Config_Achievement = 81;
   public static final int Config_OffLine = 82;
   public static final int Config_BigPay = 83;
   public static final int Config_SevenDay = 84;
   public static final int Config_VipGift = 85;
   public static final int Config_TopGift = 86;
   public static final int Config_SpiritOfWar = 87;
   public static final int Config_Hallow = 88;
   public static final int Config_TanXian = 89;
   public static final int Config_DayPay = 90;
   public static final int Config_Friendly = 91;
   public static final int Config_ExpReduction = 92;
   public static final int Config_LuckyTurntable = 93;

   public static void addScript(int id, InputStream in) {
      scriptMap.put(id, in);
   }

   public static InputStream getScriptStrem(int id) {
      return (InputStream)scriptMap.get(id);
   }

   public static void initScript() throws Exception {
      Iterator it = scriptMap.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         int id = ((Integer)entry.getKey()).intValue();
         InputStream in = (InputStream)entry.getValue();
         loadScript(id, in);
         in.close();
      }

   }

   public static void clearScript() {
      scriptMap.clear();
   }

   public static void loadScript(int id, InputStream in) throws Exception {
      switch(id) {
      case 2:
         DFA.initial(in);
         break;
      case 3:
         RndNames.init(in);
         break;
      case 4:
         MapConfig.initWorldMapInfo(in);
         break;
      case 5:
         VariableConstant.init(in);
      case 6:
      case 9:
      case 12:
      case 16:
      case 17:
      case 18:
      case 19:
      case 22:
      case 23:
      case 24:
      case 25:
      case 27:
      case 30:
      case 31:
      case 33:
      case 36:
      case 37:
      case 38:
      case 43:
      case 46:
      case 56:
      case 57:
      case 61:
      case 63:
      case 89:
      default:
         break;
      case 7:
         MapConfig.initMonsterGroup(in);
         break;
      case 8:
         MonsterStar.initMonsterStar(in);
         break;
      case 10:
         MapConfig.initNpcInfo(in);
         break;
      case 11:
         MapConfig.initSmallMapElement(in);
         break;
      case 13:
         DialogConfigManager.initConfigs(in);
         break;
      case 14:
         ShopConfigure.init(in);
         break;
      case 15:
         ItemSort.init(in);
         break;
      case 20:
         GangManager.initGangData(in);
         break;
      case 21:
         BuffModel.init(in);
         break;
      case 26:
         MapConfig.initMaterialGroup(in);
         break;
      case 28:
         StoneDataManager.init(in);
         break;
      case 29:
         CompositLabel.init(in);
         break;
      case 32:
         DurabilityManager.init(in);
         break;
      case 34:
         ItemSource.init(in);
         break;
      case 35:
         AttackDataManager.init(in);
         break;
      case 39:
         TaskActionManager.initTaskAction(in);
         break;
      case 40:
         BroadcastManager.init(in);
         break;
      case 41:
         FcmManager.init(in);
         break;
      case 42:
         LevelUpManager.initAction(in);
         break;
      case 44:
         DynamicMenuManager.init(in);
         break;
      case 45:
         TrialConfigs.initTrial(in);
         break;
      case 47:
         FunctionOpenManager.init(in);
         break;
      case 48:
         Global.initDefaultEquitment(in);
         break;
      case 49:
         WorldBossManager.init(in);
         break;
      case 50:
         BoxManager.init(in);
         break;
      case 51:
         DeportExpandData.init(in);
         break;
      case 52:
         MarketManager.init(in);
         break;
      case 53:
         TaskActionManager.initNotAutoTask(in);
         break;
      case 54:
         ItemOtherManager.init(in);
         break;
      case 55:
         ArrowGuideManager.init(in);
         break;
      case 58:
         GiftSnManager.init(in);
         break;
      case 59:
         ShortcutBuy.init(in);
         break;
      case 60:
         FunctionPreviewManager.init(in);
         break;
      case 62:
         CreateProfessionInfo.init(in);
         break;
      case 64:
         ActivityManager.initKf(in);
         break;
      case 65:
         ActivityManager.initFirstPay(in);
         break;
      case 66:
         ActivityManager.initCloseTest(in);
         break;
      case 67:
         Collection.init(in);
         break;
      case 68:
         GameGuide.init(in);
         break;
      case 69:
         ActivityManager.initBaidu(in);
         break;
      case 70:
         TitleManager.init(in);
         break;
      case 71:
         ActivityManager.initTeHui(in);
         break;
      case 72:
         RobotManager.init(in);
         break;
      case 73:
         GetItemWayManager.init(in);
         break;
      case 74:
         ActivityManager.initTxLevelUp(in);
         break;
      case 75:
         ExtargetManager.init(in);
         break;
      case 76:
         ActivityManager.initYxlb(in);
         break;
      case 77:
         ActivityManager.initBlueVip(in);
         break;
      case 78:
         BlueVip.initBlueIcon(in);
         break;
      case 79:
         Qqpay.init(in);
         break;
      case 80:
         ActivityManager.initBlueVipRenew(in);
         break;
      case 81:
         AchievementManager.init(in);
         break;
      case 82:
         OfflineManager.init(in);
         break;
      case 83:
         ActivityManager.initBigPay(in);
         break;
      case 84:
         SevenDayTreasureData.init(in);
         break;
      case 85:
         ActivityManager.initVipGift(in);
         break;
      case 86:
         TopManager.initTopReward(in);
         break;
      case 87:
         SpiritTools.init(in);
         break;
      case 88:
         HallowModel.init(in);
         break;
      case 90:
         ActivityManager.initDayPay(in);
         break;
      case 91:
         FriendManager.init(in);
         break;
      case 92:
         PlayerLevelData.initExpReduction(in);
         break;
      case 93:
         LuckyTurnTableManager.init(in);
      }

   }
}
