package com.mu.game.model.mall;

import com.mu.game.model.item.Item;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MallConfigManager {
   private static Logger logger = LoggerFactory.getLogger(MallConfigManager.class);
   private static List cxList0 = new ArrayList();
   private static List cxList1 = new ArrayList();
   private static List cxList2 = new ArrayList();
   private static MallLabelData[] labelArr = new MallLabelData[0];
   private static Map itemMap = new HashMap();
   private static Map modelMap = new HashMap();

   public static void initConfigs(InputStream in) throws BiffException, IOException {
      Workbook wb = null;

      try {
         wb = Workbook.getWorkbook(in);
         Sheet[] sheets = wb.getSheets();
         List cxList0 = new ArrayList();
         List cxList1 = new ArrayList();
         List cxList2 = new ArrayList();
         MallLabelData[] labelArr = new MallLabelData[sheets.length - 4];
         Map itemMap = new HashMap();
         Map modelMap = new HashMap();
         initMallGroupConfig(sheets[1], cxList0, itemMap, modelMap);
         initMallGroupConfig(sheets[2], cxList1, itemMap, modelMap);
         initMallGroupConfig(sheets[3], cxList2, itemMap, modelMap);

         for(int i = 0; i < labelArr.length; ++i) {
            labelArr[i] = new MallLabelData(sheets[i + 4].getName());
            initMallGroupConfig(sheets[i + 4], labelArr[i].getItemList(), itemMap, modelMap);
            labelArr[i].setPriceType(((MallItemData)labelArr[i].getItemList().get(0)).getPriceType());
         }

         cxList0 = cxList0;
         cxList1 = cxList1;
         cxList2 = cxList2;
         labelArr = labelArr;
         itemMap = itemMap;
         modelMap = modelMap;
      } finally {
         if (wb != null) {
            wb.close();
         }

      }
   }

   private static void initMallGroupConfig(Sheet sheet, List list, Map itemMap, Map modelMap) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         String G = sheet.getCell("G" + i).getContents().trim();
         String H = sheet.getCell("H" + i).getContents().trim();
         String I = sheet.getCell("I" + i).getContents().trim();
         MallItemData mid = new MallItemData(Integer.parseInt(A), 1);
         mid.setId(Integer.parseInt(I));
         mid.setStatRuleID(Integer.parseInt(B));
         mid.setPriceType(Integer.parseInt(C));
         mid.setPrice1(Integer.parseInt(D));
         mid.setPrice2(Integer.parseInt(E));
         mid.setVipLevel(Integer.parseInt(F));
         mid.setExpireTime(Long.parseLong(G));
         mid.setBind("1".equals(H));
         mid.buildItem();
         Item item = mid.getItem();
         if (item == null) {
            logger.error("MallItemData[{}] config error, can not build item!", mid.getModelID());
         } else {
            modelMap.put(mid.getModelID(), mid);
            list.add(mid);
            itemMap.put(item.getID(), mid);
         }
      }

   }

   public static List getCXList(int profession) {
      return profession == 0 ? cxList0 : (profession == 1 ? cxList1 : (profession == 2 ? cxList2 : null));
   }

   public static MallLabelData[] getLabelArr() {
      return labelArr;
   }

   public static MallItemData getData(long id) {
      return (MallItemData)itemMap.get(id);
   }

   public static MallItemData getData(int modelId) {
      return (MallItemData)modelMap.get(modelId);
   }
}
