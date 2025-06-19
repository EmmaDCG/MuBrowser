package com.mu.game.model.equip.mosaic;

import com.mu.game.model.equip.equipStat.EquipStatCreation;
import com.mu.game.model.equip.weight.CreationElement;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class MosaicCreationData extends EquipStatCreation {
   private static HashMap datas = new HashMap();

   public MosaicCreationData(int sortID) {
      super(sortID);
   }

   public static void init(String excelName, Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int sortID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String weightStr = Tools.getCellValue(sheet.getCell("C" + i));
         CreationElement element = new CreationElement();
         element.parse(weightStr, excelName + "镶嵌个数解析 " + sortID);
         MosaicCreationData data = new MosaicCreationData(sortID);
         data.setElement(element);
         datas.put(sortID, data);
      }

   }

   public static MosaicCreationData getData(int sortID) {
      return (MosaicCreationData)datas.get(sortID);
   }

   public static boolean hasSortID(int sortID) {
      return datas.containsKey(sortID);
   }
}
