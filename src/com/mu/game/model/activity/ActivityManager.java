package com.mu.game.model.activity;

import com.mu.db.manager.GlobalDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.activity.imp.kfhd.boss.ActivityBoss;
import com.mu.game.model.activity.imp.test.ActivityTest;
import com.mu.game.model.activity.imp.tx.bluerenew.ActivityBlueRenew;
import com.mu.game.model.activity.imp.tx.bluevip.ActivityBlueVip;
import com.mu.game.model.activity.shell.BaiduShell;
import com.mu.game.model.activity.shell.BigPayShell;
import com.mu.game.model.activity.shell.BlueVipRenewShell;
import com.mu.game.model.activity.shell.BlueVipShell;
import com.mu.game.model.activity.shell.CollectionShell;
import com.mu.game.model.activity.shell.DayPayShell;
import com.mu.game.model.activity.shell.FirstPayShell;
import com.mu.game.model.activity.shell.KfShell;
import com.mu.game.model.activity.shell.TeHuiShell;
import com.mu.game.model.activity.shell.TxLevelUpShell;
import com.mu.game.model.activity.shell.VipGiftShell;
import com.mu.game.model.activity.shell.YxlbShell;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.bluevip.BlueVip;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityManager {
   public static final int Activity_FirstPay = 1;
   public static final int Activity_Boss = 2;
   public static final int Activity_Level = 3;
   public static final int Activity_Pet = 4;
   public static final int Activity_Test = 5;
   public static final int Activity_Collection = 127;
   public static final int Activity_TeHui = 6;
   public static final int Activity_Equip = 7;
   public static final int Activity_TxLevelUp = 8;
   public static final int Activity_Yxlb = 9;
   public static final int Activity_BlueVip = 10;
   public static final int Activity_BlueVipFinish = 11;
   public static final int Activity_BlueVipRenew = 12;
   public static final int Activity_BigPay = 13;
   public static final int Activity_ZhuoYue = 14;
   public static final int Activity_QiangHua = 15;
   public static final int Activity_VipGift = 16;
   public static final int Activity_DayPay = 17;
   public static final int Status_NotRequire = 0;
   public static final int Status_CanReceive = 1;
   public static final int Status_AlreadyReceive = 2;
   public static final int Shell_FirstPay = 1;
   public static final int Shell_Kfhd = 2;
   public static final int Shell_CloseTest = 3;
   public static final int Shell_TeHui = 4;
   public static final int Shell_Collection = 127;
   public static final int Shell_BaiduClub = 5;
   public static final int Shell_TxLevelUp = 6;
   public static final int Shell_Yxlb = 7;
   public static final int Shell_BlueVip = 8;
   public static final int Shell_BlueVipRenew = 10;
   public static final int Shell_BigPay = 11;
   public static final int Shell_VipGift = 12;
   public static final int Shell_DayPay = 13;
   public static final int Receive_RoleNotDaily = 1;
   public static final int Receive_RoleDaily = 2;
   public static final int Receive_UserNotDaily = 3;
   public static final int Receive_UserDaily = 4;
   private static ConcurrentHashMap activityMap = new ConcurrentHashMap(8, 0.75F, 2);
   private static ConcurrentHashMap elementMap = new ConcurrentHashMap(8, 0.75F, 2);
   private static ConcurrentHashMap serverLeftMap = new ConcurrentHashMap(8, 0.75F, 2);
   private static ConcurrentHashMap shellMap = new ConcurrentHashMap(8, 0.75F, 2);
   private static Logger logger = LoggerFactory.getLogger(ActivityManager.class);

   public static void initFirstPay(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(1);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initCloseTest(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(3);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initDayPay(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(13);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initTxLevelUp(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(6);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initYxlb(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(7);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initBigPay(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(11);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initVipGift(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(12);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initBlueVip(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(8);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initBlueVipRenew(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(10);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initCollection(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(127);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initBaidu(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(5);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initTeHui(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(4);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static void initKf(InputStream in) throws Exception {
      ActivityShell shell = getOrCreate(2);
      if (shell != null) {
         shell.init(in);
      }

   }

   public static int getServerLeft(int eid) {
      Integer in = (Integer)serverLeftMap.get(eid);
      return in == null ? -1 : in.intValue();
   }

   public static void putServerLeft(int eid, int num) {
      serverLeftMap.put(eid, num);
   }

   public static void addEelemt(ActivityElement e) {
      if (elementMap.containsKey(e.getId())) {
         logger.error("has same element id {},name is {}", e.getId(), e.getName());
      }

      elementMap.put(e.getId(), e);
   }

   public static void removeElement(int elementId) {
      elementMap.remove(elementId);
   }

   public static BaiduShell getBaiduShell() {
      ActivityShell shell = getShell(5);
      return shell == null ? null : (BaiduShell)shell;
   }

   public static ActivityElement getActivityElement(int id) {
      return (ActivityElement)elementMap.get(id);
   }

   public static void bossBeKilled(int bossId, long rid) {
      Activity activity = getActivity(2);
      if (activity != null) {
         ActivityBoss ab = (ActivityBoss)activity;
         if (ab.isOpen()) {
            ab.addKillRecord(bossId, rid);
            Player player = CenterManager.getPlayerByRoleID(rid);
            if (player != null) {
               refreshDynamicMenu(2, player);
            }
         }
      }

   }

   public static boolean showKfMenu() {
      Activity ba = getActivity(2);
      Activity la = getActivity(3);
      Activity pa = getActivity(4);
      Activity eq = getActivity(7);
      return ba != null && ba.isOpen() || la != null && la.isOpen() || pa != null && pa.isOpen() || eq != null && eq.isOpen();
   }

   public static void refreshDynamicMenu(int aid, Player player) {
      Activity ac = getActivity(aid);
      if (ac != null) {
         switch(aid) {
         case 1:
            UpdateMenu.update(player, 16);
            break;
         case 2:
         case 3:
         case 4:
         case 7:
            if (ac.isOpen()) {
               UpdateMenu.update(player, 15);
            }
            break;
         case 5:
            UpdateMenu.update(player, 17);
            break;
         case 127:
            UpdateMenu.update(player, 19);
         }

      }
   }

   public static Activity getActivity(int aid) {
      return (Activity)activityMap.get(aid);
   }

   public static ConcurrentHashMap getActivityMap() {
      return activityMap;
   }

   public static void addActivity(Activity activity) {
      activityMap.put(activity.getId(), activity);
   }

   public static void reloadActivity(int shellId) throws Exception {
      ActivityShell shell = getOrCreate(shellId);
      InputStream in = getActivityInput(shellId);
      if (shell != null && in != null) {
         shell.reload(in);
         in.close();
         in = null;
      } else {
         throw new Exception("reload activity shell " + shellId + " faild");
      }
   }

   public static void refreshWhenLevelUp(Player player) {
      refreshIcon(player, 10);
      refreshIcon(player, 8);
      refreshIcon(player, 3);
      refreshIcon(player, 5);
   }

   public static void reloadAllActivity() throws Exception {
      reloadActivity(1);
      reloadActivity(2);
      reloadActivity(127);
      reloadActivity(3);
      reloadActivity(4);
      reloadActivity(8);
   }

   private static InputStream getActivityInput(int shellId) {
      InputStream in = null;
      switch(shellId) {
      case 1:
         in = GlobalDBManager.getSystemScriptData(65);
         break;
      case 2:
         in = GlobalDBManager.getSystemScriptData(64);
         break;
      case 3:
         in = GlobalDBManager.getSystemScriptData(66);
         break;
      case 4:
         in = GlobalDBManager.getSystemScriptData(71);
         break;
      case 8:
         in = GlobalDBManager.getSystemScriptData(77);
         break;
      case 127:
         in = GlobalDBManager.getSystemScriptData(67);
      }

      return in;
   }

   public static ActivityShell getShell(int shellId) {
      return (ActivityShell)shellMap.get(shellId);
   }

   private static ActivityShell getOrCreate(int shellId) {
      ActivityShell shell = (ActivityShell)shellMap.get(shellId);
      if (shell != null) {
         return (ActivityShell)shell;
      } else {
         switch(shellId) {
         case 1:
            shell = new FirstPayShell();
            break;
         case 2:
            shell = new KfShell();
            break;
         case 4:
            shell = new TeHuiShell();
            break;
         case 5:
            shell = new BaiduShell();
            break;
         case 6:
            shell = new TxLevelUpShell();
            break;
         case 7:
            shell = new YxlbShell();
            break;
         case 8:
            shell = new BlueVipShell();
            break;
         case 10:
            shell = new BlueVipRenewShell();
            break;
         case 11:
            shell = new BigPayShell();
            break;
         case 12:
            shell = new VipGiftShell();
            break;
         case 13:
            shell = new DayPayShell();
            break;
         case 127:
            shell = new CollectionShell();
         }

         if (shell != null) {
            shellMap.put(((ActivityShell)shell).getShellId(), shell);
         }

         return (ActivityShell)shell;
      }
   }

   public static Activity removeAndDestroyActivity(int id) {
      Activity activity = (Activity)activityMap.remove(id);
      if (activity != null) {
         activity.destroy();
      }

      return activity;
   }

   public static void refreshIcon(Player player, int activityId) {
      Activity activity = (Activity)activityMap.get(activityId);
      if (activity != null && activity.isOpen()) {
         activity.refreshIcon(player);
      }

   }

   public static int getBlueVipShwoNumber(Player player) {
      Activity activity = (Activity)activityMap.get(Integer.valueOf(10));
      if (activity != null) {
         ActivityBlueVip av = (ActivityBlueVip)activity;
         return av.getShowNumber(player);
      } else {
         return 0;
      }
   }

   public static ActivityTest getActivityTest() {
      Activity activity = (Activity)activityMap.get(Integer.valueOf(5));
      return activity != null ? (ActivityTest)activity : null;
   }

   public static ActivityBlueRenew getBlueRenew() {
      Activity activity = (Activity)activityMap.get(Integer.valueOf(12));
      return activity != null ? (ActivityBlueRenew)activity : null;
   }

   public static void checkPlayerOffLine(long rid) {
      ActivityBlueRenew renew = getBlueRenew();
      if (renew != null) {
         renew.removeRole(rid);
      }

   }

   public static void checkPlayerOnLine(Player player) {
      ActivityBlueRenew renew = getBlueRenew();
      if (renew != null) {
         BlueVip bv = player.getUser().getBlueVip();
         if (bv.isNeedCheckValid()) {
            long time = Math.abs(bv.getValidTime());
            if (time > 0L && time < 259200L && renew.getElement().getReceiveStatus(player) != 2) {
               renew.addRole(player.getID());
            }
         }
      }

   }
}
