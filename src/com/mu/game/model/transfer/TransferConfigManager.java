package com.mu.game.model.transfer;

import com.mu.game.model.task.TaskData;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferConfigManager {
   private static Logger logger = LoggerFactory.getLogger(TransferConfigManager.class);
   private static final HashMap transferMap = new HashMap();

   public static void initConfigs(InputStream in) throws BiffException, IOException {
      Workbook wb = null;

      try {
         wb = Workbook.getWorkbook(in);
         Sheet[] sheets = wb.getSheets();
         initTransferConfig(sheets[2]);
         initTransferSkillConfig(sheets[1]);

         for(int i = 3; i < sheets.length; ++i) {
            initTransferStepConfig(sheets[i]);
         }

         Iterator iterator = transferMap.values().iterator();

         while(iterator.hasNext()) {
            HashMap tsMap = (HashMap)iterator.next();
            Iterator it2 = tsMap.values().iterator();

            while(it2.hasNext()) {
               Transfer ts = (Transfer)it2.next();
               TaskData last = null;
               Iterator it3 = ts.getStepList().iterator();

               while(it3.hasNext()) {
                  TransferStep step = (TransferStep)it3.next();

                  TaskData data;
                  for(Iterator it4 = step.getTaskList().iterator(); it4.hasNext(); last = data) {
                     data = (TaskData)it4.next();
                     data.setClazzIndex(ts.getTaskList().size());
                     ts.getTaskList().add(data);
                     if (last != null) {
                        if (last.getClazzNext() != null) {
                           logger.error("TaskStep config error, task{} next is not null, task{} can not set", last.getId(), data.getId());
                        }

                        last.setClazzNext(data);
                     } else {
                        ts.setTaskHead(data);
                     }
                  }
               }
            }
         }

      } finally {
         if (wb != null) {
            wb.close();
         }

      }
   }

   private static void initTransferConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         String G = sheet.getCell("G" + i).getContents().trim();
         Transfer transfer = new Transfer();
         transfer.setId(Integer.parseInt(A));
         transfer.setJob1(Integer.parseInt(B));
         transfer.setJob2(Integer.parseInt(C));
         transfer.setName(D);
         transfer.setImage(Integer.parseInt(E));
         transfer.setLevel(Integer.parseInt(F));
         transfer.setAKeyCompleteStr(G);
         HashMap map = (HashMap)transferMap.get(transfer.getJob1());
         if (map == null) {
            map = new HashMap();
            transferMap.put(transfer.getJob1(), map);
         }

         map.put(transfer.getId(), transfer);
      }

   }

   private static void initTransferSkillConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         TransferSkill ts = new TransferSkill();
         ts.setTransfer(Integer.parseInt(A));
         ts.setJob(Integer.parseInt(B));
         ts.setName(C);
         ts.setIcon(Integer.parseInt(D));
         ts.setTip(E);
         Transfer transfer = getTransfer(ts.getJob(), ts.getTransfer());
         if (transfer == null) {
            logger.error("TransferSkill config error, not found transfer {} job {}", ts.getTransfer(), ts.getJob());
         } else {
            transfer.getSkillList().add(ts);
         }
      }

   }

   private static void initTransferStepConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         String G = sheet.getCell("G" + i).getContents().trim();
         TransferStep ts = new TransferStep();
         ts.setTransfer(Integer.parseInt(A));
         ts.setJob(Integer.parseInt(B));
         ts.setStep(Integer.parseInt(C));
         ts.setDescription(D);
         ts.setAttributeDot(Integer.parseInt(E));
         ts.setTaskStr(F);
         ts.setAttributeListStr(G);
         Transfer transfer = getTransfer(ts.getJob(), ts.getTransfer());
         if (transfer == null) {
            logger.error("TransferSkill config error, not found transfer {} job {}", ts.getTransfer(), ts.getJob());
         } else {
            transfer.getStepList().add(ts);
         }
      }

   }

   public static Transfer getTransfer(int job, int transfer) {
      HashMap map = (HashMap)transferMap.get(job);
      return map == null ? null : (Transfer)map.get(transfer);
   }

   public static Transfer getWillTransfer(int job, int transfer) {
      HashMap map = (HashMap)transferMap.get(job);
      return map == null ? null : (Transfer)map.get(transfer + 1);
   }
}
