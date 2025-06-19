package com.mu.game.model.activity.imp.tx.bluevip;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.HashMap;
import jxl.Sheet;

public class ActivityBlueVipElement extends ActivityElement {
   private int icon = 0;
   private String title = "";
   private String des = "";
   private HashMap itemMap = new HashMap();
   private HashMap unitMap = new HashMap();

   public ActivityBlueVipElement(int id, ActivityBlueVip father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
   }

   public void init(Sheet sheet, int index) throws Exception {
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public ArrayList getItemList(int pro) {
      return (ArrayList)this.itemMap.get(pro);
   }

   public ArrayList getItemDataList(int pro) {
      return (ArrayList)this.unitMap.get(pro);
   }

   public void addItemList(int pro, ArrayList list) {
      this.itemMap.put(pro, list);
   }

   public void addItemUnitList(int pro, ArrayList list) {
      this.unitMap.put(pro, list);
   }

   public ArrayList getItemUnitList(Player player) {
      int pro = player.getProType();
      return this.getItemDataList(pro);
   }

   public int getReceiveType() {
      return 4;
   }

   public int getReceiveStatus(Player player) {
      return 0;
   }
}
