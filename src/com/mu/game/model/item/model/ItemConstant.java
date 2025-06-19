package com.mu.game.model.item.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemConstant {
   private static HashMap statSortMap = new HashMap();
   private static List clientShowStatSortOrder = new ArrayList();
   public static final int StatT_Basic = 0;
   public static final int StatT_zhuijia = 1;
   public static final int StatT_Lucky = 2;
   public static final int StatT_Excellent = 3;
   public static final int StatT_Special = 4;
   public static final int StatT_Master = 5;
   public static final int StatT_Active = 6;
   public static final int StatT_Stone = 11;
   public static final int StatT_Rune = 12;
   public static final int StatT_Set = 13;
   public static final int[] StatT_Array = new int[]{1, 2, 3, 4, 5};
   public static final int ContainerT_All = -1;
   public static final int ContainerT_Equipment = 0;
   public static final int ContainerT_Backpack = 1;
   public static final int ContainerT_Depot = 4;
   public static final int ContainerT_ShortCut = 7;
   public static final int ContainerT_Mosaic = 10;
   public static final int ContainerT_Inventory_Box = 8;
   public static final int ContainerT_Depot_Box = 9;
   public static final int ContainerT_Shop = 11;
   public static final int ContainerT_Horse = 12;
   public static final int ContainerT_Card = 13;
   public static final int ContainerT_Xunbao = 14;
   public static final int ContainerT_System = 16;
   public static final int MoneyT_Common = 1;
   public static final int MoneyT_Ingot = 2;
   public static final int MoneyT_BindMoney = 3;
   public static final int MoneyT_BindIngot = 4;
   public static final int MoneyT_Points = 5;
   public static final int Backpack_DefaultPage = 49;
   public static final int Backpack_AdditionCount = 1;
   public static final int Backpack_MaxPage = 98;
   public static final int Depot_DefaultPage = 1;
   public static final int Depot_AdditionCount = 81;
   public static final int Depot_MaxPage = 4;
   public static final int TreasureHouse_DefaultPage = 1;
   public static final int TreasureHouse_AdditionCount = 300;
   public static final int TreasureHouse_MaxPage = 1;
   public static final int Delete_First_Bind = 1;
   public static final int Delete_First_NoBind = 2;
   public static final int QuickSaleSize = 24;
   public static final int AutoSaleVacantCount = 5;
   public static final int AutoSaleInterval = 10000;
   public static final int AutoMendEquipInterval = 10000;
   public static final int WashPropertyCount = 100;
   public static final int BackPage_Full_TipCount = 3;
   public static final int ItemShowTime = 14400000;
   public static final int StrengthGiveLevel = 7;
   public static final int AutoUseItemMaxLevel = 90;

   public static String getStatSortName(int sortID) {
      return statSortMap.containsKey(sortID) ? ((EquipStatSort)statSortMap.get(sortID)).getName() : "";
   }

   public static int getStatSortFont(int sortID) {
      return statSortMap.containsKey(sortID) ? ((EquipStatSort)statSortMap.get(sortID)).getFont() : 0;
   }

   public static void setStatSort(EquipStatSort ss) {
      statSortMap.put(ss.getSortID(), ss);
      clientShowStatSortOrder.add(ss.getSortID());
   }

   public static List getClientShowStatSortOrder() {
      return clientShowStatSortOrder;
   }

   public static String getContainerName(int type) {
      switch(type) {
      case 0:
         return "身上装备";
      case 1:
         return "背包";
      case 4:
         return "仓库";
      case 11:
         return "市场";
      default:
         return "其他";
      }
   }
}
