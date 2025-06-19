package com.mu.game.model.activity.imp.firstpay;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;

public class FirstPay extends Activity {
   private HashMap modelMap = new HashMap();
   private int payIngot = 100;
   private int totalReward = 888;

   public FirstPay() {
      super(1);
   }

   public void addModle(int pro, int[] model) {
      this.modelMap.put(pro, model);
   }

   public HashMap getModelMap() {
      return this.modelMap;
   }

   public int getTotalReward() {
      return this.totalReward;
   }

   public void setTotalReward(int totalReward) {
      this.totalReward = totalReward;
   }

   public void init(Object obj) throws Exception {
      Sheet sheet = (Sheet)obj;
      int eid = Tools.getCellIntValue(sheet.getCell("B2"));
      this.payIngot = Tools.getCellIntValue(sheet.getCell("C2"));
      String itemStr = Tools.getCellValue(sheet.getCell("D2"));
      FirstPayElement ae = new FirstPayElement(eid, this);
      String[] pStr = itemStr.split("#");

      for(int i = 0; i < pStr.length; ++i) {
         String[] tmp = pStr[i].split(":");
         int profession = Integer.parseInt(tmp[0]);
         ae.addItemList(profession, Tools.parseItemList(tmp[1]));
         ae.addItemUnitList(profession, Tools.parseItemDataUnitList(tmp[1]));
      }

      this.addElement(ae, true);
      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, 200000);
      this.setCloseDate(cc.getTime());
   }

   public int getPayIngot() {
      return this.payIngot;
   }

   public int getActivityType() {
      return 1;
   }

   public void writeDetail(Player player) {
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.getId());
         int pro = player.getProType();
         int[] model = (int[])this.modelMap.get(pro);
         ai.writeShort(model[0]);
         ai.writeByte(model[1]);
         ai.writeInt(this.getTotalReward());
         FirstPayElement element = (FirstPayElement)this.getElementList().get(0);
         ArrayList list = element.getItemList(pro);
         ai.writeByte(list.size());
         Iterator var8 = list.iterator();

         while(var8.hasNext()) {
            Item item = (Item)var8.next();
            GetItemStats.writeItem(item, ai);
         }

         ai.writeShort(element.getId());
         ai.writeByte(element.getReceiveStatus(player));
         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public int getShellId() {
      return 1;
   }
}
