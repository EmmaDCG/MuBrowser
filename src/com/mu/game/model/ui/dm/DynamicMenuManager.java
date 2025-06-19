package com.mu.game.model.ui.dm;

import com.mu.game.model.ui.dm.imp.dungeon.BigDevilMenu;
import com.mu.game.model.ui.dm.imp.dungeon.BloodCastleMenu;
import com.mu.game.model.ui.dm.imp.dungeon.DevilSqureMenu;
import com.mu.game.model.ui.dm.imp.dungeon.RedFortMenu;
import com.mu.game.model.ui.dm.imp.dungeon.TempleMenu;
import com.mu.game.model.ui.dm.imp.dungeon.TrialMenu;
import com.mu.game.model.ui.dm.imp.other.BigPayMenu;
import com.mu.game.model.ui.dm.imp.other.BlueRenewMenu;
import com.mu.game.model.ui.dm.imp.other.BlueVipMenu;
import com.mu.game.model.ui.dm.imp.other.BossMenu;
import com.mu.game.model.ui.dm.imp.other.CollectionMenu;
import com.mu.game.model.ui.dm.imp.other.DayPayMenu;
import com.mu.game.model.ui.dm.imp.other.ExTargetMenu;
import com.mu.game.model.ui.dm.imp.other.FinancingMenu;
import com.mu.game.model.ui.dm.imp.other.FirstPayMenu;
import com.mu.game.model.ui.dm.imp.other.KfhdMenu;
import com.mu.game.model.ui.dm.imp.other.LuckyTurntableMenu;
import com.mu.game.model.ui.dm.imp.other.LuolanMenu;
import com.mu.game.model.ui.dm.imp.other.OfflineMenu;
import com.mu.game.model.ui.dm.imp.other.RewardHallMenu;
import com.mu.game.model.ui.dm.imp.other.SevenDayMenu;
import com.mu.game.model.ui.dm.imp.other.TanxianMenu;
import com.mu.game.model.ui.dm.imp.other.TeHuiMenu;
import com.mu.game.model.ui.dm.imp.other.TxLevelUpMenu;
import com.mu.game.model.ui.dm.imp.other.VipGiftMenu;
import com.mu.game.model.ui.dm.imp.other.YxlbMenu;
import com.mu.game.model.ui.dm.imp.transferjob.TrnasferJobMenu;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class DynamicMenuManager {
   public static final int Type_Button = 1;
   public static final int Type_Effect = 2;
   public static final int Menu_BloodCastle = 1;
   public static final int Menu_DevilSqure = 2;
   public static final int Menu_TransferJob1 = 3;
   public static final int Menu_TransferJob2 = 4;
   public static final int Menu_TransferJob3 = 5;
   public static final int Menu_TransferJob4 = 6;
   public static final int Menu_Trial = 7;
   public static final int Menu_Temple = 8;
   public static final int Menu_Boss = 9;
   public static final int Menu_qiandao = 11;
   public static final int Menu_RedFort = 12;
   public static final int Menu_BigDevil = 13;
   public static final int Menu_Tzlc = 14;
   public static final int Menu_Kfhd = 15;
   public static final int Menu_FirstPay = 16;
   public static final int Menu_CloseTest = 17;
   public static final int Menu_Collection = 19;
   public static final int Menu_Baidu = 20;
   public static final int Menu_TeHui = 21;
   public static final int Menu_Luolan = 22;
   public static final int Menu_TxLu = 23;
   public static final int Menu_ExTarget = 24;
   public static final int Menu_Yxlb = 25;
   public static final int Menu_BlueVip = 26;
   public static final int Menu_BlueVipRenew = 28;
   public static final int Menu_BigPay = 29;
   public static final int Menu_Offline = 30;
   public static final int Menu_SevenDay = 31;
   public static final int Menu_VipGift = 32;
   public static final int Menu_TanXian = 33;
   public static final int Menu_DayPay = 34;
   public static final int Menu_LuckyTurntable = 35;
   private static HashMap menuMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      initMenu(sheet);
   }

   private static void initMenu(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         DynamicMenu menu = createMenu(sheet, i);
         if (menu != null) {
            menuMap.put(menu.getId(), menu);
         }
      }

   }

   public static DynamicMenu getMenu(int id) {
      return (DynamicMenu)menuMap.get(id);
   }

   public static HashMap getMenuMap() {
      return menuMap;
   }

   public static void refreshAllMenuWhenChangeDay(Player player) {
      try {
         UpdateMenu.update(player, 13);
         UpdateMenu.update(player, 1);
         UpdateMenu.update(player, 9);
         UpdateMenu.update(player, 17);
         UpdateMenu.update(player, 2);
         UpdateMenu.update(player, 15);
         UpdateMenu.update(player, 11);
         UpdateMenu.update(player, 8);
         UpdateMenu.update(player, 7);
         UpdateMenu.update(player, 14);
         UpdateMenu.update(player, 26);
         UpdateMenu.update(player, 31);
         UpdateMenu.update(player, 34);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static DynamicMenu createMenu(Sheet sheet, int index) throws Exception {
      int id = Tools.getCellIntValue(sheet.getCell("A" + index));
      String name = Tools.getCellValue(sheet.getCell("B" + index));
      int buType = Tools.getCellIntValue(sheet.getCell("C" + index));
      int icon = Tools.getCellIntValue(sheet.getCell("D" + index));
      int row = Tools.getCellIntValue(sheet.getCell("E" + index));
      int position = Tools.getCellIntValue(sheet.getCell("F" + index));
      String operaionStr = Tools.getCellValue(sheet.getCell("I" + index));
      int funtionOpenId = Tools.getCellIntValue(sheet.getCell("O" + index));
      String tip = Tools.getCellValue(sheet.getCell("P" + index));
      int hg = Tools.getCellIntValue(sheet.getCell("Q" + index));
      int sg = Tools.getCellIntValue(sheet.getCell("R" + index));
      int showLevel = Tools.getCellIntValue(sheet.getCell("S" + index));
      String[] tmp = operaionStr.split(",");
      int[] operations = new int[tmp.length];

      for(int i = 0; i < operations.length; ++i) {
         operations[i] = Integer.parseInt(tmp[i]);
      }

      DynamicMenu menu = null;
      switch(id) {
      case 1:
         menu = new BloodCastleMenu();
         break;
      case 2:
         menu = new DevilSqureMenu();
         break;
      case 3:
      case 4:
      case 5:
      case 6:
         menu = new TrnasferJobMenu(id, operations[0]);
         break;
      case 7:
         menu = new TrialMenu();
         break;
      case 8:
         menu = new TempleMenu();
         break;
      case 9:
         menu = new BossMenu();
      case 10:
      case 17:
      case 18:
      case 20:
      case 27:
      default:
         break;
      case 11:
         menu = new RewardHallMenu();
         break;
      case 12:
         menu = new RedFortMenu();
         break;
      case 13:
         menu = new BigDevilMenu();
         break;
      case 14:
         menu = new FinancingMenu();
         break;
      case 15:
         menu = new KfhdMenu();
         break;
      case 16:
         menu = new FirstPayMenu();
         break;
      case 19:
         menu = new CollectionMenu();
         break;
      case 21:
         menu = new TeHuiMenu();
         break;
      case 22:
         menu = new LuolanMenu();
         break;
      case 23:
         menu = new TxLevelUpMenu();
         break;
      case 24:
         menu = new ExTargetMenu();
         break;
      case 25:
         menu = new YxlbMenu();
         break;
      case 26:
         menu = new BlueVipMenu();
         break;
      case 28:
         menu = new BlueRenewMenu();
         break;
      case 29:
         menu = new BigPayMenu();
         break;
      case 30:
         menu = new OfflineMenu();
         break;
      case 31:
         menu = new SevenDayMenu();
         break;
      case 32:
         menu = new VipGiftMenu();
         break;
      case 33:
         menu = new TanxianMenu();
         break;
      case 34:
         menu = new DayPayMenu();
         break;
      case 35:
         menu = new LuckyTurntableMenu();
      }

      if (menu != null) {
         ((DynamicMenu)menu).setIcons(icon);
         ((DynamicMenu)menu).setPosition(position);
         ((DynamicMenu)menu).setPanelId(operations);
         ((DynamicMenu)menu).setName(name);
         ((DynamicMenu)menu).setOpenFunctionId(funtionOpenId);
         ((DynamicMenu)menu).setRow(row);
         ((DynamicMenu)menu).setTips(tip == null ? "" : tip);
         ((DynamicMenu)menu).setHg(hg);
         ((DynamicMenu)menu).setSg(sg);
         ((DynamicMenu)menu).setShowLevel(showLevel);
      }

      return (DynamicMenu)menu;
   }

   public static void checkWhenLevelUp(Player player, int oldLevel, int newLevel) {
      Iterator it = menuMap.values().iterator();

      while(it.hasNext()) {
         DynamicMenu menu = (DynamicMenu)it.next();
         if (oldLevel < menu.getShowLevel() && newLevel >= menu.getShowLevel()) {
            UpdateMenu.update(player, menu.getId());
         }
      }

   }

   public static void operationTransferJob(Player player, int n) {
      DynamicMenu menu = null;
      switch(n) {
      case 1:
         menu = getMenu(3);
         break;
      case 2:
         menu = getMenu(4);
         break;
      case 3:
         menu = getMenu(5);
         break;
      case 4:
         menu = getMenu(6);
      }

      if (menu != null) {
         UpdateMenu.update(player, menu.getId());
      }

   }
}
