package com.mu.game.model.item.box;

import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.weight.WeightElement;
import com.mu.utils.Rnd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoxItem {
   private int boxID;
   private WeightElement weightItems = null;
   private List rateItems = null;
   private int rateCount = 0;
   private int openNeedItemId = -1;
   private int openNeedCount = 0;
   private int ingot = 0;
   private boolean firecracker;
   private boolean tip;

   public BoxItem(int boxID) {
      this.boxID = boxID;
   }

   public void check(String des) throws Exception {
      if (this.weightItems != null || this.rateItems != null && this.rateItems.size() >= 1) {
         if (this.weightItems != null && this.weightItems.getMaxWeight() < 1) {
            throw new Exception(des + " : 权重物品概率不正确");
         } else if (this.rateItems != null && (this.rateItems.size() < 1 || this.rateCount != -1 && this.rateCount < 1)) {
            throw new Exception(des + " : 概率物品填写不完整");
         } else {
            if (this.openNeedItemId != -1) {
               if (ItemModel.getModel(this.openNeedItemId) == null) {
                  throw new Exception(des + " : 开箱子钥匙道具不存在");
               }

               if (this.openNeedCount < 1) {
                  throw new Exception(des + " : 开箱子道具数量填写不正确");
               }
            }

         }
      } else {
         throw new Exception(des + " : 没有填写概率和权重物品数据");
      }
   }

   public List getItemData() {
      List unitList = new ArrayList();
      if (this.rateItems != null) {
         for(int pollingTimes = 10; pollingTimes > 0; --pollingTimes) {
            Iterator var4 = this.rateItems.iterator();

            while(var4.hasNext()) {
               BoxItemAtom atom = (BoxItemAtom)var4.next();
               int rate = Rnd.get(100000);
               if (rate < atom.getWeight()) {
                  ItemDataUnit unit = atom.getDataUnit();
                  unitList.add(unit);
               }
            }

            if (unitList.size() > 0) {
               break;
            }
         }

         int tmpCount;
         if (unitList.size() < 1) {
            tmpCount = Rnd.get(0, this.rateItems.size() - 1);
            BoxItemAtom atom = (BoxItemAtom)this.rateItems.get(tmpCount);
            ItemDataUnit unit = atom.getDataUnit();
            unitList.add(unit);
         }

         if (this.rateCount != -1 && unitList.size() > this.rateCount) {
            for(tmpCount = unitList.size() - this.rateCount; tmpCount > 0; --tmpCount) {
               int rndIndex = Rnd.get(0, unitList.size() - 1);
               unitList.remove(rndIndex);
            }
         }
      }

      if (this.weightItems != null) {
         BoxItemAtom atom = (BoxItemAtom)this.weightItems.getRndAtom();
         ItemDataUnit unit = atom.getDataUnit();
         unitList.add(unit);
      }

      return unitList;
   }

   public int getBoxID() {
      return this.boxID;
   }

   public void setBoxID(int boxID) {
      this.boxID = boxID;
   }

   public WeightElement getWeightItems() {
      return this.weightItems;
   }

   public void setWeightItems(WeightElement weightItems) {
      this.weightItems = weightItems;
   }

   public boolean isFirecracker() {
      return this.firecracker;
   }

   public void setFirecracker(boolean firecracker) {
      this.firecracker = firecracker;
   }

   public List getRateItems() {
      return this.rateItems;
   }

   public void setRateItems(List rateItems) {
      this.rateItems = rateItems;
   }

   public int getRateCount() {
      return this.rateCount;
   }

   public void setRateCount(int rateCount) {
      this.rateCount = rateCount;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public int getOpenNeedItemId() {
      return this.openNeedItemId;
   }

   public void setOpenNeedItemId(int openNeedItemId) {
      this.openNeedItemId = openNeedItemId;
   }

   public int getOpenNeedCount() {
      return this.openNeedCount;
   }

   public void setOpenNeedCount(int openNeedCount) {
      this.openNeedCount = openNeedCount;
   }

   public boolean isTip() {
      return this.tip;
   }

   public void setTip(boolean tip) {
      this.tip = tip;
   }
}
