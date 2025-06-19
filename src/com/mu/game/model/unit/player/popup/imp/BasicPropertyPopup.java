package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.item.Item;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class BasicPropertyPopup extends Popup {
   private HashMap statMap = null;
   private long itemID;

   public BasicPropertyPopup(int id, long itemID, HashMap statMap) {
      super(id);
      this.itemID = itemID;
      this.statMap = new HashMap();
      this.statMap.putAll(statMap);
   }

   public String getTitle() {
      return MessageText.getText(4010);
   }

   public String getContent() {
      String s = MessageText.getText(4011);
      String tmpStr = "";
      String tmpStr2 = "";

      Entry entry;
      int needValue;
      for(Iterator it = this.statMap.entrySet().iterator(); it.hasNext(); tmpStr2 = tmpStr2 + needValue + MessageText.getText(4012) + ((StatEnum)entry.getKey()).getName()) {
         entry = (Entry)it.next();
         int needAllValue = ((int[])entry.getValue())[0];
         needValue = ((int[])entry.getValue())[1];
         tmpStr = tmpStr + needAllValue + MessageText.getText(4012) + ((StatEnum)entry.getKey()).getName();
      }

      s = s.replace("%s%", tmpStr);
      s = s.replace("%ss%", tmpStr2);
      return s;
   }

   public void dealLeftClick(Player player) {
      Item item = player.getBackpack().getItemByID(this.itemID);
      if (item == null) {
         SystemMessage.writeMessage(player, 3002);
      } else {
         int str = this.getStatValue(StatEnum.STR);
         int dex = this.getStatValue(StatEnum.DEX);
         int con = this.getStatValue(StatEnum.CON);
         int intell = this.getStatValue(StatEnum.INT);
         int result = PlayerManager.allocatePotential(player, str, dex, con, intell);
         if (result == 1) {
            AfterAllocateEquipItemPopup popup = new AfterAllocateEquipItemPopup(player.createPopupID(), this.itemID, item.getName(), str, con, dex, intell);
            ShowPopup.open(player, popup);
         } else {
            SystemMessage.writeMessage(player, result);
         }

      }
   }

   private int getStatValue(StatEnum stat) {
      return !this.statMap.containsKey(stat) ? 0 : ((int[])this.statMap.get(stat))[1];
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 16;
   }

   public void destroy() {
      this.statMap.clear();
      this.statMap = null;
      super.destroy();
   }
}
