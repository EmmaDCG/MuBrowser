package com.mu.game.model.activity.imp.collection;

import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Rnd;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class Collection {
   private static int id = 12701;
   private static String name = "";
   private static String url = "";
   private static ArrayList unitList = null;
   private static ArrayList rewardList = null;
   private static Date openDate = null;
   private static Date endDate = null;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      id = Tools.getCellIntValue(sheet.getCell("A2"));
      String tmp = Tools.getCellValue(sheet.getCell("B2"));
      rewardList = Tools.parseItemList(tmp);
      unitList = Tools.parseItemDataUnitList(tmp);
      name = Tools.getCellValue(sheet.getCell("C2"));
      url = Tools.getCellValue(sheet.getCell("D2"));
      openDate = Time.getDate("1980-01-01 00:00:00");
      endDate = Time.getDate("2100-01-01 00:00:00");
   }

   public static int getId() {
      return id;
   }

   public static String getName() {
      return name;
   }

   public static String getUrl() {
      return url + "?v=" + Rnd.get(1L, 10000000000000L);
   }

   public static ArrayList getUnitList() {
      return unitList;
   }

   public static ArrayList getRewardList() {
      return rewardList;
   }

   public static boolean receive(Player player) {
      if (canReceive(player, true)) {
         OperationResult or = player.getItemManager().addItem((List)unitList);
         if (or.isOk()) {
            player.getActivityLogs().received(3, player.getUser().getServerID(), getId(), Time.getTimeStr());
            UpdateMenu.update(player, 19);
            return true;
         } else {
            SystemMessage.writeMessage(player, or.getResult());
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean canReceive(Player player, boolean notice) {
      return false;
   }
}
