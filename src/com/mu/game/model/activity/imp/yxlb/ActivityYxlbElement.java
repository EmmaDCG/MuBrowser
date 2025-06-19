package com.mu.game.model.activity.imp.yxlb;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.HashMap;

public class ActivityYxlbElement extends ActivityElement {
   private HashMap itemMap = new HashMap();
   private HashMap unitMap = new HashMap();

   public ActivityYxlbElement(int id, ActivityYxlb father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
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

   public int getReceiveType() {
      return 3;
   }

   public ArrayList getItemUnitList(Player player) {
      int pro = player.getProType();
      return this.getItemDataList(pro);
   }

   public int getReceiveStatus(Player player) {
      return this.receiveOverload(player) ? 2 : 1;
   }
}
