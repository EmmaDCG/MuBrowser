package com.mu.game.model.activity.imp.tx.levelup;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.Calendar;
import jxl.Sheet;

public class TxLevelUpActivity extends Activity {
   public TxLevelUpActivity() {
      super(8);
   }

   public void init(Object obj) throws Exception {
      Sheet sheet = (Sheet)obj;
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int eid = Tools.getCellIntValue(sheet.getCell("A" + i));
         String itemStr = Tools.getCellValue(sheet.getCell("C" + i));
         TxLevelUpElement ae = new TxLevelUpElement(eid, this);
         String[] pStr = itemStr.split("#");

         for(int j = 0; j < pStr.length; ++j) {
            String[] tmp = pStr[j].split(":");
            int profession = Integer.parseInt(tmp[0]);
            ae.addItemList(profession, Tools.parseItemList(tmp[1]));
            ae.addItemUnitList(profession, Tools.parseItemDataUnitList(tmp[1]));
         }

         ae.setNumerical(Tools.getCellIntValue(sheet.getCell("B" + i)));
         this.addElement(ae, true);
      }

      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, 200000);
      this.setCloseDate(cc.getTime());
   }

   public int getShellId() {
      return 6;
   }

   public int getActivityType() {
      return 6;
   }

   public void writeDetail(Player player) {
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.getId());
         ai.writeUTF(this.getName());
         ArrayList al = this.getElementList();
         ai.writeByte(al.size());

         for(int i = 0; i < al.size(); ++i) {
            ((ActivityElement)al.get(i)).writeDetail(player, ai);
         }

         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
