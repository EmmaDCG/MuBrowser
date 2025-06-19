package com.mu.game.model.activity.imp.baidu;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;

public class ActivityBaiduClub extends Activity {
   public ActivityBaiduClub() {
      super(0);
   }

   public void init(Object obj) throws Exception {
      Element element = (Element)obj;
      this.setName(element.getAttributeValue("name"));
      this.setSystemClose(!element.getAttribute("isOpen").getBooleanValue());
      List list = element.getChildren("element");
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         Element child = (Element)var5.next();
         int eid = child.getAttribute("id").getIntValue();
         int receiveType = child.getAttribute("receiveType").getIntValue();
         String itemStr = child.getAttributeValue("item");
         int cl = child.getAttribute("level").getIntValue();
         ActivityBaiduClubElement ac = new ActivityBaiduClubElement(eid, this, cl);
         ac.setReceiveType(receiveType);
         ac.setRewardList(Tools.parseItemList(itemStr));
         ac.setUnitList(Tools.parseItemDataUnitList(itemStr));
         this.addElement(ac, true);
      }

      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, 1000000);
      this.setCloseDate(cc.getTime());
   }

   public boolean isOpen() {
      return Global.getPlatID() != 1 ? false : super.isOpen();
   }

   public int getShellId() {
      return 5;
   }

   public int getActivityType() {
      return 4;
   }

   public void writeDetail(Player player) {
   }
}
