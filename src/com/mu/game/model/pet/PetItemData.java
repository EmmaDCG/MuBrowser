package com.mu.game.model.pet;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.utils.CommonRegPattern;
import java.util.regex.Matcher;

public class PetItemData extends ItemDataUnit {
   private int value;
   private Item item = ItemTools.createItem(2, this);
   private int lll;
   private int lul;

   public PetItemData(int modelID, int count) {
      super(modelID, count);
   }

   public static PetItemData parseValueFormStr(String str) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(str);
      m.find();
      int modelID = Integer.parseInt(m.group());
      m.find();
      int count = Integer.parseInt(m.group());
      int value = m.find() ? Integer.parseInt(m.group()) : 0;
      PetItemData data = new PetItemData(modelID, count);
      data.setValue(value);
      return data;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public int getLll() {
      return this.lll;
   }

   public void setLll(int lll) {
      this.lll = lll;
   }

   public int getLul() {
      return this.lul;
   }

   public void setLul(int lul) {
      this.lul = lul;
   }

   public Item getItem() {
      return this.item;
   }
}
