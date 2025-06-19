package com.mu.game.model.unit.player.extarget;

import com.mu.executor.Executor;
import com.mu.game.model.item.GetItemWayManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.extarget.ExTargetGetWay;
import com.mu.io.game.packet.imp.extarget.ExTargetReceive;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class ExtargetManager {
   private static HashMap targetMap = new HashMap();
   private static HashMap collectMap = new HashMap();
   private Player owner;
   private HashMap collectedMap = new HashMap();
   private HashMap receiveMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      initGeneral(wb.getSheet(1));
      Iterator it = targetMap.values().iterator();

      while(it.hasNext()) {
         ExTarget et = (ExTarget)it.next();
         initTarget(et, wb.getSheet(et.getPageId()));
      }

   }

   private static void initGeneral(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int page = Tools.getCellIntValue(sheet.getCell("C" + i));
         int title = Tools.getCellIntValue(sheet.getCell("D" + i));
         ItemDataUnit unit = Tools.parseItemDataUnit(Tools.getCellValue(sheet.getCell("E" + i)));
         ExTarget et = new ExTarget(id);
         et.setName(name);
         et.setPageId(page);
         et.setRewardUnit(unit);
         et.setTitleId(title);
         targetMap.put(id, et);
      }

   }

   private static void initTarget(ExTarget target, Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int pro = Tools.getCellIntValue(sheet.getCell("A" + i));
         int index = Tools.getCellIntValue(sheet.getCell("B" + i));
         String[] iStrs = Tools.getCellValue(sheet.getCell("C" + i)).split(",");
         ItemDataUnit du = new ItemDataUnit(Integer.parseInt(iStrs[0]), 1);
         du.setStatRuleID(Integer.parseInt(iStrs[1]));
         du.setHide(true);
         du.setBind(false);
         Item showItem = ItemTools.createItem(2, du);
         int model = Tools.getCellIntValue(sheet.getCell("D" + i));
         String text = Tools.getCellValue(sheet.getCell("E" + i));
         String linkText = Tools.getCellValue(sheet.getCell("F" + i));
         int zoom = Tools.getCellIntValue(sheet.getCell("G" + i));
         String tips = Tools.getCellValue(sheet.getCell("H" + i));
         ExTargetElement tl = new ExTargetElement();
         tl.setIndex(index);
         tl.setItem(showItem);
         tl.setZoom(zoom);
         tl.setTips(tips);
         ExTargetGetWay ew = new ExTargetGetWay();
         ew.writeUTF(text);
         String[] tmp = linkText.split("#");
         ew.writeByte(tmp.length);

         for(int j = 0; j < tmp.length; ++j) {
            String[] linkStrs = tmp[j].split(";");
            int type = Integer.parseInt(linkStrs[0]);
            String value = linkStrs[1];
            GetItemWayManager.writeLinkDetail(ew, type, j, value);
         }

         tl.setPacket(ew);
         target.addElement(pro, tl);
         target.addItemTarget(pro, model, index);
         HashMap map = (HashMap)collectMap.get(pro);
         if (map == null) {
            map = new HashMap();
            collectMap.put(pro, map);
         }

         map.put(model, new int[]{target.getId(), index});
      }

   }

   public static ExTargetElement getElement(int pro, int id, int index) {
      ExTarget target = (ExTarget)targetMap.get(id);
      return target != null ? target.getElement(pro, index) : null;
   }

   public static void checkCollect(Player player, Item item) {
      HashMap map = (HashMap)collectMap.get(player.getProType());
      if (map != null) {
         int[] in = (int[])map.get(item.getModelID());
         if (in != null) {
            ExtargetManager manager = player.getExtargetManager();
            if (manager != null && manager.getReceiveStatus(in[0]) == 0 && !manager.hasCollected(in[0], in[1])) {
               manager.collectExItem(in[0], in[1]);
            }
         }
      }

   }

   public boolean hasCollected(int id, int index) {
      HashMap tmpMap = (HashMap)this.collectedMap.get(id);
      return tmpMap == null ? false : tmpMap.containsKey(index);
   }

   public void collectExItem(int id, int index) {
      HashMap tmpMap = (HashMap)this.collectedMap.get(id);
      if (tmpMap == null) {
         tmpMap = new HashMap();
         this.collectedMap.put(id, tmpMap);
      }

      tmpMap.put(index, true);
      WriteOnlyPacket packet = Executor.ExtargetCollect.toPacket(this.owner.getID(), id, index);
      this.owner.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public void addReceiveWhenInit(int id) {
      this.receiveMap.put(id, true);
   }

   public void addCollectWhenInit(int id, int index) {
      HashMap map = (HashMap)this.collectedMap.get(id);
      if (map == null) {
         map = new HashMap();
         this.collectedMap.put(id, map);
      }

      map.put(index, true);
   }

   public static HashMap getTargetMap() {
      return targetMap;
   }

   public boolean hasFullCollected(int id) {
      int proType = this.owner.getProType();
      HashMap tmpMap = (HashMap)this.collectedMap.get(id);
      if (tmpMap == null) {
         return false;
      } else {
         ExTarget target = (ExTarget)targetMap.get(id);
         if (target == null) {
            return false;
         } else {
            int size = target.getElementSize(proType);
            return size > 0 && tmpMap.size() >= size;
         }
      }
   }

   public int getShowNumber() {
      int num = 0;
      Iterator it = targetMap.keySet().iterator();

      while(it.hasNext()) {
         int id = ((Integer)it.next()).intValue();
         if (this.getReceiveStatus(id) == 1) {
            ++num;
         }
      }

      return num;
   }

   public int getReceiveStatus(int id) {
      if (this.receiveMap.containsKey(id)) {
         return 2;
      } else {
         return this.hasFullCollected(id) ? 1 : 0;
      }
   }

   public synchronized void receive(int id) {
      if (this.getReceiveStatus(id) == 1) {
         ExTarget et = (ExTarget)targetMap.get(id);
         if (et != null) {
            ItemDataUnit unit = et.getRewardUnit();
            OperationResult result = this.getOwner().getItemManager().addItem(unit);
            if (result.getResult() != 1) {
               ExTargetReceive.writeReceiveResult(this.getOwner(), id, false, -1L, 0);
               SystemMessage.writeMessage(this.getOwner(), result.getResult());
            } else {
               ExTargetReceive.writeReceiveResult(this.getOwner(), id, true, result.getItemID(), 2);
               this.receiveMap.put(id, true);
               WriteOnlyPacket packet = Executor.ExtargetReceive.toPacket(this.owner.getID(), id);
               this.owner.writePacket(packet);
               packet.destroy();
               packet = null;
               UpdateMenu.update(this.owner, 24);
            }
         }
      }
   }

   public boolean isAllReceived() {
      Iterator it = targetMap.keySet().iterator();

      while(it.hasNext()) {
         int id = ((Integer)it.next()).intValue();
         if (this.getReceiveStatus(id) != 2) {
            return false;
         }
      }

      return true;
   }

   public ExtargetManager(Player owner) {
      this.owner = owner;
   }

   public Player getOwner() {
      return this.owner;
   }

   public void destroy() {
      this.owner = null;
   }
}
