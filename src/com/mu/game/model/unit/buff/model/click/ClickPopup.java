package com.mu.game.model.unit.buff.model.click;

import com.mu.game.model.unit.buff.Buff;
import com.mu.game.model.unit.buff.model.click.imp.OpenWebpageClickPopup;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public abstract class ClickPopup {
   public static final int Click_OpenBaiduMember = 1;
   private static HashMap popupMap = new HashMap();
   private int id;
   private int type;

   public ClickPopup(int id, int type) {
      this.type = type;
      this.id = id;
   }

   public abstract void doClick(Player var1, Buff var2);

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         int type = Tools.getCellIntValue(sheet.getCell("C" + i));
         String value = Tools.getCellValue(sheet.getCell("D" + i));
         ClickPopup popup = createClickPopup(id, type, value);
         if (popup != null) {
            popupMap.put(popup.getId(), popup);
         }
      }

   }

   public static ClickPopup createClickPopup(int id, int type, String value) throws Exception {
      switch(type) {
      case 1:
         if (value != null && value.trim().length() >= 1) {
            return new OpenWebpageClickPopup(id, type, value);
         }

         throw new Exception("点击方式 = " + id + " - url地址填写有错");
      default:
         return null;
      }
   }

   public static ClickPopup getClickPopup(int id) {
      return (ClickPopup)popupMap.get(id);
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }
}
