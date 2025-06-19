package com.mu.game.model.activity.imp.tx.bluerenew;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.bluevip.BlueVip;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.Calendar;
import jxl.Sheet;
import jxl.Workbook;
import org.jboss.netty.util.internal.ConcurrentHashMap;

public class ActivityBlueRenew extends Activity {
   private ConcurrentHashMap roleMap = new ConcurrentHashMap();
   private String yearValid = null;
   private String superValid = null;
   private String normalValid = null;
   private String yearSoonValid = null;
   private String superSoonValid = null;
   private String normalSoonValid = null;
   private String roleTitle = null;

   public ActivityBlueRenew() {
      super(12);
   }

   public void init(Object obj) throws Exception {
      Workbook wb = Workbook.getWorkbook((InputStream)obj);
      Sheet sheet = wb.getSheet(1);
      boolean systemClose = Tools.getCellIntValue(sheet.getCell("D2")) != 1;
      this.setSystemClose(systemClose);
      this.initElement(wb.getSheet(2));
      this.initDes(wb.getSheet(3));
      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, 200000);
      this.setCloseDate(cc.getTime());
   }

   private void initDes(Sheet sheet) throws Exception {
      this.yearValid = Tools.getCellValue(sheet.getCell("B2"));
      this.superValid = Tools.getCellValue(sheet.getCell("B3"));
      this.normalValid = Tools.getCellValue(sheet.getCell("B4"));
      this.yearSoonValid = Tools.getCellValue(sheet.getCell("B5"));
      this.superSoonValid = Tools.getCellValue(sheet.getCell("B6"));
      this.normalSoonValid = Tools.getCellValue(sheet.getCell("B7"));
      this.roleTitle = Tools.getCellValue(sheet.getCell("B8"));
   }

   public ActivityBlueRenewElement getElement() {
      return this.elementList != null && this.elementList.size() != 0 ? (ActivityBlueRenewElement)this.elementList.get(0) : null;
   }

   public ConcurrentHashMap getRoleMap() {
      return this.roleMap;
   }

   public String getYearValid() {
      return this.yearValid;
   }

   public String getSuperValid() {
      return this.superValid;
   }

   public String getNormalValid() {
      return this.normalValid;
   }

   public String getYearSoonValid() {
      return this.yearSoonValid;
   }

   public String getSuperSoonValid() {
      return this.superSoonValid;
   }

   public String getNormalSoonValid() {
      return this.normalSoonValid;
   }

   public String getRoleTitle() {
      return this.roleTitle;
   }

   private void initElement(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int eid = Tools.getCellIntValue(sheet.getCell("A" + i));
         ActivityBlueRenewElement ae = new ActivityBlueRenewElement(eid, this);
         ae.init(sheet, i);
         this.addElement(ae, true);
      }

   }

   public void addRole(long rid) {
      this.roleMap.put(rid, true);
   }

   public void removeRole(long rid) {
      this.roleMap.remove(rid);
   }

   public boolean hasPlayer(long rid) {
      return this.roleMap.containsKey(rid);
   }

   public int getShellId() {
      return 10;
   }

   public int getActivityType() {
      return 10;
   }

   private String getValidStr(Player player) {
      BlueVip bv = player.getUser().getBlueVip();
      if (!bv.is_blue_vip()) {
         if (bv.getYear_vip_valid_time() > 0L) {
            return this.yearValid;
         }

         if (bv.getExpand_vip_valid_time() > 0L) {
            return this.superValid;
         }

         if (bv.getVip_valid_time() > 0L) {
            return this.normalValid;
         }
      } else {
         if (bv.getYear_vip_valid_time() > 0L) {
            return this.yearSoonValid;
         }

         if (bv.getExpand_vip_valid_time() > 0L) {
            return this.superSoonValid;
         }

         if (bv.getVip_valid_time() > 0L) {
            return this.normalSoonValid;
         }
      }

      return this.normalSoonValid;
   }

   public void writeDetail(Player player) {
      try {
         ActivityInfo ai = new ActivityInfo();
         ai.writeByte(this.id);
         ai.writeUTF(this.roleTitle.replace("%s%", player.getName()));
         ai.writeUTF(this.getValidStr(player));
         this.getElement().writeDetail(player, ai);
         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
