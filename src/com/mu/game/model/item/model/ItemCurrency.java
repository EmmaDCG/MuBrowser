package com.mu.game.model.item.model;

import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class ItemCurrency {
   private static HashMap currencies = new HashMap();
   private int moneyType;
   private String moneyName;
   private int icon;
   private int statID;

   public ItemCurrency(int moneyType, String moneyName, int icon, int statID) {
      this.moneyType = moneyType;
      this.moneyName = moneyName;
      this.icon = icon;
      this.statID = statID;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int moneyType = Tools.getCellIntValue(sheet.getCell("A" + i));
         String moneyName = Tools.getCellValue(sheet.getCell("B" + i));
         int icon = Tools.getCellIntValue(sheet.getCell("C" + i));
         int statID = Tools.getCellIntValue(sheet.getCell("D" + i));
         ItemCurrency currency = new ItemCurrency(moneyType, moneyName, icon, statID);
         currencies.put(currency.getMoneyType(), currency);
      }

   }

   public static HashMap getAllCurrencies() {
      return currencies;
   }

   public static ItemCurrency getCurrency(int moneyType) {
      return (ItemCurrency)currencies.get(moneyType);
   }

   public int getMoneyType() {
      return this.moneyType;
   }

   public void setMoneyType(int moneyType) {
      this.moneyType = moneyType;
   }

   public String getMoneyName() {
      return this.moneyName;
   }

   public void setMoneyName(String moneyName) {
      this.moneyName = moneyName;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public int getStatID() {
      return this.statID;
   }

   public void setStatID(int statID) {
      this.statID = statID;
   }
}
