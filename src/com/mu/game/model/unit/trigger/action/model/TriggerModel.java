package com.mu.game.model.unit.trigger.action.model;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.trigger.action.ActionEnum;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class TriggerModel {
   private static HashMap models = new HashMap();
   private int modelID;
   private int triggereID;
   private boolean metux;
   private int rate;
   private int duration;
   private String otherParams;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum;

   public TriggerModel(int modelID, int triggereID, boolean metux, int rate, int duration, String otherParams) {
      this.modelID = modelID;
      this.triggereID = triggereID;
      this.metux = metux;
      this.rate = rate;
      this.duration = duration;
      this.otherParams = otherParams;
   }

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet modelSheet = wb.getSheet(2);
      initModel(modelSheet);
      Sheet triStatSheet = wb.getSheet(1);
      TriggerStats.init(triStatSheet);
   }

   private static void initModel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int modelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int triggerID = Tools.getCellIntValue(sheet.getCell("B" + i));
         boolean metux = Tools.getCellFloatValue(sheet.getCell("C" + i)) == 1.0F;
         int duration = Tools.getCellIntValue(sheet.getCell("D" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("E" + i));
         String otherParams = Tools.getCellValue(sheet.getCell("F" + i));
         TriggerModel model = new TriggerModel(modelID, triggerID, metux, rate, duration, otherParams);
         model.check();
         models.put(modelID, model);
      }

   }

   public static TriggerModel getModel(int modelID) {
      return (TriggerModel)models.get(modelID);
   }

   private void check() throws Exception {
      ActionEnum ae = ActionEnum.find(this.triggereID);
      if (ae == ActionEnum.None) {
         throw new Exception("触发 - 行为ID不存在 ，" + this.modelID);
      } else {
         String[] splits = null;
         int triggerStatId = 0;
         int resStatId = 0;
         int value = 0;
         //check this part
         switch($SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum()[ae.ordinal()]) {
         case 6:
            splits = this.otherParams.split(",");
            if (splits.length < 4) {
               throw new Exception("触发事件 - 参数错误 1，ID  = " + this.modelID);
            }

            int buffID = Integer.parseInt(splits[0]);
            int level = Integer.parseInt(splits[1]);
            triggerStatId = Integer.parseInt(splits[2]);
            resStatId = Integer.parseInt(splits[3]);
            if (!BuffModel.hasModel(buffID)) {
               throw new Exception("触发事件 - 参数错误2 ，ID  = " + this.modelID);
            }

            if (level < 1) {
               throw new Exception("触发事件 - 参数错误3 ，ID  = " + this.modelID);
            }

            if (StatEnum.find(triggerStatId) == StatEnum.None) {
               throw new Exception("触发事件 - 参数错误4 ，ID  = " + this.modelID);
            }

            if (StatEnum.find(resStatId) == StatEnum.None) {
               throw new Exception("触发事件 - 参数错误5 ，ID  = " + this.modelID);
            }
            break;
         case 7:
            splits = this.otherParams.split(",");
            if (splits.length < 3) {
               throw new Exception("触发事件 - 参数错误6 ，ID  = " + this.modelID);
            }

            value = Integer.parseInt(splits[0]);
            triggerStatId = Integer.parseInt(splits[1]);
            resStatId = Integer.parseInt(splits[2]);
            if (value < 1) {
               throw new Exception("触发事件 - 参数错误 7，ID  = " + this.modelID);
            }
         }

      }
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getTriggereID() {
      return this.triggereID;
   }

   public void setTriggereID(int triggereID) {
      this.triggereID = triggereID;
   }

   public int getRate() {
      return this.rate;
   }

   public void setRate(int rate) {
      this.rate = rate;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public String getOtherParams() {
      return this.otherParams;
   }

   public void setOtherParams(String otherParams) {
      this.otherParams = otherParams;
   }

   public boolean isMetux() {
      return this.metux;
   }

   public void setMetux(boolean metux) {
      this.metux = metux;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum;
      if ($SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum != null) {
         return var10000;
      } else {
         int[] var0 = new int[ActionEnum.values().length];

         try {
            var0[ActionEnum.Attack_BossEndBuff.ordinal()] = 12;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            var0[ActionEnum.Attack_EndBuff.ordinal()] = 8;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            var0[ActionEnum.Attack_Recover_Hp.ordinal()] = 5;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            var0[ActionEnum.Attack_Target_DeBuff.ordinal()] = 6;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            var0[ActionEnum.Attack_Traget_ReduceHP.ordinal()] = 7;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            var0[ActionEnum.BeAttack_Fanzhen.ordinal()] = 13;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            var0[ActionEnum.Kill_Monster_Recover_AG.ordinal()] = 4;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[ActionEnum.Kill_Monster_Recover_HP.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[ActionEnum.Kill_Monster_Recover_Mp.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[ActionEnum.Move_EndBuff.ordinal()] = 9;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[ActionEnum.None.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[ActionEnum.PK_EndBuff.ordinal()] = 11;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[ActionEnum.UseSkill_EndBuff.ordinal()] = 10;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum = var0;
         return var0;
      }
   }
}
