package com.mu.game.model.activity.imp.tehui;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.utils.Tools;
import java.util.Calendar;
import jxl.Sheet;

public class ActivityTeHui extends Activity {
   public ActivityTeHui() {
      super(6);
   }

   public void init(Object obj) throws Exception {
      Sheet sheet = (Sheet)obj;
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int eid = Tools.getCellIntValue(sheet.getCell("A" + i));
         String itemStr = Tools.getCellValue(sheet.getCell("B" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("C" + i));
         String des = Tools.getCellValue(sheet.getCell("D" + i));
         ActivityTeHuiElement ae = new ActivityTeHuiElement(eid, this);
         String[] pStr = itemStr.split("#");

         for(int j = 0; j < pStr.length; ++j) {
            String[] tmp = pStr[j].split(":");
            int profession = Integer.parseInt(tmp[0]);
            ae.addItemList(profession, Tools.parseItemList(tmp[1]));
            ae.addItemUnitList(profession, Tools.parseItemDataUnitList(tmp[1]));
         }

         ae.setDes(des);
         ae.setIngot(ingot);
         ae.setName(Tools.getCellValue(sheet.getCell("E" + i)));
         this.addElement(ae, true);
      }

      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, this.getDuration());
      this.setCloseDate(cc.getTime());
   }

   public int getShellId() {
      return 4;
   }

   public int getActivityType() {
      return 4;
   }

   public void writeDetail(Player player) {
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.getId());

         for(int i = 0; i < this.getElementList().size(); ++i) {
            ((ActivityElement)this.getElementList().get(i)).writeDetail(player, ai);
         }

         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
