package com.mu.game.model.unit.player.extarget;

import com.mu.game.model.item.ItemDataUnit;
import java.util.HashMap;

public class ExTarget {
   private int id;
   private String name;
   private int pageId;
   private int titleId;
   private ItemDataUnit rewardUnit = null;
   private HashMap targetElementMap = new HashMap();
   private HashMap targetMap = new HashMap();

   public ExTarget(int id) {
      this.id = id;
   }

   public HashMap getElementMap(int pro) {
      return (HashMap)this.targetElementMap.get(pro);
   }

   public ExTargetElement getElement(int pro, int index) {
      HashMap map = this.getElementMap(pro);
      return map != null ? (ExTargetElement)map.get(index) : null;
   }

   public int getElementSize(int pro) {
      HashMap map = (HashMap)this.targetElementMap.get(pro);
      return map != null ? map.size() : 0;
   }

   public void addElement(int pro, ExTargetElement element) {
      HashMap map = (HashMap)this.targetElementMap.get(pro);
      if (map == null) {
         map = new HashMap();
         this.targetElementMap.put(pro, map);
      }

      map.put(element.getIndex(), element);
   }

   public void addItemTarget(int pro, int modelId, int index) {
      HashMap map = (HashMap)this.targetMap.get(pro);
      if (map == null) {
         map = new HashMap();
         this.targetMap.put(pro, map);
      }

      map.put(modelId, pro);
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getPageId() {
      return this.pageId;
   }

   public void setPageId(int pageId) {
      this.pageId = pageId;
   }

   public int getTitleId() {
      return this.titleId;
   }

   public void setTitleId(int titleId) {
      this.titleId = titleId;
   }

   public ItemDataUnit getRewardUnit() {
      return this.rewardUnit;
   }

   public void setRewardUnit(ItemDataUnit rewardUnit) {
      this.rewardUnit = rewardUnit;
   }
}
