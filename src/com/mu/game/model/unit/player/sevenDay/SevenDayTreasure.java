package com.mu.game.model.unit.player.sevenDay;

import com.mu.executor.Executor;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class SevenDayTreasure {
   private Player player;
   private int loginTotalday = 0;
   private HashMap hasFoundIndex = new HashMap();
   private boolean save;

   public SevenDayTreasure(Player player) {
      this.player = player;
   }

   public void init(int loginDays, String hasFoundStr) {
      try {
         if (hasFoundStr.trim().length() > 0) {
            String[] splits = hasFoundStr.split(";");
            String[] var7 = splits;
            int var6 = splits.length;

            for(int var5 = 0; var5 < var6; ++var5) {
               String s = var7[var5];
               if (s.trim().length() >= 1) {
                  String[] secSplits = s.split(",");
                  this.addFoundIndex(Integer.parseInt(secSplits[0]), Integer.parseInt(secSplits[1]));
               }
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      this.loginTotalday = loginDays;
      if (this.player.isNeedZeroClear() && this.needToSaveByfindCount() && this.loginTotalday < 7) {
         ++this.loginTotalday;
         this.setSave(true);
      }

      this.loginTotalday = Math.min(7, Math.max(this.loginTotalday, 1));
   }

   private void addFoundIndex(int index, int showIndex) {
      if (index >= 0 && index < 7) {
         this.hasFoundIndex.put(index, showIndex);
      }
   }

   public boolean isShowInMap() {
      if (this.getFoundSize() >= 7) {
         return false;
      } else {
         return this.isFunctionOpen();
      }
   }

   public int findTreasure(int index) {
      if (this.hasFoundByIndex(index)) {
         SystemMessage.writeMessage(this.player, 23201);
         return -1;
      } else if (this.getRemainCount() < 1) {
         SystemMessage.writeMessage(this.player, 23202);
         return -1;
      } else {
         int findOrder = this.getFoundSize() + 1;
         SevenDayTreasureData data = SevenDayTreasureData.getSevenData(findOrder);
         ItemDataUnit unit = data.getUnit();
         int result = this.player.getItemManager().addItem(unit).getResult();
         if (result != 1) {
            SystemMessage.writeMessage(this.player, result);
            return -1;
         } else {
            this.addFoundIndex(index, data.getIndex());
            this.savefind();
            return data.getIndex();
         }
      }
   }

   public boolean hasFoundByIndex(int index) {
      return this.hasFoundIndex.containsKey(index);
   }

   public boolean hasFoundByShowIndex(int showIndex) {
      return this.hasFoundIndex.containsValue(showIndex);
   }

   public Item getShowItem(int index) {
      if (!this.hasFoundByIndex(index)) {
         return null;
      } else {
         int showIndex = ((Integer)this.hasFoundIndex.get(index)).intValue();
         return SevenDayTreasureData.getShowItem(showIndex);
      }
   }

   public int getRemainCount() {
      if (!this.isShowInMap()) {
         return 0;
      } else if (this.getFoundSize() >= 7) {
         return 0;
      } else {
         int count = this.loginTotalday - this.hasFoundIndex.size();
         count = Math.max(count, 0);
         return count;
      }
   }

   public void savefind() {
      WriteOnlyPacket packet = Executor.SaveSevenDayTreasure.toPacket(this.getPlayer());
      this.getPlayer().writePacket(packet);
      packet.destroy();
      packet = null;
   }

   private boolean isFunctionOpen() {
      return FunctionOpenManager.isOpen(this.getPlayer(), 22);
   }

   public void openFunction() {
      this.loginTotalday = 1;
      this.setSave(true);
      UpdateMenu.update(this.player, 31);
   }

   public void onDaySkip() {
      if (this.isFunctionOpen()) {
         ++this.loginTotalday;
         if (this.needToSaveByfindCount()) {
            this.setSave(true);
         }

      }
   }

   private boolean needToSaveByfindCount() {
      return this.getFoundSize() < 7;
   }

   public String getFoundIndexStr() {
      StringBuffer sb = new StringBuffer();
      Iterator it = this.hasFoundIndex.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         sb.append(entry.getKey());
         sb.append(",");
         sb.append(entry.getValue());
         sb.append(";");
      }

      return sb.toString();
   }

   public void destory() {
      this.player = null;
      if (this.hasFoundIndex != null) {
         this.hasFoundIndex.clear();
         this.hasFoundIndex = null;
      }

   }

   public int getShowLoginTotalDay() {
      return this.getLoginTotalday() > 7 ? 7 : this.getLoginTotalday();
   }

   public HashMap getHasFoundIndex() {
      return this.hasFoundIndex;
   }

   public int getFoundSize() {
      return this.hasFoundIndex.size();
   }

   public boolean isSave() {
      return this.save;
   }

   public void setSave(boolean save) {
      this.save = save;
   }

   public int getLoginTotalday() {
      return this.loginTotalday;
   }

   public void setLoginTotalday(int loginTotalday) {
      this.loginTotalday = loginTotalday;
   }

   public Player getPlayer() {
      return this.player;
   }
}
