package com.mu.game.model.shield;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.utils.CommonRegPattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

public class ShieldRank {
   private int rank;
   private int star;
   private int limit;
   private Item expendItem;
   private int expendItemId;
   private int expendItemCount;
   private int addValue;
   private int effect;
   private int zdl;
   private ShieldRank next;
   private List propertyList = new ArrayList();
   private List propertyList2 = new ArrayList();
   private HashMap propertyMap = new HashMap();

   public List getPropertyList() {
      return this.propertyList;
   }

   public List getPropertyList2() {
      return this.propertyList2;
   }

   public HashMap getPropertyMap() {
      return this.propertyMap;
   }

   public void addProperty(String str) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(str);
      m.find();
      int id = Integer.parseInt(m.group());
      m.find();
      int value = Integer.parseInt(m.group());
      m.find();
      int type = Integer.parseInt(m.group());
      FinalModify fm = new FinalModify(StatEnum.find(id), value, StatModifyPriority.fine(type));
      this.propertyMap.put(fm.getStat(), fm);
      this.propertyList.add(fm);
      this.propertyList2.add(fm);
   }

   public void setExpendStr(String expendStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(expendStr);
      m.find();
      this.expendItemId = Integer.parseInt(m.group());
      m.find();
      this.expendItemCount = Integer.parseInt(m.group());
      m.find();
      this.addValue = Integer.parseInt(m.group());
      this.expendItem = ItemTools.createItem(this.expendItemId, this.expendItemCount, 2);
   }

   public int getRank() {
      return this.rank;
   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public int getStar() {
      return this.star;
   }

   public void setStar(int star) {
      this.star = star;
   }

   public int getLimit() {
      return this.limit;
   }

   public void setLimit(int limit) {
      this.limit = limit;
   }

   public int getExpendItemId() {
      return this.expendItemId;
   }

   public int getExpendItemCount() {
      return this.expendItemCount;
   }

   public int getAddValue() {
      return this.addValue;
   }

   public ShieldRank getNext() {
      return this.next;
   }

   public void setNext(ShieldRank next) {
      this.next = next;
   }

   public Item getExpendItem() {
      return this.expendItem;
   }

   public int getEffect() {
      return this.effect;
   }

   public void setEffect(int effect) {
      this.effect = effect;
   }

   public int getZDL() {
      return this.zdl;
   }

   public void setZDL(int zdl) {
      this.zdl = zdl;
      FinalModify fm = new FinalModify(StatEnum.DOMINEERING, zdl, StatModifyPriority.ADD);
      this.propertyList2.add(fm);
   }
}
