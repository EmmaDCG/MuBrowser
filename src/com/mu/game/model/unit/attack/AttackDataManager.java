package com.mu.game.model.unit.attack;

import com.mu.game.model.properties.CreatureNaturalStats;
import com.mu.game.model.properties.NumConversion;
import com.mu.game.model.properties.newPotentail.PotentialData;
import com.mu.game.model.unit.player.hang.HangConfig;
import com.mu.game.model.unit.player.hang.SaleCondition;
import com.mu.game.model.unit.player.pkMode.EvilEnum;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.game.model.unit.talent.TalentModel;
import com.mu.utils.Tools;
import java.io.InputStream;
import jxl.Sheet;
import jxl.Workbook;

public class AttackDataManager {
   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      NumConversion.init(sheet);
      Sheet constantSheet = wb.getSheet(2);
      initAttackConstant(constantSheet);
      TalentModel.init(wb.getSheet(3));
      PkEnum.init(wb.getSheet(4));
      EvilEnum.init(wb.getSheet(5));
      CreatureNaturalStats.initPlayerNaturalStats(wb.getSheet(6));
      HangConfig.init(wb.getSheet(7));
      PotentialData.initOrder(wb.getSheet(8));
      PotentialData.init(wb.getSheet(9));
      SaleCondition.initStarLevel(wb.getSheet(10));
      SaleCondition.initZhuijiaLevel(wb.getSheet(11));
   }

   private static void initAttackConstant(Sheet sheet) throws Exception {
      AttackConstant.Coeff_Basic_Hit = Tools.getCellFloatValue(sheet.getCell("B1")) / 100000.0F;
      AttackConstant.Coeff_Global = Tools.getCellFloatValue(sheet.getCell("B2")) / 100000.0F;
      AttackConstant.Coeff_Hit_Correction = Tools.getCellFloatValue(sheet.getCell("B3")) / 100000.0F;
      AttackConstant.Coeff_Dodge_Correction = Tools.getCellFloatValue(sheet.getCell("B4")) / 100000.0F;
      AttackConstant.OneHand_Attack = Tools.getCellFloatValue(sheet.getCell("B6")) / 100000.0F;
      AttackConstant.BothHand_Attack = Tools.getCellFloatValue(sheet.getCell("B7")) / 100000.0F;
      AttackConstant.MainHand_Attack = Tools.getCellFloatValue(sheet.getCell("B8")) / 100000.0F;
      AttackConstant.SecondaryHand_Attack = Tools.getCellFloatValue(sheet.getCell("B9")) / 100000.0F;
   }
}
