package com.mu.game.model.unit;

import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class CreatureTemplate {
   private static HashMap templateMap = new HashMap();
   private int templateId;
   private int modelId;
   private int header;
   private int scale = 100;
   private int leftEquip = -1;
   private int rightEquip = -1;
   private int leftEffect = 0;
   private int rightEffect = 0;
   private int appearMusic = -1;
   private int dieMusic = -1;
   private int attackMusic = -1;
   private int moveMusic = -1;
   private int idleMusic = -1;
   private int wingEquip = -1;
   private int wingEffect = -1;
   private ArrayList entryList = new ArrayList();
   private ArrayList popTextList = null;

   public static void initTemplate(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet[] sheets = wb.getSheets();

      for(int j = 1; j < sheets.length; ++j) {
         Sheet sheet = wb.getSheet(j);
         int rows = sheet.getRows();
         int columns = sheet.getColumns();

         for(int i = 2; i <= rows; ++i) {
            CreatureTemplate ct = new CreatureTemplate(Tools.getCellIntValue(sheet.getCell("A" + i)));
            ct.setModelId(Tools.getCellIntValue(sheet.getCell("B" + i)));
            ct.setHeader(Tools.getCellIntValue(sheet.getCell("C" + i)));
            ct.setScale((int)(Float.parseFloat(Tools.getCellValue(sheet.getCell("E" + i))) * 100.0F));
            ct.setLeftEquip(Tools.getCellIntValue(sheet.getCell("F" + i)));
            ct.setRightEquip(Tools.getCellIntValue(sheet.getCell("G" + i)));
            ct.setLeftEffect(Tools.getCellIntValue(sheet.getCell("H" + i)));
            ct.setRightEffect(Tools.getCellIntValue(sheet.getCell("I" + i)));
            ct.setAppearMusic(Tools.getCellIntValue(sheet.getCell("K" + i)));
            ct.setDieMusic(Tools.getCellIntValue(sheet.getCell("L" + i)));
            ct.setAttackMusic(Tools.getCellIntValue(sheet.getCell("M" + i)));
            ct.setMoveMusic(Tools.getCellIntValue(sheet.getCell("N" + i)));
            ct.setIdleMusic(Tools.getCellIntValue(sheet.getCell("O" + i)));
            ct.setWingEquip(Tools.getCellIntValue(sheet.getCell("P" + i)));
            ct.setWingEffect(Tools.getCellIntValue(sheet.getCell("Q" + i)));
            if (ct.getScale() <= 0) {
               throw new Exception("怪物缩放错误");
            }

            if (columns > 17) {
               String popTmp = Tools.getCellValue(sheet.getCell("R" + i));
               if (popTmp != null && !popTmp.trim().equals("")) {
                  String[] pop = popTmp.split("#_#");
                  ArrayList list = new ArrayList();

                  for(int k = 0; k < pop.length; ++k) {
                     list.add(pop[k]);
                  }

                  ct.setPopTextList(list);
               }
            }

            if (ct.getLeftEquip() > 0) {
               ct.entryList.add(new ExternalEntry(6, ct.getLeftEquip(), ct.getLeftEffect()));
            }

            if (ct.getRightEquip() > 0) {
               ct.entryList.add(new ExternalEntry(7, ct.getRightEquip(), ct.getRightEffect()));
            }

            if (ct.getWingEquip() > 0) {
               ct.entryList.add(new ExternalEntry(8, ct.getWingEquip(), ct.getWingEffect()));
            }

            templateMap.put(ct.getTemplateId(), ct);
         }
      }

   }

   public static boolean hasTemplate(int templateId) {
      return templateMap.containsKey(templateId);
   }

   public static CreatureTemplate getTemplate(int templateId) {
      return (CreatureTemplate)templateMap.get(templateId);
   }

   public CreatureTemplate(int id) {
      this.templateId = id;
   }

   public int getTemplateId() {
      return this.templateId;
   }

   public int getWingEquip() {
      return this.wingEquip;
   }

   public void setWingEquip(int wingEquip) {
      this.wingEquip = wingEquip;
   }

   public int getWingEffect() {
      return this.wingEffect;
   }

   public void setWingEffect(int wingEffect) {
      this.wingEffect = wingEffect;
   }

   public int getModelId() {
      return this.modelId;
   }

   public void setModelId(int modelId) {
      this.modelId = modelId;
   }

   public int getHeader() {
      return this.header;
   }

   public void setHeader(int header) {
      this.header = header;
   }

   public int getScale() {
      return this.scale;
   }

   public void setScale(int scale) {
      this.scale = scale;
   }

   public int getLeftEquip() {
      return this.leftEquip;
   }

   public void setLeftEquip(int leftEquip) {
      this.leftEquip = leftEquip;
   }

   public int getRightEquip() {
      return this.rightEquip;
   }

   public void setRightEquip(int rightEquip) {
      this.rightEquip = rightEquip;
   }

   public int getLeftEffect() {
      return this.leftEffect;
   }

   public void setLeftEffect(int leftEffect) {
      this.leftEffect = leftEffect;
   }

   public int getRightEffect() {
      return this.rightEffect;
   }

   public void setRightEffect(int rightEffect) {
      this.rightEffect = rightEffect;
   }

   public ArrayList getEquipEntry() {
      return this.entryList;
   }

   public int getAppearMusic() {
      return this.appearMusic;
   }

   public void setAppearMusic(int appearMusic) {
      this.appearMusic = appearMusic;
   }

   public int getMoveMusic() {
      return this.moveMusic;
   }

   public void setMoveMusic(int moveMusic) {
      this.moveMusic = moveMusic;
   }

   public int getAttackMusic() {
      return this.attackMusic;
   }

   public void setAttackMusic(int attackMusic) {
      this.attackMusic = attackMusic;
   }

   public int getDieMusic() {
      return this.dieMusic;
   }

   public void setDieMusic(int dieMusic) {
      this.dieMusic = dieMusic;
   }

   public int getIdleMusic() {
      return this.idleMusic;
   }

   public void setIdleMusic(int idleMusic) {
      this.idleMusic = idleMusic;
   }

   public ArrayList getPopTextList() {
      return this.popTextList;
   }

   public void setPopTextList(ArrayList popTextList) {
      this.popTextList = popTextList;
   }
}
