package com.mu.game.model.item;

import com.mu.game.model.equip.equipSet.EquipSetModel;
import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.equip.forging.ForgingRuleDes;
import com.mu.game.model.equip.newStone.StoneDataManager;
import com.mu.game.model.equip.star.StarForgingData;
import com.mu.game.model.equip.zhuijia.ZhuijiaForgingData;
import com.mu.game.model.item.model.ItemCreateType;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.stats.ItemModify;
import com.mu.game.model.stats.StatEnum;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Item {
   private long id;
   private int modelID = -1;
   private int count;
   private int slot;
   private int containerType;
   private int starLevel;
   private int zhuijiaLevel;
   private int socket;
   private boolean isBind = false;
   private int quality;
   private int moneyType = 1;
   private int shopMoney = 0;
   private int score = 0;
   private int starUpTimes;
   private int onceMaxStarLevel;
   private long expireTime = -1L;
   private int durability = 0;
   private boolean hide;
   private List basisStats = new ArrayList();
   private SortedMap otherStats = new TreeMap();
   private List stones = new ArrayList();
   private List runes = new ArrayList();
   private int setItemSize = 0;
   private int domineering = 0;

   public Item(long id, int modelID, int count) {
      this.id = id;
      this.modelID = modelID;
      this.quality = ItemModel.getModel(modelID).getQuality();
      this.setCount(count);
   }

   public void reset(int newModelID) {
      this.modelID = newModelID;
      this.basisStats.clear();
      ItemModel model = ItemModel.getModel(newModelID);
      this.basisStats.addAll(model.cloneItemStats());
      this.calculateScore();
   }

   public void calculateScore() {
      if (this.isEquipment()) {
         int domi = this.getModel().getDomineering();
         domi += StarForgingData.getDomineering(this);
         domi += ZhuijiaForgingData.getDomeneering(this);
         EquipStat stat;
         Iterator var3;
         if (this.getOtherStats() != null) {
            for(var3 = this.getOtherStats().values().iterator(); var3.hasNext(); domi += stat.getDomineering()) {
               stat = (EquipStat)var3.next();
            }
         }

         EquipStat stat2;
         if (this.getModel().getStarActivation().size() > 0) {
            Iterator it = this.getModel().getStarActivation().entrySet().iterator();

            label42:
            while(true) {
               Entry entry;
               do {
                  if (!it.hasNext()) {
                     break label42;
                  }

                  entry = (Entry)it.next();
               } while(this.getStarLevel() < ((Integer)entry.getKey()).intValue());

               for(Iterator var5 = ((List)entry.getValue()).iterator(); var5.hasNext(); domi += stat2.getDomineering()) {
                  stat2 = (EquipStat)var5.next();
               }
            }
         }

         if (this.getStones().size() > 0) {
            for(var3 = this.getStones().iterator(); var3.hasNext(); domi += stat.getDomineering()) {
               ItemStone stone = (ItemStone)var3.next();
               stat = EquipStat.getEquipStat(stone.getEquipStatID());
            }
         }

         this.setDomineering(domi);
      }
   }

   public boolean isEquipment() {
      return this.getModel().isEquipment() || this.getModel().getSort() == 1;
   }

   public boolean hasLucky() {
      return this.getBonusStatSize(2) > 0;
   }

   public void setBasisStats(List basisStats) {
      this.basisStats.clear();
      if (basisStats != null && basisStats.size() > 0) {
         this.basisStats.addAll(basisStats);
      }

   }

   public void setOtherStats(SortedMap otherStats) {
      this.otherStats.clear();
      if (otherStats != null && otherStats.size() > 0) {
         this.otherStats.putAll(otherStats);
      }

   }

   public void increaseCount(int inceaseCount, long newExpireTime) {
      this.setCount(this.count + inceaseCount);
      if (newExpireTime > this.expireTime) {
         this.expireTime = newExpireTime;
      }

   }

   public boolean decreaseCount(int changeCount) {
      int tmpCount = this.count - changeCount;
      if (tmpCount >= 0) {
         this.setCount(tmpCount);
         return true;
      } else {
         this.setCount(0);
         return false;
      }
   }

   public void changeStarLevel(int newStarLevel) {
      this.setStarLevel(newStarLevel);
      this.calculateScore();
   }

   public void changeZhuijiaLevel(int newZhuijiaLevel) {
      this.setZhuijiaLevel(newZhuijiaLevel);
      this.calculateScore();
   }

   public void addStone(ItemStone stone, boolean calScore) {
      this.stones.add(stone);
      this.sortStones(calScore);
   }

   public void sortStones(boolean calScore) {
      int index = 0;

      for(Iterator var4 = this.stones.iterator(); var4.hasNext(); ++index) {
         ItemStone stone = (ItemStone)var4.next();
         stone.setIndex(index);
      }

      if (calScore) {
         this.calculateScore();
      }

   }

   public ItemStone removeStone(int index) {
      if (index > this.stones.size() - 1) {
         return null;
      } else {
         ItemStone stone = (ItemStone)this.stones.remove(index);
         this.sortStones(true);
         return stone;
      }
   }

   public void removeAllStone() {
      this.stones.clear();
      this.sortStones(true);
   }

   public ItemStone getStoneById(int index) {
      return index > this.stones.size() - 1 ? null : (ItemStone)this.stones.get(index);
   }

   public boolean hasStone() {
      return this.stones.size() > 0;
   }

   public boolean hasStoneByModelID(int modelID) {
      Iterator var3 = this.stones.iterator();

      while(var3.hasNext()) {
         ItemStone stone = (ItemStone)var3.next();
         if (stone.getModelID() == modelID) {
            return true;
         }
      }

      return false;
   }

   public long getShowExpireTime() {
      return this.expireTime == -1L ? this.expireTime : this.getExpireTime();
   }

   public boolean isCalExpireTime() {
      return this.getContainerType() != 16;
   }

   public boolean isTimeLimited() {
      return this.expireTime != -1L;
   }

   public boolean isTimeExpired(long now) {
      return this.isTimeLimited() && now > this.getExpireTime();
   }

   public void addRune(ItemRune rune, boolean calScore) {
      this.runes.add(rune);
      this.sortRune(calScore);
   }

   public void replayceRune(int index, int modelID) {
      int tmpIndex = -1;

      for(int i = 0; i < this.runes.size(); ++i) {
         if (index == ((ItemRune)this.runes.get(i)).getIndex()) {
            tmpIndex = i;
            break;
         }
      }

      if (tmpIndex != -1) {
         ItemRune rune = (ItemRune)this.runes.get(tmpIndex);
         rune.setModelID(modelID);
      } else {
         this.runes.add(new ItemRune(modelID, index));
      }

      this.sortRune(true);
   }

   public ItemRune getRuneByIndex(int index) {
      return index > this.runes.size() - 1 ? null : (ItemRune)this.runes.get(index);
   }

   public ItemRune removeRuneByIndex(int index) {
      if (index > this.runes.size() - 1) {
         return null;
      } else {
         ItemRune rune = (ItemRune)this.runes.remove(index);
         this.sortRune(true);
         return rune;
      }
   }

   public void sortRune(boolean calScore) {
      int index = 0;
      Iterator var4 = this.runes.iterator();

      while(var4.hasNext()) {
         ItemRune rune = (ItemRune)var4.next();
         rune.setIndex(index);
      }

      if (calScore) {
         this.calculateScore();
      }

   }

   public int getFirstStatValue(StatEnum stat) {
      Iterator var3 = this.basisStats.iterator();

      ItemModify modify;
      while(var3.hasNext()) {
         modify = (ItemModify)var3.next();
         if (modify.getStat() == stat) {
            return modify.getValue();
         }
      }

      var3 = this.otherStats.values().iterator();

      while(var3.hasNext()) {
         modify = (ItemModify)var3.next();
         if (modify.getStat() == stat) {
            return modify.getValue();
         }
      }

      return 0;
   }

   public SortedMap getClassifyStats() {
      if (this.otherStats.size() < 1) {
         return null;
      } else {
         SortedMap sortMaps = new TreeMap();

         ItemModify stat;
         Object statList;
         for(Iterator var3 = this.otherStats.values().iterator(); var3.hasNext(); ((List)statList).add(stat)) {
            stat = (ItemModify)var3.next();
            statList = (List)sortMaps.get(stat.getBonusType());
            if (statList == null) {
               statList = new ArrayList();
               sortMaps.put(stat.getBonusType(), statList);
            }
         }

         return sortMaps;
      }
   }

   public List getBonusClassifyStats(int bonusType) {
      if (this.otherStats.size() < 1) {
         return null;
      } else {
         List list = null;
         Iterator var4 = this.otherStats.values().iterator();

         while(var4.hasNext()) {
            ItemModify modify = (ItemModify)var4.next();
            if (modify.getBonusType() == bonusType) {
               if (list == null) {
                  list = new ArrayList();
               }

               list.add(modify);
            }
         }

         return list;
      }
   }

   public int getBonusStatSize(int bonusType) {
      if (this.otherStats.size() < 1) {
         return 0;
      } else {
         int size = 0;
         Iterator var4 = this.otherStats.values().iterator();

         while(var4.hasNext()) {
            ItemModify modify = (ItemModify)var4.next();
            if (modify.getBonusType() == bonusType) {
               ++size;
            }
         }

         return size;
      }
   }

   public ItemModify getFirstModify(int bonusType) {
      switch(bonusType) {
      case 0:
         if (this.basisStats.size() > 0) {
            return (ItemModify)this.basisStats.get(0);
         }

         return null;
      default:
         Iterator var3 = this.otherStats.values().iterator();

         while(var3.hasNext()) {
            ItemModify modify = (ItemModify)var3.next();
            if (modify.getBonusType() == bonusType) {
               return modify;
            }
         }

         return null;
      }
   }

   public Item cloneItem(int createType) {
      Item newItem = null;

      try {
         long itemObjID = ItemCreateType.getItemObjId(createType);
         newItem = new Item(itemObjID, this.modelID, this.count);
         newItem.setBind(this.isBind);
         newItem.setSlot(this.slot);
         newItem.setContainerType(this.containerType);
         newItem.setStarLevel(this.starLevel);
         newItem.setSocket(this.socket);
         newItem.setMoneyType(this.moneyType);
         newItem.setShopMoney(this.shopMoney);
         newItem.setScore(this.score);
         newItem.setStarUpTimes(this.starUpTimes);
         newItem.setOnceMaxStarLevel(this.onceMaxStarLevel);
         newItem.setExpireTime(this.expireTime);
         newItem.setDurability(this.durability);
         newItem.setHide(this.hide);
         newItem.setZhuijiaLevel(this.zhuijiaLevel);
         newItem.setQuality(this.getQuality());
         List newBasis = new ArrayList();
         Iterator var7 = this.basisStats.iterator();

         while(var7.hasNext()) {
            ItemModify modify = (ItemModify)var7.next();
            newBasis.add(modify.cloneModify());
         }

         newItem.setBasisStats(newBasis);
         SortedMap newOthers = new TreeMap();
         Iterator var8 = this.otherStats.values().iterator();

         while(var8.hasNext()) {
            EquipStat other = (EquipStat)var8.next();
            newOthers.put(other.getId(), other.cloneStat());
         }

         newItem.setOtherStats(newOthers);
         var8 = this.stones.iterator();

         while(var8.hasNext()) {
            ItemStone stone = (ItemStone)var8.next();
            newItem.addStone(stone.cloneItemStrone(), false);
         }

         var8 = this.runes.iterator();

         while(var8.hasNext()) {
            ItemRune rune = (ItemRune)var8.next();
            newItem.addRune(rune.cloneRune(), false);
         }

         newItem.calculateScore();
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      return newItem;
   }

   public int getItemType() {
      return this.getModel().getItemType();
   }

   public int getItemSort() {
      return this.getModel().getSort();
   }

   public int getUserLevel() {
      return this.getModel().getUseLevel();
   }

   public int getLevel() {
      return this.getModel().getLevel();
   }

   public int getMoney() {
      switch(this.getContainerType()) {
      case 11:
      case 16:
         return this.shopMoney;
      default:
         return this.getMoneySellToNPC();
      }
   }

   public int getMoneySellToNPC() {
      return this.getModel().getSellNpcPrice();
   }

   public int getSetID() {
      int setID = this.getModel().getSets();
      if (!EquipSetModel.hasSet(setID)) {
         setID = -1;
      }

      return setID;
   }

   public boolean isEquipSet() {
      return this.getModel().isEquipSet();
   }

   public boolean isInBackpack() {
      return this.getContainerType() == 1;
   }

   public String getStoneStr() {
      return StringTools.getStoneStr(this.stones);
   }

   public String getRuneStr() {
      return StringTools.getRuneStr(this.runes);
   }

   public String getBasisStr() {
      return this.isEquipment() ? "" : StringTools.getModifyString(this.basisStats);
   }

   public String getOtherStr() {
      return StringTools.getEquipStatString(this.otherStats);
   }

   public int getShowSockety() {
      return !this.isHide() && StoneDataManager.canMosaicByType(this.getItemType()) ? this.socket : this.socket;
   }

   public int getMaxDurability() {
      return this.getModel().getDurability();
   }

   public boolean isHide() {
      return this.hide;
   }

   public void setHide(boolean hide) {
      this.hide = hide;
   }

   public final long getID() {
      return this.id;
   }

   public int getQuality() {
      return this.quality;
   }

   public void setQuality(int quality) {
      this.quality = quality;
   }

   public String getName() {
      return ForgingRuleDes.assemItemShowName(this);
   }

   public ItemModel getModel() {
      return ItemModel.getModel(this.modelID);
   }

   public final boolean isBind() {
      return this.isBind;
   }

   public final void setBind(boolean isBind) {
      this.isBind = isBind;
   }

   public final long getExpireTime() {
      return this.expireTime;
   }

   public final void setExpireTime(long expireTime) {
      this.expireTime = expireTime;
   }

   public final int getSlot() {
      return this.slot;
   }

   public final void setSlot(int slot) {
      this.slot = slot;
   }

   public void setId(long id) {
      this.id = id;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getContainerType() {
      return this.containerType;
   }

   public void setContainerType(int containerType) {
      this.containerType = containerType;
   }

   public int getStarLevel() {
      return this.starLevel;
   }

   public void setStarLevel(int starLevel) {
      this.starLevel = starLevel;
   }

   public int getSocket() {
      return this.socket;
   }

   public void setSocket(int socket) {
      this.socket = socket;
   }

   public int getMoneyType() {
      return this.moneyType;
   }

   public void setMoneyType(int moneyType) {
      this.moneyType = moneyType;
   }

   public int getShopMoney() {
      return this.shopMoney;
   }

   public void setShopMoney(int shopMoney) {
      this.shopMoney = shopMoney;
   }

   public int getScore() {
      return this.score;
   }

   public void setScore(int score) {
      this.score = score;
   }

   public int getStarUpTimes() {
      return this.starUpTimes;
   }

   public void setStarUpTimes(int starUpTimes) {
      this.starUpTimes = starUpTimes;
   }

   public int getOnceMaxStarLevel() {
      return this.onceMaxStarLevel;
   }

   public void setOnceMaxStarLevel(int onceMaxStarLevel) {
      if (onceMaxStarLevel < this.getStarLevel()) {
         onceMaxStarLevel = this.getStarLevel();
      }

      this.onceMaxStarLevel = onceMaxStarLevel;
   }

   public List getBasisStats() {
      return this.basisStats;
   }

   public SortedMap getOtherStats() {
      return this.otherStats;
   }

   public final int getCount() {
      return this.count;
   }

   public int getZhuijiaLevel() {
      return this.zhuijiaLevel;
   }

   public void setZhuijiaLevel(int zhuijiaLevel) {
      this.zhuijiaLevel = zhuijiaLevel;
   }

   public final void setCount(int count) {
      this.count = count;
   }

   public int getDurability() {
      return this.durability;
   }

   public void setDurability(int durability) {
      this.durability = durability;
   }

   public List getStones() {
      return this.stones;
   }

   public List getRunes() {
      return this.runes;
   }

   public int getSetItemSize() {
      if (this.containerType != 0) {
         int setID = this.getSetID();
         if (setID != -1) {
            return 1;
         }
      }

      return this.setItemSize;
   }

   public void setSetItemSize(int setItemSize) {
      this.setItemSize = setItemSize;
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   public void destroy() {
      if (this.basisStats != null) {
         this.basisStats.clear();
         this.basisStats = null;
      }

      if (this.otherStats != null) {
         this.otherStats.clear();
         this.otherStats = null;
      }

      if (this.runes != null) {
         this.runes.clear();
         this.runes = null;
      }

      if (this.stones != null) {
         this.stones.clear();
         this.stones = null;
      }

   }
}
