package com.mu.game.model.dialog;

import com.mu.game.model.dialog.options.DialogOptionTask;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.NpcInfo;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.TaskDialog;
import com.mu.game.model.task.target.TaskTarget;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DialogConfigManager {
   private static final HashMap optionMap = new HashMap();
   private static final HashMap dialogMap = new HashMap();

   public static void initConfigs(InputStream in) throws BiffException, IOException {
      Workbook wb = null;

      try {
         wb = Workbook.getWorkbook(in);
         Sheet[] sheets = wb.getSheets();
         initDialogOptionConfig(sheets[2]);
         initDialogConfig(sheets[1]);
      } finally {
         if (wb != null) {
            wb.close();
         }

      }

   }

   public static Dialog getNpcDialog(long npcId) {
      NpcInfo info = MapConfig.getNpcInfo(npcId);
      return info == null ? null : (Dialog)dialogMap.get(info.getDialogId());
   }

   private static void initDialogOptionConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String value1 = sheet.getCell("A" + i).getContents().trim();
         String value2 = sheet.getCell("B" + i).getContents().trim();
         String value3 = sheet.getCell("C" + i).getContents().trim();
         String value4 = sheet.getCell("D" + i).getContents().trim();
         String value5 = sheet.getCell("E" + i).getContents().trim();
         String value6 = sheet.getCell("F" + i).getContents().trim();
         DialogOptionType type = DialogOptionType.valueOf(Integer.parseInt(value4));
         DialogOption option = type.newInstance();
         option.setId(Integer.parseInt(value1));
         option.setName(value2);
         option.setIcon(Integer.parseInt(value3));
         option.setType(type);
         option.setValue(Integer.parseInt(value5));
         option.setClose(value6.equals("1"));
         optionMap.put(option.getId(), option);
      }

   }

   private static void initDialogConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String value1 = sheet.getCell("A" + i).getContents().trim();
         String value2 = sheet.getCell("B" + i).getContents().trim();
         String value3 = sheet.getCell("C" + i).getContents().trim();
         String value4 = sheet.getCell("D" + i).getContents().trim();
         String value5 = sheet.getCell("E" + i).getContents().trim();
         Dialog dialog = new Dialog(Long.parseLong(value1));
         dialog.setTitle(value2);
         dialog.setIcon(Integer.parseInt(value3));
         dialog.setContentStr(value4);
         dialog.setOptionList(value5);
         dialogMap.put(dialog.getId(), dialog);
      }

   }

   public static Dialog getDialog(long id) {
      return (Dialog)dialogMap.get(id);
   }

   public static void addTaskOption(TaskData data, long npcId, TaskDialog taskDialog, DialogOptionSee see, TaskTarget target) {
      if (taskDialog != null) {
         int i;
         for(i = 1; i < Integer.MAX_VALUE && optionMap.get(i) != null; ++i) {
            ;
         }

         DialogOptionTask dot = new DialogOptionTask();
         dot.setId(i);
         dot.setName(taskDialog.getName(data));
         dot.setIcon(see.getIcon());
         dot.setType(DialogOptionType.DOT_TASK);
         dot.setValue(data.getId());
         dot.setClose(true);
         dot.setSee(see);
         dot.setData(data);
         dot.setDialog(taskDialog);
         dot.setTarget(target);
         dot.setTaskNpc(npcId);
         Dialog dialog = getNpcDialog(npcId);
         dialog.getOptionList().add(dot);
         optionMap.put(dot.getId(), dot);
      }
   }

   public static DialogOption getOption(int id) {
      return (DialogOption)optionMap.get(id);
   }
}
