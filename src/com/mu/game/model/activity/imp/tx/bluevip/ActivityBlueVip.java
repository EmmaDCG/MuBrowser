package com.mu.game.model.activity.imp.tx.bluevip;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.io.game.packet.imp.activity.ActivityReceive;
import com.mu.io.game.packet.imp.activity.BlueOneKeyReceive;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class ActivityBlueVip extends Activity {
   private String vipDes;
   private String vipUrl;
   private String funtionName;
   private String callBackName;
   private ArrayList normalList = new ArrayList();
   private ArrayList levellList = new ArrayList();
   private ActivitySuperBlueElement superElement = null;
   private ActivityNewBeeBlueVipElement newBeeElement = null;
   private ActivityYearBlueVipElement yearElement = null;

   public ActivityBlueVip() {
      super(10);
   }

   private void initNormal(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int eid = Tools.getCellIntValue(sheet.getCell("A" + i));
         ActivityNormalBlueElement ae = new ActivityNormalBlueElement(eid, this);
         ae.init(sheet, i);
         this.addElement(ae, true);
         this.normalList.add(ae);
      }

   }

   private void initLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int eid = Tools.getCellIntValue(sheet.getCell("A" + i));
         ActivityLevelBlueElement ae = new ActivityLevelBlueElement(eid, this);
         ae.init(sheet, i);
         this.addElement(ae, true);
         this.levellList.add(ae);
      }

   }

   private void initSuper(Sheet sheet) throws Exception {
      int eid = Tools.getCellIntValue(sheet.getCell("A2"));
      ActivitySuperBlueElement ae = new ActivitySuperBlueElement(eid, this);
      ae.init(sheet, 2);
      this.addElement(ae, true);
      this.superElement = ae;
   }

   private void initYear(Sheet sheet) throws Exception {
      int eid = Tools.getCellIntValue(sheet.getCell("A2"));
      ActivityYearBlueVipElement ae = new ActivityYearBlueVipElement(eid, this);
      ae.init(sheet, 2);
      this.addElement(ae, true);
      this.yearElement = ae;
   }

   private void initNewBee(Sheet sheet) throws Exception {
      int eid = Tools.getCellIntValue(sheet.getCell("A2"));
      ActivityNewBeeBlueVipElement ae = new ActivityNewBeeBlueVipElement(eid, this);
      ae.init(sheet, 2);
      this.addElement(ae, true);
      this.newBeeElement = ae;
   }

   public void init(Object obj) throws Exception {
      Workbook wb = Workbook.getWorkbook((InputStream)obj);
      Sheet sheet = wb.getSheet(1);
      boolean systemClose = Tools.getCellIntValue(sheet.getCell("D2")) != 1;
      this.vipDes = Tools.getCellValue(sheet.getCell("E2"));
      this.vipUrl = Tools.getCellValue(sheet.getCell("F2"));
      this.funtionName = Tools.getCellValue(sheet.getCell("G2"));
      this.callBackName = Tools.getCellValue(sheet.getCell("H2"));
      this.setSystemClose(systemClose);
      this.initNormal(wb.getSheet(2));
      this.initSuper(wb.getSheet(3));
      this.initYear(wb.getSheet(4));
      this.initNewBee(wb.getSheet(5));
      this.initLevel(wb.getSheet(6));
      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, 200000);
      this.setCloseDate(cc.getTime());
   }

   public int getShellId() {
      return 8;
   }

   public int getActivityType() {
      return 8;
   }

   public String getFuntionName() {
      return this.funtionName;
   }

   public String getCallBackName() {
      return this.callBackName;
   }

   private int getOnkeyStatus(Player player) {
      Iterator var3 = this.normalList.iterator();

      while(var3.hasNext()) {
         ActivityNormalBlueElement ae = (ActivityNormalBlueElement)var3.next();
         if (ae.getReceiveStatus(player) == 1) {
            return 1;
         }
      }

      if (this.superElement.getReceiveStatus(player) == 1) {
         return 1;
      } else {
         return this.yearElement.getReceiveStatus(player) == 1 ? 1 : 0;
      }
   }

   public void writeDetail(Player player) {
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.id);
         ai.writeBoolean(player.getUser().getBlueVip().isVip());
         ai.writeByte(this.normalList.size());
         Iterator var4 = this.normalList.iterator();

         while(var4.hasNext()) {
            ActivityNormalBlueElement be = (ActivityNormalBlueElement)var4.next();
            be.writeDetail(player, ai);
         }

         this.superElement.writeDetail(player, ai);
         this.yearElement.writeDetail(player, ai);
         this.newBeeElement.writeDetail(player, ai);
         int index = -1;

         ActivityLevelBlueElement le;
         int i;
         for(i = 0; i < this.levellList.size(); ++i) {
            le = (ActivityLevelBlueElement)this.levellList.get(i);
            if (le.getReceiveStatus(player) != 2) {
               index = i;
               break;
            }
         }

         if (index == -1 || index + 2 >= this.levellList.size()) {
            index = this.levellList.size() - 2;
         }

         ai.writeByte(2);

         for(i = index; i < index + 2; ++i) {
            le = (ActivityLevelBlueElement)this.levellList.get(i);
            le.writeDetail(player, ai);
         }

         ai.writeUTF(this.vipDes);
         ai.writeUTF(this.vipUrl);
         ai.writeByte(this.getOnkeyStatus(player));
         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   private void writeReceive(Player player, boolean b, int status, int eid) {
      ActivityReceive ar = new ActivityReceive();

      try {
         ar.writeShort(eid);
         ar.writeBoolean(b);
         ar.writeByte(status);
         ar.writeInt(-1);
         player.writePacket(ar);
         ar.destroy();
         ar = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public ArrayList getNormalList() {
      return this.normalList;
   }

   public ArrayList getLevellList() {
      return this.levellList;
   }

   public ActivitySuperBlueElement getSuperElement() {
      return this.superElement;
   }

   public ActivityNewBeeBlueVipElement getNewBeeElement() {
      return this.newBeeElement;
   }

   public ActivityYearBlueVipElement getYearElement() {
      return this.yearElement;
   }

   public String getVipDes() {
      return this.vipDes;
   }

   public String getVipUrl() {
      return this.vipUrl;
   }

   public void refreshIcon(Player player) {
      UpdateMenu.update(player, 26);
   }

   public void oneKeyReceive(Player player) {
      ActivityNormalBlueElement ne = null;
      boolean normalReceive = false;
      Iterator var5 = this.normalList.iterator();

      ActivityNormalBlueElement list;
      while(var5.hasNext()) {
         list = (ActivityNormalBlueElement)var5.next();
         if (list.getReceiveStatus(player) == 1) {
            ne = list;
            break;
         }
      }

      ArrayList list2 = new ArrayList();
      if (ne != null) {
         normalReceive = true;
         ArrayList listNormal = ne.getItemUnitList(player);
         Iterator var7 = listNormal.iterator();

         while(var7.hasNext()) {
            ItemDataUnit unit = (ItemDataUnit)var7.next();
            list2.add(unit.cloneUnit());
         }
      }

      boolean superReceive = false;
      Iterator var8;
      if (this.superElement.getReceiveStatus(player) == 1) {
         superReceive = true;
         ArrayList listNormal = this.superElement.getItemUnitList(player);
         var8 = listNormal.iterator();

         while(var8.hasNext()) {
            ItemDataUnit unit = (ItemDataUnit)var8.next();
            list2.add(unit.cloneUnit());
         }
      }

      boolean yearReceive = false;
      if (this.yearElement.getReceiveStatus(player) == 1) {
         yearReceive = true;
         ArrayList listNormal = this.yearElement.getItemUnitList(player);
         Iterator var9 = listNormal.iterator();

         while(var9.hasNext()) {
            ItemDataUnit unit = (ItemDataUnit)var9.next();
            list2.add(unit.cloneUnit());
         }
      }

      OperationResult or = player.getItemManager().addItem((List)list2);
      if (or.isOk()) {
         if (normalReceive) {
            player.getActivityLogs().received(ne.getReceiveType(), player.getUser().getServerID(), ne.getId(), Time.getTimeStr());
            this.writeReceive(player, true, ne.getReceiveStatus(player), ne.getId());
         }

         if (superReceive) {
            player.getActivityLogs().received(this.superElement.getReceiveType(), player.getUser().getServerID(), this.superElement.getId(), Time.getTimeStr());
            this.writeReceive(player, true, this.superElement.getReceiveStatus(player), this.superElement.getId());
         }

         if (yearReceive) {
            player.getActivityLogs().received(this.yearElement.getReceiveType(), player.getUser().getServerID(), this.yearElement.getId(), Time.getTimeStr());
            this.writeReceive(player, true, this.yearElement.getReceiveStatus(player), this.yearElement.getId());
         }

         this.refreshIcon(player);
         BlueOneKeyReceive br = new BlueOneKeyReceive();

         try {
            br.writeByte(0);
            player.writePacket(br);
            br.destroy();
            var8 = null;
         } catch (Exception var10) {
            var10.printStackTrace();
         }
      } else {
         SystemMessage.writeMessage(player, or.getResult());
      }

      list2.clear();
      list = null;
   }

   public int getShowNumber(Player player) {
      int num = 0;
      Iterator var4 = this.normalList.iterator();

      while(var4.hasNext()) {
         ActivityNormalBlueElement ae = (ActivityNormalBlueElement)var4.next();
         if (ae.getReceiveStatus(player) == 1) {
            ++num;
            break;
         }
      }

      if (this.superElement.getReceiveStatus(player) == 1) {
         ++num;
      }

      if (this.yearElement.getReceiveStatus(player) == 1) {
         ++num;
      }

      if (this.newBeeElement.getReceiveStatus(player) == 1) {
         ++num;
      }

      var4 = this.levellList.iterator();

      while(var4.hasNext()) {
         ActivityLevelBlueElement ae = (ActivityLevelBlueElement)var4.next();
         if (ae.canReceive(player, false) && ae.getReceiveStatus(player) == 1) {
            ++num;
         }
      }

      return num;
   }
}
