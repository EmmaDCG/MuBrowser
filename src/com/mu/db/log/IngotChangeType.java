package com.mu.db.log;

import com.mu.game.model.item.model.ItemModel;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public enum IngotChangeType {
   None(0, "其他类型"),
   PersonTransaction(1, "玩家交易"),
   BuyItemFromNPC(2, "购买NPC商品"),
   InheriteEquipment(3, "装备转移"),
   Wing(4, "翅膀"),
   GM(5, "gm命令"),
   PET(6, "佣兵"),
   GANGLEVELUP(7, "帮派升级"),
   CLEARDUNCD(8, "清除副本冷却"),
   TRANSFER(9, "转职"),
   TRIALRECEIVE(10, "试炼领取"),
   TASK(11, "任务"),
   ZHUIJIA(12, "追加"),
   OpenBox(13, "开箱子"),
   Market(14, "市场交易"),
   Mall(15, "商城"),
   BloodMultiReceive(16, "血色多倍领取"),
   DevilMultiReceive(17, "恶魔多倍领取"),
   BloodInspire(18, "血色钻石鼓舞"),
   DevilInspire(19, "恶魔钻石鼓舞"),
   DungeonInspire(20, "副本钻石鼓舞"),
   StrengthEquip(21, "强化装备"),
   OpenBackpack(22, "开启背包格子"),
   GMPay(23, "GM充值"),
   Financing(24, "投资理财"),
   OperateMaigcItem(25, "魔盒"),
   SIGN(26, "签到"),
   USEITEM(27, "使用道具"),
   TeHui(28, "特惠活动"),
   PlatPay(29, "平台充值"),
   Recover(30, "副本追回"),
   RedPacket(31, "发红包"),
   ReceiveRedPacket(32, "收红包"),
   GangWelfare(33, "帮派福利"),
   WeekReward(34, "周奖励"),
   UpdateEquip(35, "装备升级"),
   RefineSpirit(36, "战魂淬炼"),
   TanXian(37, "探险"),
   LuckyTurnTable(38, "幸运大转盘");

   private static HashMap types = new HashMap();
   private int type;
   private String des;

   public static void init() {
      IngotChangeType[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         IngotChangeType type = var3[var1];
         types.put(type.getType(), type);
      }

   }

   public static IngotChangeType find(int type) {
      return types.containsKey(type) ? (IngotChangeType)types.get(type) : None;
   }

   public static String getTypeName(int type) {
      return find(type).getDes();
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int typeID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String des = Tools.getCellValue(sheet.getCell("B" + i));
         IngotChangeType type = find(typeID);
         type.setDes(des);
      }

   }

   private IngotChangeType(int type, String des) {
      this.type = type;
      this.des = des;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public static String getItemLogDetail(int modelID) {
      return modelID + "(" + ItemModel.getModel(modelID).getName() + ")";
   }
}
