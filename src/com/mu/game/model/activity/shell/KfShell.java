package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.kfhd.boss.ActivityBoss;
import com.mu.game.model.activity.imp.kfhd.euip.ActivityEquip;
import com.mu.game.model.activity.imp.kfhd.level.ActivityLevel;
import com.mu.game.model.activity.imp.kfhd.pet.ActivityPet;
import com.mu.game.model.activity.imp.kfhd.qh.QiangHuaActivity;
import com.mu.game.model.activity.imp.kfhd.zy.ZhuoYueActivity;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import jxl.Sheet;
import jxl.Workbook;

public class KfShell extends ActivityShell {
   public int getMenuId() {
      return 15;
   }

   public int getShellId() {
      return 2;
   }

   public ArrayList getDigitalList(Player player) {
      ArrayList list = new ArrayList();
      Activity ba = ActivityManager.getActivity(2);
      Activity la = ActivityManager.getActivity(3);
      Activity pa = ActivityManager.getActivity(4);
      Activity eq = ActivityManager.getActivity(7);
      Activity zq = ActivityManager.getActivity(14);
      Activity qq = ActivityManager.getActivity(15);
      int baNumber = ba.getCanReceiveNumber(player);
      if (baNumber > 0) {
         list.add(new int[]{ba.getDigitalRelationId(), baNumber});
      }

      int laNumber = la.getCanReceiveNumber(player);
      if (laNumber > 0) {
         list.add(new int[]{la.getDigitalRelationId(), laNumber});
      }

      int paNumber = pa.getCanReceiveNumber(player);
      if (paNumber > 0) {
         list.add(new int[]{pa.getDigitalRelationId(), paNumber});
      }

      int eaNumber = eq.getCanReceiveNumber(player);
      if (eaNumber > 0) {
         list.add(new int[]{eq.getDigitalRelationId(), eaNumber});
      }

      int zqNumber = zq.getCanReceiveNumber(player);
      if (zqNumber > 0) {
         list.add(new int[]{zq.getDigitalRelationId(), zqNumber});
      }

      int qqNumber = qq.getCanReceiveNumber(player);
      if (qqNumber > 0) {
         list.add(new int[]{qq.getDigitalRelationId(), qqNumber});
      }

      return list;
   }

   public void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();
      Date curDate = Calendar.getInstance().getTime();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int openType = Tools.getCellIntValue(sheet.getCell("C" + i));
         String openTime = Tools.getCellValue(sheet.getCell("D" + i));
         int dur = Tools.getCellIntValue(sheet.getCell("E" + i));
         int sort = Tools.getCellIntValue(sheet.getCell("F" + i));
         int page = Tools.getCellIntValue(sheet.getCell("G" + i));
         boolean isSystemClose = Tools.getCellIntValue(sheet.getCell("H" + i)) != 1;
         Sheet detailPage = wb.getSheet(page);
         Activity activity = null;
         switch(id) {
         case 2:
            activity = new ActivityBoss(id);
            break;
         case 3:
            activity = new ActivityLevel(id);
            break;
         case 4:
            activity = new ActivityPet(id);
         case 5:
         case 6:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         default:
            break;
         case 7:
            activity = new ActivityEquip(id);
            break;
         case 14:
            activity = new ZhuoYueActivity(id);
            break;
         case 15:
            activity = new QiangHuaActivity(id);
         }

         if (activity != null) {
            ((Activity)activity).setName(name);
            ((Activity)activity).setOpenTime(openTime);
            ((Activity)activity).setOpenType(openType);
            ((Activity)activity).setDuration(dur);
            ((Activity)activity).setSort(sort);
            ((Activity)activity).setSystemClose(isSystemClose);
            ((Activity)activity).init(detailPage);
            ActivityManager.addActivity((Activity)activity);
            if (((Activity)activity).getOpenDate() != null && ((Activity)activity).getOpenDate().after(curDate)) {
               SpecifiedTimeManager.schedule(((Activity)activity).getOpenTask(), ((Activity)activity).getOpenDate());
            }

            if (((Activity)activity).getCloseDate() != null && ((Activity)activity).getCloseDate().after(curDate)) {
               SpecifiedTimeManager.schedule(((Activity)activity).getCloseTask(), ((Activity)activity).getCloseDate());
            }
         }
      }

   }
}
