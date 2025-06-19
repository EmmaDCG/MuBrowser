package com.mu.game.model.guide;

import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.unit.action.Action;
import com.mu.game.model.unit.action.ActionFactory;
import com.mu.game.model.unit.action.DelayAction;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.material.ChangeMaterialClickStatus;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import jxl.Sheet;
import jxl.Workbook;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class TaskActionManager {
   private static ConcurrentHashMap completeActionMap = new ConcurrentHashMap(8, 0.75F, 2);
   private static ConcurrentHashMap acceptActionMap = new ConcurrentHashMap(8, 0.75F, 2);
   private static ConcurrentHashMap submitActionMap = new ConcurrentHashMap(8, 0.75F, 2);
   private static HashMap notAutoTaskMap = new HashMap();

   public static void initTaskAction(InputStream in) throws Exception {
      SAXBuilder sb = new SAXBuilder();
      Document doc = sb.build(in);
      Element root = doc.getRootElement();
      List completeList = root.getChild("complete").getChildren("task");
      Iterator var6 = completeList.iterator();

      while(var6.hasNext()) {
         Element element = (Element)var6.next();
         int taskID = element.getAttribute("id").getIntValue();
         Action action = ActionFactory.createAction(taskID, element.getChild("action"));
         DelayAction ta = new DelayAction(action);
         ArrayList list = (ArrayList)completeActionMap.get(taskID);
         if (list == null) {
            list = new ArrayList();
            completeActionMap.put(taskID, list);
         }

         list.add(ta);
         Attribute delayAttr = element.getAttribute("delay");
         if (delayAttr != null) {
            ta.setDelayTime(delayAttr.getLongValue());
         }
      }

      List acceptleteList = root.getChild("accept").getChildren("task");
      Iterator var17 = acceptleteList.iterator();

      while(var17.hasNext()) {
         Element element = (Element)var17.next();
         int taskID = element.getAttribute("id").getIntValue();
         Action action = ActionFactory.createAction(taskID, element.getChild("action"));
         DelayAction ta = new DelayAction(action);
         ArrayList list = (ArrayList)acceptActionMap.get(taskID);
         if (list == null) {
            list = new ArrayList();
            acceptActionMap.put(taskID, list);
         }

         list.add(ta);
         Attribute delayAttr = element.getAttribute("delay");
         if (delayAttr != null) {
            ta.setDelayTime(delayAttr.getLongValue());
         }
      }

      List submitList = root.getChild("submit").getChildren("task");
      Iterator var20 = submitList.iterator();

      while(var20.hasNext()) {
         Element element = (Element)var20.next();
         int taskID = element.getAttribute("id").getIntValue();
         Action action = ActionFactory.createAction(taskID, element.getChild("action"));
         DelayAction ta = new DelayAction(action);
         ArrayList list = (ArrayList)submitActionMap.get(taskID);
         if (list == null) {
            list = new ArrayList();
            submitActionMap.put(taskID, list);
         }

         list.add(ta);
         Attribute delayAttr = element.getAttribute("delay");
         if (delayAttr != null) {
            ta.setDelayTime(delayAttr.getLongValue());
         }
      }

   }

   public static void initNotAutoTask(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         notAutoTaskMap.put(Tools.getCellIntValue(sheet.getCell("A" + i)), true);
      }

   }

   public static boolean dontAuto(int taskId) {
      return notAutoTaskMap.containsKey(taskId);
   }

   public static void doAcceptAction(Player player, int taskID) {
      ArrayList list = (ArrayList)acceptActionMap.get(taskID);
      if (list != null) {
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            DelayAction ta = (DelayAction)var4.next();
            ta.doAction(player);
         }
      }

      changeMaterialClickStatus(player, taskID, true);
   }

   public static void doCompleteAction(Player player, int taskID) {
      ArrayList list = (ArrayList)completeActionMap.get(taskID);
      if (list != null) {
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            DelayAction ta = (DelayAction)var4.next();
            ta.doAction(player);
         }
      }

      changeMaterialClickStatus(player, taskID, false);
   }

   public static void doRateRecude(Player player, int taskID) {
      changeMaterialClickStatus(player, taskID, true);
   }

   public static void changeMaterialClickStatus(Player player, int taskID, boolean canClick) {
      Task task = (Task)player.getTaskManager().getCurrentTaskMap().get(taskID);
      if (task != null) {
         if (task.getData().getCjId() > 0) {
            ArrayList list = player.getMap().getAroundMaterialByTemplateId(player.getPosition(), task.getData().getCjId());
            ChangeMaterialClickStatus cs = new ChangeMaterialClickStatus(list, canClick);
            player.writePacket(cs);
            cs.destroy();
            cs = null;
            list.clear();
            list = null;
         }

      }
   }

   public static void doSubmitAction(Player player, int taskID) {
      ArrayList list = (ArrayList)submitActionMap.get(taskID);
      if (list != null) {
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            DelayAction ta = (DelayAction)var4.next();
            ta.doAction(player);
         }
      }

      FunctionOpenManager.checkTask(player, taskID);
      if (player.isNeddAutoTask()) {
         Task task = player.getTaskManager().getCurZJTask();
         if (task == null) {
            return;
         }

         if (task.getData().getClazzIndex() >= TaskConfigManager.TASK_TRACE_ZJ_INDEX) {
            player.setNeddAutoTask(false);
         }
      }

   }
}
