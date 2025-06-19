package com.mu.game.model.equip.equipStat;

import com.mu.game.model.equip.excellent.ExcellentCreationData;
import com.mu.game.model.equip.excellent.ExcellentSort;
import com.mu.game.model.equip.lucky.LuckyCreationData;
import com.mu.game.model.equip.mosaic.MosaicCreationData;
import com.mu.game.model.equip.star.StarCreationData;
import com.mu.game.model.equip.star.StarForgingData;
import com.mu.game.model.equip.zhuijia.ZhuijiaCreationData;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemColor;
import com.mu.game.model.item.model.ItemModel;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EquipStatManager {
   private static Logger logger = LoggerFactory.getLogger(EquipStatManager.class);

   public static void resetItem(Item item, int ruleID, boolean hide) {
      EquipStatRule rule = EquipStatRule.getRule(ruleID);
      if (rule != null) {
         int lucky = LuckyCreationData.getData(rule.getLuckySortID()).getRndValue();
         int zhuijia = ZhuijiaCreationData.getData(rule.getZhuijiaSortID()).getRndValue();
         int starLevel = StarCreationData.getData(rule.getStrengthID()).getRndValue();
         int socket = MosaicCreationData.getData(rule.getMosaicSortID()).getRndValue();
         ExcellentSort eSort = ExcellentSort.getExcellentSort(rule.getExcellentSortID());
         int excellentSize = eSort.getRndValue();
         if (hide) {
            lucky = LuckyCreationData.getData(rule.getLuckySortID()).getBestValue();
            zhuijia = ZhuijiaCreationData.getData(rule.getZhuijiaSortID()).getBestValue();
            starLevel = StarCreationData.getData(rule.getStrengthID()).getBestValue();
            socket = MosaicCreationData.getData(rule.getMosaicSortID()).getBestValue();
            excellentSize = eSort.getBestValue();
         }

         starLevel = Math.min(starLevel, StarForgingData.getMaxStarLevel(item.getItemType()));
         item.setStarLevel(starLevel);
         item.setSocket(socket);
         SortedMap otherStats = null;
         if (excellentSize > 0) {
            ExcellentCreationData data = ExcellentCreationData.getData(eSort.getDataID(), item.getItemType());
            if (data != null) {
               otherStats = data.getElement().getExcellentStats(excellentSize);
               item.setOtherStats(otherStats);
               otherStats.clear();
               otherStats = null;
            } else {
               logger.error("卓越属性池不存在 ，dataID = " + eSort.getDataID() + "，物品类型= " + item.getItemType());
            }
         }

         if (lucky > 0) {
            addLuckyStat(item.getOtherStats());
         }

         item.setZhuijiaLevel(zhuijia);
         resetItemQuality(item);
      }
   }

   public static void resetItemQuality(Item item) {
      if (!item.isEquipment()) {
         item.setQuality(item.getModel().getQuality());
      } else {
         int quality = ItemColor.COLOR_WHITE.getIdentity();
         int socket = item.getSocket();
         int excellentSize = item.getBonusStatSize(3);
         int lucky = item.getBonusStatSize(2);
         int zhuijiaLevel = item.getZhuijiaLevel();
         if (socket > 0) {
            quality = ItemColor.COLOR_ORANGE.getIdentity();
         } else if (excellentSize > 0) {
            quality = ItemColor.COLOR_GREEN.getIdentity();
         } else if (lucky > 0 || zhuijiaLevel > 0) {
            quality = ItemColor.COLOR_BLUE.getIdentity();
         }

         item.setQuality(quality);
      }
   }

   public static void addLuckyStat(SortedMap allStats) {
      HashMap luckyStatMap = EquipStat.getLuckyStats();
      Iterator var3 = luckyStatMap.values().iterator();

      while(var3.hasNext()) {
         EquipStat stat = (EquipStat)var3.next();
         allStats.put(stat.getId(), stat.cloneStat());
      }

   }

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      String excelName = "属性生成规则文档 - ";
      Sheet equipstatSheet = wb.getSheet(1);
      EquipStat.init(equipstatSheet);
      Sheet excellentStatSheet = wb.getSheet(3);
      ExcellentCreationData.init(excelName, excellentStatSheet);
      Sheet excellentSortSheet = wb.getSheet(2);
      ExcellentSort.init(excelName, excellentSortSheet);
      Sheet mosaicSheet = wb.getSheet(4);
      MosaicCreationData.init(excelName, mosaicSheet);
      Sheet zhuijiaSheet = wb.getSheet(5);
      ZhuijiaCreationData.init(excelName, zhuijiaSheet);
      Sheet strengthSheet = wb.getSheet(6);
      StarCreationData.init(excelName, strengthSheet);
      Sheet luckySheet = wb.getSheet(7);
      LuckyCreationData.init(excelName, luckySheet);
      Sheet ruleSheet = wb.getSheet(8);
      EquipStatRule.init(ruleSheet);
      ItemModel.initStarActivation(wb.getSheet(9));
   }
}
