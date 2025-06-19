package com.mu.game.model.unit.player.bluevip;

import com.mu.utils.Tools;
import flexjson.JSONDeserializer;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class BlueVip {
   public static final int Valid_Check_Time = 259200;
   private static HashMap iconMap = new HashMap();
   public static final int Vip_None = 0;
   public static final int Vip_Normal = 1;
   public static final int Vip_Year = 2;
   public static final int Vip_Super = 3;
   public static final int Vip_Super_Year = 4;
   private boolean is_blue_vip = false;
   private boolean is_blue_year_vip = false;
   private boolean is_super_blue_vip = false;
   private boolean is_expand_blue_vip = false;
   private int blue_vip_level = 0;
   private boolean is_have_growth = false;
   private boolean is_mobile_blue_vip = false;
   private long server_time;
   private long vip_reg_time;
   private long year_vip_reg_time;
   private long super_vip_reg_time;
   private long expand_vip_reg_time;
   private long vip_valid_time;
   private long year_vip_valid_time;
   private long super_vip_valid_time;
   private long expand_vip_valid_time;
   private String tag = "0";
   private boolean needCheckValid = false;

   public static void initBlueIcon(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         String tag = Tools.getCellValue(sheet.getCell("A" + i));
         String[] iconStr = Tools.getCellValue(sheet.getCell("B" + i)).split(",");
         String text = Tools.getCellValue(sheet.getCell("C" + i));
         if (text == null) {
            text = "";
         }

         BlueIcon bi = new BlueIcon();
         bi.setTag(tag);
         bi.setText(text);
         int[] icons = bi.getIcons();

         for(int j = 0; j < iconStr.length; ++j) {
            icons[j] = Integer.parseInt(iconStr[j]);
         }

         String title = Tools.getCellValue(sheet.getCell("D" + i));
         String des = Tools.getCellValue(sheet.getCell("E" + i));
         bi.setPrivilege(title + "|" + des);
         iconMap.put(tag, bi);
      }

   }

   public static BlueIcon getBlueIcon(String tag) {
      BlueIcon bi = (BlueIcon)iconMap.get(tag);
      return bi == null ? (BlueIcon)iconMap.get("0") : bi;
   }

   public void init(String json) {
      JSONDeserializer der = new JSONDeserializer();
      HashMap map = (HashMap)der.deserialize(json);
      int ret = Integer.valueOf(map.get("ret").toString()).intValue();
      if (ret == 0) {
         this.server_time = Long.valueOf(map.get("server_time").toString()).longValue();
         this.is_expand_blue_vip = Integer.parseInt(map.get("is_expand_blue_vip").toString()) == 1;
         this.expand_vip_valid_time = Long.valueOf(map.get("expand_vip_valid_time").toString()).longValue();
         this.super_vip_reg_time = Long.valueOf(map.get("super_vip_reg_time").toString()).longValue();
         this.super_vip_valid_time = Long.valueOf(map.get("super_vip_valid_time").toString()).longValue();
         this.year_vip_valid_time = Long.valueOf(map.get("year_vip_valid_time").toString()).longValue();
         this.blue_vip_level = Integer.parseInt(map.get("blue_vip_level").toString());
         this.is_super_blue_vip = Integer.parseInt(map.get("is_super_blue_vip").toString()) == 1;
         this.expand_vip_reg_time = Long.valueOf(map.get("expand_vip_reg_time").toString()).longValue();
         this.year_vip_reg_time = Long.valueOf(map.get("year_vip_reg_time").toString()).longValue();
         this.is_blue_year_vip = Long.valueOf(map.get("is_blue_year_vip").toString()).longValue() == 1L;
         this.is_have_growth = Integer.parseInt(map.get("is_have_growth").toString()) == 1;
         this.vip_reg_time = Long.valueOf(map.get("vip_reg_time").toString()).longValue();
         this.is_blue_vip = Integer.parseInt(map.get("is_blue_vip").toString()) == 1;
         this.is_mobile_blue_vip = Integer.parseInt(map.get("is_mobile_blue_vip").toString()) == 1;
         this.vip_valid_time = Long.valueOf(map.get("vip_valid_time").toString()).longValue();
         this.initTag();
         this.initCheckValid();
      }

   }

   public void initCheckValid() {
      if (this.isVip()) {
         this.needCheckValid = true;
      } else {
         this.needCheckValid = Math.abs(this.getValidTime()) < 259200L;
      }

   }

   private void initTag() {
      if (this.blue_vip_level != 0) {
         if (this.is_blue_year_vip) {
            if (this.is_super_blue_vip) {
               this.tag = "4_" + this.blue_vip_level;
            } else {
               this.tag = "2_" + this.blue_vip_level;
            }
         } else if (this.is_super_blue_vip) {
            this.tag = "3_" + this.blue_vip_level;
         } else {
            this.tag = "1_" + this.blue_vip_level;
         }
      }

   }

   public boolean isVip() {
      return this.is_blue_vip || this.is_blue_year_vip || this.is_super_blue_vip || this.is_expand_blue_vip;
   }

   public boolean is_blue_vip() {
      return this.is_blue_vip;
   }

   public boolean is_blue_year_vip() {
      return this.is_blue_year_vip;
   }

   public boolean is_super_blue_vip() {
      return this.is_super_blue_vip;
   }

   public boolean is_expand_blue_vip() {
      return this.is_expand_blue_vip;
   }

   public int getBlue_vip_level() {
      return this.blue_vip_level;
   }

   public boolean is_have_growth() {
      return this.is_have_growth;
   }

   public boolean is_mobile_blue_vip() {
      return this.is_mobile_blue_vip;
   }

   public long getServer_time() {
      return this.server_time;
   }

   public long getVip_reg_time() {
      return this.vip_reg_time;
   }

   public long getYear_vip_reg_time() {
      return this.year_vip_reg_time;
   }

   public long getSuper_vip_reg_time() {
      return this.super_vip_reg_time;
   }

   public long getExpand_vip_reg_time() {
      return this.expand_vip_reg_time;
   }

   public long getVip_valid_time() {
      return this.vip_valid_time;
   }

   public long getYear_vip_valid_time() {
      return this.year_vip_valid_time;
   }

   public long getSuper_vip_valid_time() {
      return this.super_vip_valid_time;
   }

   public long getExpand_vip_valid_time() {
      return this.expand_vip_valid_time;
   }

   public String getTag() {
      return this.tag;
   }

   public long getValidTime() {
      long now = System.currentTimeMillis() / 1000L;
      if (this.year_vip_valid_time > 0L) {
         return this.year_vip_valid_time - now;
      } else if (this.expand_vip_valid_time > 0L) {
         return this.year_vip_valid_time - now;
      } else {
         return this.vip_valid_time > 0L ? this.vip_valid_time - now : 0L;
      }
   }

   public boolean isNeedCheckValid() {
      return this.needCheckValid;
   }

   public BlueIcon getBlueIcon() {
      return getBlueIcon(this.getTag());
   }
}
