package com.mu.game.model.fp;

import com.mu.config.MessageText;
import com.mu.executor.Executor;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.guide.FunctionPreviewUpdate;
import com.mu.io.game.packet.imp.player.tips.SystemFunctionTip;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class FunctionPreviewManager {
   private static HashMap previewMap = new HashMap();
   private static FunctionPreview firstPrivew = null;
   private static FunctionPreview lastPrivew = null;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();
      ArrayList list = new ArrayList();

      int i;
      for(i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int openLevel = Tools.getCellIntValue(sheet.getCell("C" + i));
         int receiveLevel = Tools.getCellIntValue(sheet.getCell("D" + i));
         String headerStr = Tools.getCellValue(sheet.getCell("E" + i));
         String des1 = Tools.getCellValue(sheet.getCell("F" + i));
         String des2 = Tools.getCellValue(sheet.getCell("G" + i));
         String itemStr = Tools.getCellValue(sheet.getCell("H" + i));
         int sort = Tools.getCellIntValue(sheet.getCell("I" + i));
         FunctionPreview preview = new FunctionPreview(id);
         preview.setName(name);
         preview.setOpenLevel(openLevel);
         preview.setReceiveLevel(receiveLevel);
         preview.setSort(sort);
         preview.setDes1(des1);
         preview.setDes2(des2);
         String[] tmpHeader = headerStr.split(";");
         if (tmpHeader.length == 1) {
            preview.setHeader(Integer.parseInt(tmpHeader[0]));
         } else {
            for(int j = 0; j < tmpHeader.length; ++j) {
               String[] proModel = tmpHeader[j].split(":");
               preview.setHeader(Integer.parseInt(proModel[0]), Integer.parseInt(proModel[1]));
            }
         }

         String[] tmpItem = itemStr.split(";");

         for(int j = 0; j < tmpItem.length; ++j) {
            String[] proItem = tmpItem[j].split(":");
            if (proItem.length == 1) {
               String[] proItemStr = proItem[0].split(",");
               if (proItemStr.length == 4) {
                  ItemDataUnit data = new ItemDataUnit(Integer.parseInt(proItemStr[0]), Integer.parseInt(proItemStr[1]), Integer.parseInt(proItemStr[2]) == 1);
                  data.setExpireTime(Long.parseLong(proItemStr[3]));
                  preview.addReward(data);
               }
            } else {
               int pro = Integer.parseInt(proItem[0]);
               String[] proItemStr = proItem[1].split(",");
               if (proItemStr.length == 4) {
                  ItemDataUnit data = new ItemDataUnit(Integer.parseInt(proItemStr[0]), Integer.parseInt(proItemStr[1]), Integer.parseInt(proItemStr[2]) == 1);
                  data.setExpireTime(Long.parseLong(proItemStr[3]));
                  preview.addReward(pro, data);
               }
            }
         }

         list.add(preview);
         previewMap.put(id, preview);
      }

      Collections.sort(list);
      firstPrivew = (FunctionPreview)list.get(0);
      lastPrivew = (FunctionPreview)list.get(list.size() - 1);

      for(i = 0; i < list.size(); ++i) {
         FunctionPreview cur = (FunctionPreview)list.get(i);
         if (i > 0) {
            cur.setPrePreview((FunctionPreview)list.get(i - 1));
         }

         if (i < list.size() - 1) {
            cur.setNextPreview((FunctionPreview)list.get(i + 1));
         }
      }

      list.clear();
   }

   public static FunctionPreview getFirstPrivew() {
      return firstPrivew;
   }

   public static FunctionPreview getLastPrivew() {
      return lastPrivew;
   }

   public static FunctionPreview getFunctionPreview(int pre) {
      return (FunctionPreview)previewMap.get(pre);
   }

   public static FunctionPreview getCurrentPreview(Player player) {
      int finishid = player.getFinishPreview();
      FunctionPreview finishPreview = getFunctionPreview(finishid);
      FunctionPreview curPreview = null;
      if (finishPreview == null) {
         curPreview = getFirstPrivew();
      } else {
         curPreview = finishPreview.getNextPreview();
      }

      return curPreview != null && player.getLevel() < curPreview.getOpenLevel() ? null : curPreview;
   }

   public static boolean receive(Player player) {
      FunctionPreview curPreview = getCurrentPreview(player);
      if (curPreview != null) {
         if (curPreview.getReceiveLevel() > player.getLevel()) {
            SystemMessage.writeMessage(player, MessageText.getText(16101).replace("%s%", String.valueOf(curPreview.getReceiveLevel())), 16101);
            return false;
         } else {
            ArrayList list = curPreview.getRewardList(player);
            boolean success = true;
            OperationResult or;
            if (list.size() > 0) {
               or = player.getItemManager().addItem((List)list);
               int result = or.getResult();
               if (result != 1) {
                  SystemMessage.writeMessage(player, result);
                  success = false;
               } else {
                  Item item = player.getBackpack().getItemByID(or.getItemID());
                  if (item != null) {
                     SystemFunctionTip.sendToClient(player, 7, or.getItemID());
                  }
               }
            }

            if (success) {
               player.setFinishPreview(curPreview.getId());
               WriteOnlyPacket packet = Executor.SavePreview.toPacket(player.getID(), player.getFinishPreview());
               player.writePacket(packet);
               packet.destroy();
               or = null;
               FunctionPreviewUpdate.pushPreview(player, true);
            }

            return success;
         }
      } else {
         return false;
      }
   }
}
