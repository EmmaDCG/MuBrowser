package com.mu.game.model.item;

import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.equip.equipStat.EquipStatManager;
import com.mu.game.model.item.model.ItemCreateType;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.service.StringTools;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemTools {
   private static Logger logger = LoggerFactory.getLogger(ItemTools.class);

   public static void setSystemExpire(Item item, long expireDuration) {
      item.setContainerType(16);
      item.setExpireTime(expireDuration);
   }

   private static Item createItem(long itemObjID, int modelID, int count, boolean isBind, boolean createBonus) {
      try {
         if (count < 1) {
            return null;
         } else {
            ItemModel model = ItemModel.getModel(modelID);
            if (model == null) {
               logger.error(itemObjID + "  模板Id = " + modelID + "  --  物品模型不存在");
               return null;
            } else {
               int tmpCount = count;
               if (count > model.getMaxStackCount()) {
                  tmpCount = model.getMaxStackCount();
               }

               Item item = new Item(itemObjID, modelID, tmpCount);
               item.setBind(isBind);
               List basisStats = model.cloneItemStats();
               item.setBasisStats(basisStats);
               if (basisStats != null) {
                  basisStats.clear();
               }

               item.setSocket(0);
               item.calculateScore();
               model = null;
               return item;
            }
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         return null;
      }
   }

   public static Item createItem(int modelId, int count, int createType) {
      ItemDataUnit unit = new ItemDataUnit(modelId, count);
      return createItem(createType, unit);
   }

   public static Item createItem(int creatType, ItemDataUnit unit) {
      long itemObjID = ItemCreateType.getItemObjId(creatType);
      Item item = createItem(itemObjID, unit.getModelID(), unit.getCount(), unit.isBind(), unit.createBonus());
      if (item != null) {
         item.setDurability(item.getModel().getDurability());
         item.setExpireTime(unit.getActualExpireTime());
         item.setBind(unit.isBind());
         item.setHide(unit.isHide());
         if (item.isEquipment()) {
            EquipStatManager.resetItem(item, unit.getStatRuleID(), unit.isHide());
            item.calculateScore();
         }
      }

      return item;
   }

   public static Item loadItem(ItemSaveAide isa) {
      ItemModel model = ItemModel.getModel(isa.getModelID());
      if (model == null) {
         logger.error("模板Id " + isa.getModelID() + "  --  物品模型不存在");
         return null;
      } else {
         int count = isa.getCount();
         if (count > model.getMaxStackCount()) {
            count = model.getMaxStackCount();
         }

         Item item = new Item(isa.getItemId(), isa.getModelID(), count);
         item.setSlot(isa.getSlot());
         item.setStarLevel(isa.getStarLevel());
         item.setBind(isa.isBind());
         item.setContainerType(isa.getContainerType());
         item.setSocket(isa.getSocket());
         item.setMoneyType(isa.getMoneyType());
         item.setShopMoney(isa.getMoney());
         item.setStarUpTimes(isa.getStarUpTimes());
         item.setOnceMaxStarLevel(isa.getOnceMaxStarLevel());
         item.setExpireTime(isa.getExpireTime());
         item.setDurability(isa.getDurability());
         item.setZhuijiaLevel(isa.getZhuijiaLevel());
         List bs = null;
         if (item.isEquipment()) {
            bs = item.getModel().cloneItemStats();
         } else {
            bs = StringTools.analyzeItemBasicModifies(isa.getBasisStats());
         }

         item.setBasisStats((List)bs);
         ((List)bs).clear();
         if (isa.getOtherStats() != null && isa.getOtherStats().trim().length() > 0) {
            SortedMap os = StringTools.analyzeItemEquipStat(isa.getOtherStats());
            item.setOtherStats(os);
            os.clear();
         }

         setItemStone(item, isa.getStones());
         setItemRune(item, isa.getRunes());
         EquipStatManager.resetItemQuality(item);
         item.calculateScore();
         return item;
      }
   }

   private static void setItemStone(Item item, String stones) {
      if (stones != null && stones.trim().length() >= 1) {
         String[] splits = stones.split(";");
         String[] var6 = splits;
         int var5 = splits.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            String stone = var6[var4];
            String[] sSplits = stone.split(",");
            if (sSplits.length < 2) {
               logger.error("宝石数据不正确 ，道具ID = {},宝石字符串 = {}", item.getID(), stones);
            } else {
               try {
                  int modelID = Integer.parseInt(sSplits[0]);
                  int equipStatID = Integer.parseInt(sSplits[1]);
                  if (ItemModel.getModel(modelID) == null) {
                     logger.error("宝石数据不正确 ，道具ID = {},宝石ID = {}", item.getID(), modelID);
                  } else if (EquipStat.getEquipStat(equipStatID) == null) {
                     logger.error("宝石数据不正确 ，道具ID = {}, 属性ID = {}", item.getID(), equipStatID);
                  } else {
                     item.addStone(new ItemStone(modelID, equipStatID, 0), false);
                  }
               } catch (Exception var10) {
                  var10.printStackTrace();
               }
            }
         }

      }
   }

   private static void setItemRune(Item item, String runes) {
      HashSet runeIds = StringTools.analyzeIntegerHashset(runes, ";");
      Iterator var4 = runeIds.iterator();

      while(var4.hasNext()) {
         Integer runeId = (Integer)var4.next();
         if (ItemModel.getModel(runeId.intValue()) == null) {
            logger.error("没有相应的符文ID ,objId = {},stoneId = {}", item.getID(), runeId);
         } else {
            item.addRune(new ItemRune(runeId.intValue(), 0), false);
         }
      }

   }

   public static String getGmShowStones(long itemID, String stones) {
      if (stones != null && stones.trim().length() >= 1) {
         StringBuffer sb = new StringBuffer();
         String[] splits = stones.split(";");
         String[] var8 = splits;
         int var7 = splits.length;

         for(int var6 = 0; var6 < var7; ++var6) {
            String stone = var8[var6];
            String[] sSplits = stone.split(",");
            if (sSplits.length < 2) {
               logger.error("GM查询-宝石数据不正确 ，道具ID = {},宝石字符串 = {}", itemID, stones);
            } else {
               try {
                  int modelID = Integer.parseInt(sSplits[0]);
                  int equipStatID = Integer.parseInt(sSplits[1]);
                  if (ItemModel.getModel(modelID) == null) {
                     logger.error("GM查询-宝石数据不正确 ，道具ID = {},宝石ID = {}", itemID, modelID);
                  } else if (EquipStat.getEquipStat(equipStatID) == null) {
                     logger.error("GM查询-宝石数据不正确 ，道具ID = {}, 属性ID = {}", itemID, equipStatID);
                  } else {
                     ItemModel model = ItemModel.getModel(modelID);
                     sb.append(model.getName());
                     sb.append("{");
                     sb.append(EquipStat.getGMShowStr(equipStatID));
                     sb.append("}");
                     sb.append("&nbsp");
                  }
               } catch (Exception var13) {
                  var13.printStackTrace();
               }
            }
         }

         return sb.toString();
      } else {
         return "";
      }
   }

   public static String getGMShowRuneString(long itemID, String runes) {
      if (runes.trim().length() < 1) {
         return "";
      } else {
         String s = "";
         HashSet runeIds = StringTools.analyzeIntegerHashset(runes, ";");
         Iterator var6 = runeIds.iterator();

         while(var6.hasNext()) {
            Integer runeId = (Integer)var6.next();
            if (ItemModel.getModel(runeId.intValue()) == null) {
               logger.error("没有相应的符文ID ,objId = {},stoneId = {}", itemID, runeId);
            } else {
               s = s + ItemModel.getModel(runeId.intValue()).getName();
            }
         }

         return s;
      }
   }

   public static String[] getGmShowExllents(long itemID, String stats) {
      String[] excellents = new String[]{"", ""};
      if (stats != null && stats.trim().length() >= 1) {
         String[] splits = stats.split(",");
         String[] var8 = splits;
         int var7 = splits.length;

         for(int var6 = 0; var6 < var7; ++var6) {
            String s = var8[var6];

            try {
               int equipStatID = Integer.parseInt(s);
               if (EquipStat.getEquipStat(equipStatID) == null) {
                  logger.error("GM查询-卓越属性不正确 ，道具ID = {}, 属性ID = {}", itemID, equipStatID);
               } else if (EquipStat.isLucky(equipStatID)) {
                  excellents[0] = excellents[0] + EquipStat.getGMShowStr(equipStatID);
               } else {
                  excellents[1] = excellents[1] + EquipStat.getGMShowStr(equipStatID);
               }
            } catch (Exception var10) {
               var10.printStackTrace();
            }
         }

         return excellents;
      } else {
         return excellents;
      }
   }

   public static boolean canStack(Item item, Item otherItem) {
      if (item != null && otherItem != null) {
         if (item.getModelID() == otherItem.getModelID() && item.isBind() == otherItem.isBind() && item.getModel().getMaxStackCount() > 1) {
            return item.getExpireTime() == otherItem.getExpireTime();
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean canStack(Item item, boolean isBind, long expiresTime) {
      if (item == null) {
         return false;
      } else {
         return item.getModel().getMaxStackCount() > 1 && isBind == item.isBind() && item.getExpireTime() == expiresTime;
      }
   }

   public static long getCurrentDateLong() {
      Calendar cal = Calendar.getInstance();
      return getDateLong(cal);
   }

   public static long getDateLong(Calendar cal) {
      int year = cal.get(1);
      int month = cal.get(2) + 1;
      int day = cal.get(5);
      int hour = cal.get(11);
      int mm = cal.get(12);
      int second = cal.get(13);
      return (long)year * 10000000000L + (long)month * 100000000L + (long)day * 1000000L + (long)(hour * 10000) + (long)(mm * 100) + (long)second;
   }
}
