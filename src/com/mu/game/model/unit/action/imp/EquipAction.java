package com.mu.game.model.unit.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import java.util.HashMap;

public class EquipAction extends XmlAction {
   private HashMap equipMap = new HashMap();

   public EquipAction(int level) {
      super(level);
   }

   public void doAction(Player player) {
      Integer in = (Integer)this.equipMap.get(player.getProType());
      if (in != null) {
         Item item = player.getBackpack().getFirstItemByModelID(in.intValue());
         if (item != null) {
            player.getItemManager().useItem(item, 1, true);
         }
      }

   }

   public void initAction(String value) {
      String[] pro = value.split(";");

      for(int i = 0; i < pro.length; ++i) {
         String[] equips = pro[i].split(",");
         this.equipMap.put(Integer.parseInt(equips[0]), Integer.parseInt(equips[1]));
      }

   }

   public void destroy() {
      if (this.equipMap != null) {
         this.equipMap.clear();
         this.equipMap = null;
      }

   }
}
