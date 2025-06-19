package com.mu.game.model.activity.imp.kfhd.pet;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import jxl.Sheet;

public class ActivityPet extends Activity {
   public ActivityPet(int id) {
      super(id);
      this.digitalRelationId = 6;
   }

   public void init(Object obj) throws Exception {
      Sheet sheet = (Sheet)obj;
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int eid = Tools.getCellIntValue(sheet.getCell("B" + i));
         boolean isServerLimit = Tools.getCellIntValue(sheet.getCell("C" + i)) == 1;
         int serverLimit = Tools.getCellIntValue(sheet.getCell("D" + i));
         int level = Tools.getCellIntValue(sheet.getCell("E" + i));
         String itemStr = Tools.getCellValue(sheet.getCell("F" + i));
         ActivityPetElement ae = new ActivityPetElement(eid, this);
         ae.setNumerical(level);
         ae.setHasServerLimit(isServerLimit);
         ae.setServerLimit(serverLimit);
         ae.setRewardList(Tools.parseItemList(itemStr));
         ae.setUnitList(Tools.parseItemDataUnitList(itemStr));
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

   public int getActivityType() {
      return 2;
   }

   public void writeDetail(Player player) {
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.getId());
         ai.writeUTF(this.getCloseTimeStr());
         ArrayList al = this.getElementList();
         ai.writeByte(al.size());
         Iterator var5 = al.iterator();

         while(var5.hasNext()) {
            ActivityElement element = (ActivityElement)var5.next();
            ActivityPetElement ae = (ActivityPetElement)element;
            ae.writeDetail(player, ai);
         }

         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public boolean isOpen() {
      return Global.isInterServiceServer() ? false : super.isOpen();
   }

   public int getShellId() {
      return 2;
   }
}
