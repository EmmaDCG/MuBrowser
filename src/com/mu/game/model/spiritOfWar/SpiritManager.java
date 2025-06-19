package com.mu.game.model.spiritOfWar;

import com.mu.db.log.IngotChangeType;
import com.mu.executor.Executor;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.hallow.model.PartModel;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.spiritOfWar.filter.FilterGroup;
import com.mu.game.model.spiritOfWar.model.RefineItem;
import com.mu.game.model.spiritOfWar.model.SpiritModel;
import com.mu.game.model.spiritOfWar.model.SpiritRankModel;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatChange;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.stats.statId.StatIdCreator;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.popup.imp.SpiritRefinePopup;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.spiritOfWar.SpiritUseItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class SpiritManager {
   private Player player;
   private int rank = 1;
   private int level = 1;
   private long exp = 0L;
   private int ingotRefineCount;
   private HashMap itemCount;
   private List statList = new ArrayList();
   private String statStr = "";
   private int domineering = 0;
   private SortedMap conditions = new TreeMap();
   boolean save = false;

   public SpiritManager(Player player) {
      this.player = player;
   }

   public void init(int rank, int level, long exp, int ingotRefineCount, String itemCountStr, String conStr) {
      this.rank = Math.max(1, Math.min(rank, SpiritRankModel.maxRank));
      this.level = Math.max(Math.min(level, SpiritTools.RankMaxLevel), 1);
      this.exp = Math.max(0L, exp);
      this.ingotRefineCount = ingotRefineCount;
      this.itemCount = StringTools.analyzeIntegerMap(itemCountStr, ",");
      if (this.player.isNeedZeroClear()) {
         if (this.ingotRefineCount != 0) {
            this.setSave(true);
         }

         this.ingotRefineCount = 0;
      }

      this.initConditions(conStr);
      this.checkItemCount();
      this.addStat();
   }

   private void initConditions(String conStr) {
      try {
         if (conStr != null && conStr.trim().length() > 0) {
            String[] splits = conStr.split(";");
            String[] var6 = splits;
            int var5 = splits.length;

            for(int var4 = 0; var4 < var5; ++var4) {
               String split = var6[var4];
               String[] ss = split.split(",");
               int groupID = Integer.parseInt(ss[0]);
               int index = Integer.parseInt(ss[1]);
               if (FilterGroup.getFilterGroup(groupID) != null) {
                  this.conditions.put(groupID, index);
               }
            }
         }

         this.checkConditions();
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   private void checkConditions() {
      FilterGroup.checkPlayerCondition(this.conditions);
   }

   private void checkItemCount() {
      Iterator it = this.itemCount.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         if (SpiritTools.getRefineItem(((Integer)entry.getKey()).intValue()) == null) {
            it.remove();
         } else if (((Integer)entry.getValue()).intValue() < 1) {
            entry.setValue(Integer.valueOf(1));
         }
      }

   }

   private void setSave(boolean b) {
      this.save = b;
   }

   public void addStat() {
      if (this.isFunctionOpen()) {
         int domi = 0;
         SpiritModel model = this.getModel();
         domi = domi + model.getDomineering();
         this.statList.clear();
         Iterator var4 = model.getStats().iterator();

         while(var4.hasNext()) {
            FinalModify fm = (FinalModify)var4.next();
            this.statList.add(fm.cloneModify());
         }

         Iterator it = this.itemCount.entrySet().iterator();

         while(true) {
            RefineItem item;
            Entry entry;
            do {
               if (!it.hasNext()) {
                  this.statStr = PartModel.getStatString(this.statList);
                  this.setDomineering(domi);
                  this.statList.add(new FinalModify(StatEnum.DOMINEERING, domi, StatModifyPriority.ADD));
                  StatChange.addStat(this.getPlayer(), StatIdCreator.createSpiritID(), this.statList, true);
                  return;
               }

               entry = (Entry)it.next();
               item = SpiritTools.getRefineItem(((Integer)entry.getKey()).intValue());
            } while(item == null);

            int value = 0;
            Iterator var8 = item.getStatList().iterator();

            while(var8.hasNext()) {
               FinalModify fm = (FinalModify)var8.next();
               value = fm.getValue() * ((Integer)entry.getValue()).intValue();
               boolean add = false;
               Iterator var11 = this.statList.iterator();

               FinalModify tmpFm;
               while(var11.hasNext()) {
                  tmpFm = (FinalModify)var11.next();
                  if (fm.getStat() == tmpFm.getStat() && fm.getPriority() == tmpFm.getPriority()) {
                     tmpFm.setValue(tmpFm.getValue() + value);
                     add = true;
                     break;
                  }
               }

               if (!add) {
                  tmpFm = fm.cloneModify();
                  tmpFm.setValue(value);
                  this.statList.add(tmpFm);
               }
            }

            domi += item.getDomineering() * ((Integer)entry.getValue()).intValue();
         }
      }
   }

   public boolean isFunctionOpen() {
      return FunctionOpenManager.isOpen(this.getPlayer(), 23);
   }

   public void openFunction() {
      this.addStat();
   }

   public void useItem(int itemId, int count) {
      int value = count;
      if (this.itemCount.containsKey(itemId)) {
         value = count + ((Integer)this.itemCount.get(itemId)).intValue();
      }

      this.itemCount.put(itemId, value);
      this.addStat();
      this.saveSpirit();
      SpiritUseItem.sendToClient(this.getPlayer());
   }

   public int canUseItem(int itemID, int needRank) {
      if (!this.isFunctionOpen()) {
         return 23302;
      } else if (this.getRank() < needRank) {
         return 23304;
      } else {
         return this.getCanUseCount(itemID) < 1 ? 23303 : 1;
      }
   }

   private boolean isTop() {
      return this.rank >= SpiritRankModel.maxRank && this.level >= SpiritTools.RankMaxLevel;
   }

   public int canRefine() {
      if (!this.isFunctionOpen()) {
         return 23302;
      } else {
         return this.isTop() ? 23301 : 1;
      }
   }

   public int refineByIngot(boolean hasComfirm) {
      int result = this.canRefine();
      if (result != 1) {
         return result;
      } else {
         int[] ingot = SpiritTools.getRefineIngot(this.ingotRefineCount + 1);
         boolean useBind = true;
         if (this.player.getBindIngot() < ingot[1]) {
            useBind = false;
         }

         if (!useBind && this.player.getIngot() < ingot[0]) {
            return 1015;
         } else if (!useBind && !hasComfirm) {
            SpiritRefinePopup pop = new SpiritRefinePopup(this.player.createPopupID(), ingot[0]);
            ShowPopup.open(this.player, pop);
            return 23310;
         } else {
            this.addExp((long)this.getModel().getIngotExp(), true);
            if (useBind) {
               PlayerManager.reduceBindIngot(this.player, ingot[1], IngotChangeType.RefineSpirit, this.getRank() + "," + this.getLevel());
            } else {
               PlayerManager.reduceIngot(this.player, ingot[0], IngotChangeType.RefineSpirit, this.getRank() + "," + this.getLevel());
            }

            return 1;
         }
      }
   }

   public void addExp(long exp, boolean useIngot) {
      int result = this.canRefine();
      if (result == 1) {
         if (exp >= 1L) {
            if (useIngot) {
               ++this.ingotRefineCount;
            }

            this.exp += Math.min(exp, Long.MAX_VALUE - this.exp);
            result = this.levelUp();
            if (result == 1) {
               this.addStat();
            }

            this.saveSpirit();
         }
      }
   }

   private int levelUp() {
      long perNeedExp = SpiritTools.needExp(this.getRank(), this.getLevel());
      if (this.exp < perNeedExp) {
         return 0;
      } else {
         while(this.exp >= perNeedExp) {
            ++this.level;
            if (this.level > SpiritTools.RankMaxLevel) {
               this.level = 1;
               ++this.rank;
            }

            this.exp -= perNeedExp;
            if (this.isTop()) {
               break;
            }

            perNeedExp = SpiritTools.needExp(this.getRank(), this.getLevel());
         }

         return 1;
      }
   }

   public int getCanUseCount(int itemID) {
      int maxCount = this.getModel().getCanUseCount(itemID);
      maxCount -= this.getCount(itemID);
      return Math.max(maxCount, 0);
   }

   public int getCount(int itemID) {
      return this.itemCount.containsKey(itemID) ? ((Integer)this.itemCount.get(itemID)).intValue() : 0;
   }

   public void clearIngotCount() {
      this.ingotRefineCount = 0;
      this.setSave(true);
   }

   public void saveSpirit() {
      WriteOnlyPacket packet = Executor.SaveSpirit.toPacket(this.getPlayer());
      this.getPlayer().writePacket(packet);
      packet.destroy();
      packet = null;
      this.setSave(false);
   }

   public boolean needToSave() {
      return !this.isFunctionOpen() ? false : this.save;
   }

   public SpiritModel getModel() {
      return SpiritTools.getModel(this.getRank(), this.getLevel());
   }

   public String getItemCountStr() {
      StringBuffer sb = new StringBuffer();
      Iterator it = this.itemCount.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         sb.append(entry.getKey());
         sb.append(",");
         sb.append(entry.getValue());
         sb.append(";");
      }

      if (sb.length() > 1) {
         sb.deleteCharAt(sb.length() - 1);
      }

      return sb.toString();
   }

   public String getConditionStr() {
      StringBuffer sb = new StringBuffer();
      Iterator it = this.conditions.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         sb.append(entry.getKey());
         sb.append(",");
         sb.append(entry.getValue());
         sb.append(";");
      }

      if (sb.length() > 1) {
         sb.deleteCharAt(sb.length() - 1);
      }

      return sb.toString();
   }

   public int getRank() {
      return this.rank;
   }

   public Player getPlayer() {
      return this.player;
   }

   public void setPlayer(Player player) {
      this.player = player;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public int getIngotRefineCount() {
      return this.ingotRefineCount;
   }

   public void setIngotRefineCount(int ingotRefineCount) {
      this.ingotRefineCount = ingotRefineCount;
   }

   public HashMap getItemCount() {
      return this.itemCount;
   }

   public void setItemCount(HashMap itemCount) {
      this.itemCount = itemCount;
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   public String getStatStr() {
      return this.statStr;
   }

   public long getExp() {
      return this.exp;
   }

   public void setExp(long exp) {
      this.exp = exp;
   }
}
