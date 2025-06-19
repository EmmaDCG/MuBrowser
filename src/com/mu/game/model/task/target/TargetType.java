package com.mu.game.model.task.target;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum TargetType {
   COUNT(1),
   SPECIFY(2),
   VALUE(3),
   VisitNpc(4),
   MoreSPECIFY(5);

   private static Logger logger = LoggerFactory.getLogger(TargetType.class);
   private int id;

   private TargetType(int id) {
      this.id = id;
   }

   public static TargetType valueOf(int id) {
      TargetType[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         TargetType type = var4[var2];
         if (type.getId() == id) {
            return type;
         }
      }

      return null;
   }

   public int getId() {
      return this.id;
   }

   public static enum CountType {
      TASK_END(1),
      XunBao(2),
      QiangHua(3),
      ZhuiJia(4),
      Task_XuanShang(5),
      KillMonster(6),
      KillWorldBoss(7),
      Task_RiChang(8),
      KillPersonalBoss(9),
      TanXian(10);

      private int id;

      private CountType(int id) {
         this.id = id;
      }

      public int getId() {
         return this.id;
      }

      public static TargetType.CountType valueOf(int id) {
         TargetType.CountType[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            TargetType.CountType type = var4[var2];
            if (type.getId() == id) {
               return type;
            }
         }

         TargetType.logger.error("init task config error, not found CountType " + id);
         return null;
      }
   }

   public static enum MoreSpecifyType {
      Equiment_Position(1),
      Equiment_Item(2),
      SKILL(3),
      ITEM(4);

      private int id;

      private MoreSpecifyType(int id) {
         this.id = id;
      }

      public int getId() {
         return this.id;
      }

      public static TargetType.MoreSpecifyType valueOf(int id) {
         TargetType.MoreSpecifyType[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            TargetType.MoreSpecifyType type = var4[var2];
            if (type.getId() == id) {
               return type;
            }
         }

         TargetType.logger.error("init task config error, not found MoreSpecifyType " + id);
         return null;
      }
   }

   public static enum SpecifyType {
      KillMonster(1),
      UseItem(2),
      CollectionItem(3),
      SubmitTask(4),
      FB_Enter_Count(6),
      FB_Appraise(7),
      ZB_Jie_Count(8),
      ZB_Jie_Jia_Count(9),
      HC_COUNT(10),
      COUNT_ZhuoYue(11),
      COUNT_XinYun(12);

      private int id;

      private SpecifyType(int id) {
         this.id = id;
      }

      public int getId() {
         return this.id;
      }

      public static TargetType.SpecifyType valueOf(int id) {
         TargetType.SpecifyType[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            TargetType.SpecifyType type = var4[var2];
            if (type.getId() == id) {
               return type;
            }
         }

         TargetType.logger.error("init task config error, not found SpecifyType " + id);
         return null;
      }
   }

   public static enum ValueType {
      RoleLevel(1),
      MONEY(3),
      SUM_QiangHua(4),
      SUM_ZhuoYue(5),
      SUM_BaoShi(6),
      MAX_ZhuiJia(7),
      SUM_ZhuiJia(8),
      MAX_TaoZhuang(9),
      ZhanPing(10),
      Pet_Rank(11),
      Vip_Level(12),
      JiaRuZhanMeng(13),
      MAX_QiangHua(14);

      private int id;

      private ValueType(int id) {
         this.id = id;
      }

      public int getId() {
         return this.id;
      }

      public static TargetType.ValueType valueOf(int id) {
         TargetType.ValueType[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            TargetType.ValueType type = var4[var2];
            if (type.getId() == id) {
               return type;
            }
         }

         TargetType.logger.error("init task config error, not found ValueType " + id);
         return null;
      }
   }
}
