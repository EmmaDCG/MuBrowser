package com.mu.io.http.servlet;

import com.mu.config.BroadcastManager;
import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.db.manager.GMDBManager;
import com.mu.db.manager.GlobalDBManager;
import com.mu.game.CenterManager;
import com.mu.game.IDFactory;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemSaveAide;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.mail.SendLevelReqMailTask;
import com.mu.game.model.mail.SendLocalServerMailTask;
import com.mu.game.model.mail.SendMailTask;
import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.clazz.TaskClazz;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.mall.MallConfig;
import com.mu.utils.DFA;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GmServlet extends HttpServlet {
   private static Logger logger = LoggerFactory.getLogger(GmServlet.class);
   private static final long serialVersionUID = 1L;
   public static ScheduledFuture broadcastFuture;

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      this.doGet(request, response);
   }

   public static void writeResult(HttpServletResponse response, Map result) {
      PrintWriter out = null;

      try {
         out = response.getWriter();
         out.print((new JSONSerializer()).deepSerialize(result));
         out.flush();
         out.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         if (out != null) {
            out.close();
         }

      }

   }

   public static void main(String[] args) {
      Map result = new HashMap();
      result.put("success", true);
      result.put("text", "ssss");
      result.put("time", Integer.valueOf(1000));
      Map tableMap = new HashMap();
      tableMap.put("curPage", Integer.valueOf(4));
      tableMap.put("pageLine", Integer.valueOf(4));
      tableMap.put("curLine", Integer.valueOf(3));
      tableMap.put("maxLine", Integer.valueOf(100));
      tableMap.put("title", new String[]{"num1", "num2", "num3"});
      List lineList = new ArrayList();
      lineList.add(new String[]{"aa", "bb", "c\"c"});
      lineList.add(new String[]{"ee", "ff", "bbb"});
      tableMap.put("data", lineList);
      result.put("table", tableMap);
      System.out.println((new JSONSerializer()).deepSerialize(result));
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      long cur = System.currentTimeMillis();
      HashMap resultMap = new HashMap();

      try {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("utf-8");
         response.setContentType("text/html;charset=UTF-8");
         int type = Tools.getInterParameter(request, "type");
         String paramStr = request.getParameter("paramStr");
         logger.info("gm operation type: {}, paramStr: {}", type, paramStr);
         HashMap paramMap = (HashMap)(new JSONDeserializer()).deserialize(paramStr);
         String sqlQuery = (String)paramMap.get("sqlQuery");
         if (sqlQuery != null && !sqlQuery.isEmpty()) {
            this.executeQuerySQL(resultMap, paramMap, type);
         } else {
            ArrayList lineList;
            String keyword;
            String message;
            int count;
            switch(type) {
            case 2:
               if ("since2015".equals(paramMap.get("encryption"))) {
                  CenterManager.dropAllPlayers();
                  count = CenterManager.getOnlinePlayerSize();
                  resultMap.put("text", "踢人成功, 人数= " + count);
                  logger.info("shutdown game server, online player size = {}", count);
               } else {
                  logger.info("shutdown game server, encryption not equals");
                  resultMap.put("err", "许可证不对");
               }
               break;
            case 1001:
               this.forceSubmitTask(resultMap, paramMap);
               break;
            case 1002:
               this.settingRoleZJTask(resultMap, paramMap);
               break;
            case 2000:
               resultMap.put("text", "当前服务器在线人数= " + CenterManager.getOnlinePlayerSize());
               break;
            case 2002:
               this.parse4Table(resultMap, paramMap, type, MapConfig.getMapPlayerSizeList());
               break;
            case 3001:
               this.queryMuBan(resultMap, paramMap);
               break;
            case 3002:
               this.modifyMuBan(resultMap, paramMap);
               break;
            case 3003:
               this.deleteMuBan(resultMap, paramMap);
               break;
            case 3006:
               count = Integer.parseInt((String)paramMap.get("count"));
               int interval = Integer.parseInt((String)paramMap.get("interval"));
               message = (String)paramMap.get("message");
               if (count <= 0) {
                  resultMap.put("err", "请填写发送次数, 并且大于0.");
               } else if (message.trim().equals("")) {
                  resultMap.put("err", "请填写公告内容.");
               } else {
                  if (broadcastFuture != null && !broadcastFuture.isCancelled()) {
                     broadcastFuture.cancel(false);
                  }

                  broadcastFuture = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(new GmServlet.BroadcastRun(count, message), 0L, (long)(interval * 1000));
                  resultMap.put("text", "操作成功!, 公告: '" + (String)paramMap.get("message") + "'");
               }
               break;
            case 3007:
               this.reloadConfig(resultMap, paramMap);
               break;
            case 4000:
               this.sreachPlayer(resultMap, paramMap);
               break;
            case 4002:
               this.sendMailToUser(resultMap, paramMap);
               break;
            case 4003:
               this.sendMailToLevelUser(resultMap, paramMap);
               break;
            case 4004:
               lineList = new ArrayList();
               keyword = (String)paramMap.get("keyword");
               GMDBManager.queryRoleItems(keyword, lineList);
               this.parse4Table(resultMap, paramMap, type, lineList);
               break;
            case 4005:
               lineList = new ArrayList();
               keyword = (String)paramMap.get("keyword");
               message = (String)paramMap.get("time_limit1");
               String time_limit2 = (String)paramMap.get("time_limit2");
               String pageLine = (String)paramMap.get("pageLine");
               GMDBManager.queryItemLog(keyword, message, time_limit2, pageLine, lineList);
               this.parse4Table(resultMap, paramMap, type, lineList);
               break;
            default:
               resultMap.put("err", "Sorry, the game server does not know the operation " + type);
            }
         }

         paramMap.clear();
         paramMap = null;
      } catch (Exception var18) {
         resultMap.put("err", var18.toString());
         var18.printStackTrace();
      } finally {
         resultMap.put("useTime", System.currentTimeMillis() - cur);
         writeResult(response, resultMap);
         resultMap.clear();
         resultMap = null;
      }

   }

   public void sendMailToUser(Map resultMap, HashMap paramMap) throws Exception {
      List player_set = (List)(new JSONDeserializer()).deserialize((String)paramMap.get("player_set"));
      List item_set = (List)(new JSONDeserializer()).deserialize((String)paramMap.get("item_set"));
      String content = (String)paramMap.get("content");
      String title = (String)paramMap.get("title");
      ArrayList itemList = new ArrayList();

      Iterator it;
      HashMap itemData;
      for(it = item_set.iterator(); it.hasNext(); itemData = null) {
         itemData = (HashMap)it.next();
         int modelID = Integer.parseInt((String)itemData.get("id"));
         int zhuijiaLevel = Integer.parseInt((String)itemData.get("zj"));
         boolean lucky = Integer.parseInt((String)itemData.get("xy")) == 1;
         boolean isBind = Integer.parseInt((String)itemData.get("bd")) == 1;
         int starLevel = Integer.parseInt((String)itemData.get("qh"));
         int socket = Integer.parseInt((String)itemData.get("ks"));
         int count = Integer.parseInt((String)itemData.get("sl"));
         String otherStr = (String)itemData.get("sx");
         ItemModel model = ItemModel.getModel(modelID);
         if (lucky) {
            otherStr = EquipStat.getLuckyString() + (otherStr.trim().length() > 0 ? "," : "") + otherStr;
         }

         while(count > 0) {
            int tmpCount = count;
            if (count > model.getMaxStackCount()) {
               tmpCount = model.getMaxStackCount();
            }

            count -= tmpCount;
            ItemSaveAide isa = new ItemSaveAide(IDFactory.getTemporaryID(), modelID, 1, tmpCount, 0, 1, starLevel, socket, isBind, 0, 1, 0, 0, -1L, model.getDurability(), "", otherStr, "", "", zhuijiaLevel);
            Item item = ItemTools.loadItem(isa);
            if (item == null) {
               itemList.clear();
               itemList = null;
               player_set.clear();
               player_set = null;
               item_set.clear();
               item_set = null;
               resultMap.put("err", "道具不存在 ,道具ID = " + modelID);
               return;
            }

            itemList.add(item);
         }

         itemData.clear();
      }

      it = player_set.iterator();

      while(it.hasNext()) {
         Long role_id = Long.parseLong(it.next().toString());
         ArrayList itemList2 = new ArrayList();
         Iterator itt = itemList.iterator();

         while(itt.hasNext()) {
            Item item = (Item)itt.next();
            itemList2.add(item.cloneItem(2));
         }

         Player owner = CenterManager.getPlayerByRoleID(role_id.longValue());
         if (owner != null) {
            SendMailTask.sendMail(owner, owner.getID(), title, content, itemList2);
            itemList2.clear();
            itemList2 = null;
         } else {
            SendLocalServerMailTask.sendMail(role_id.longValue(), title, content, itemList2);
         }
      }

      itemList.clear();
      itemList = null;
      player_set.clear();
      player_set = null;
      item_set.clear();
      item_set = null;
      resultMap.put("text", "添加物品操作完成");
   }

   public void sendMailToLevelUser(Map resultMap, HashMap paramMap) throws Exception {
      int level = Integer.parseInt((String)paramMap.get("level"));
      String content = (String)paramMap.get("content");
      String title = (String)paramMap.get("title");
      List item_set = (List)(new JSONDeserializer()).deserialize((String)paramMap.get("item_set"));
      ArrayList itemList = new ArrayList();

      HashMap itemData;
      for(Iterator itt = item_set.iterator(); itt.hasNext(); itemData = null) {
         itemData = (HashMap)itt.next();
         int modelID = Integer.parseInt((String)itemData.get("id"));
         int zhuijiaLevel = Integer.parseInt((String)itemData.get("zj"));
         boolean lucky = Integer.parseInt((String)itemData.get("xy")) == 1;
         boolean isBind = Integer.parseInt((String)itemData.get("bd")) == 1;
         int starLevel = Integer.parseInt((String)itemData.get("qh"));
         int socket = Integer.parseInt((String)itemData.get("ks"));
         int count = Integer.parseInt((String)itemData.get("sl"));
         String otherStr = (String)itemData.get("sx");
         ItemModel model = ItemModel.getModel(modelID);
         if (lucky) {
            otherStr = EquipStat.getLuckyString() + (otherStr.trim().length() > 0 ? "," : "") + otherStr;
         }

         while(count > 0) {
            int tmpCount = count;
            if (count > model.getMaxStackCount()) {
               tmpCount = model.getMaxStackCount();
            }

            count -= tmpCount;
            ItemSaveAide isa = new ItemSaveAide(IDFactory.getTemporaryID(), modelID, 1, tmpCount, 0, 1, starLevel, socket, isBind, 0, 1, 0, 0, -1L, model.getDurability(), "", otherStr, "", "", zhuijiaLevel);
            Item item = ItemTools.loadItem(isa);
            if (item == null) {
               itemList.clear();
               itemList = null;
               item_set.clear();
               item_set = null;
               resultMap.put("err", "道具不存在 ,道具ID = " + modelID);
               return;
            }

            itemList.add(item);
         }

         itemData.clear();
      }

      SendLevelReqMailTask.sendMail(level, title, content, itemList);
      itemList.clear();
      itemList = null;
      item_set.clear();
      item_set = null;
      resultMap.put("text", "添加物品操作完成");
   }

   public void sreachPlayer(Map resultMap, HashMap paramMap) throws Exception {
      List lineList = new ArrayList();
      GMDBManager.queryRoleInfo((String)paramMap.get("keyword"), lineList);
      resultMap.put("data", lineList);
   }

   public void reloadConfig(Map resultMap, HashMap paramMap) throws Exception {
      int config_type = Integer.parseInt((String)paramMap.get("config_type"));
      Connection conn = null;
      switch(config_type) {
      case 1:
         try {
            conn = Pool.getGlobalConnection();
            MallConfigManager.initConfigs(GlobalDBManager.getSystemScriptData(conn, 56));
         } finally {
            Pool.closeConnection(conn);
         }

         Iterator it = CenterManager.getAllPlayerIterator();

         while(it.hasNext()) {
            Player player = (Player)it.next();
            MallConfig.sendMsgMallConfig(player);
         }

         resultMap.put("text", "商城配置热加载, 操作完成!");
         break;
      case 2:
         try {
            conn = Pool.getGlobalConnection();
            DFA.initial(GlobalDBManager.getSystemScriptData(conn, 2));
         } finally {
            Pool.closeConnection(conn);
         }

         resultMap.put("text", "关键字过滤, 操作完成!");
         break;
      case 3:
         ActivityManager.reloadAllActivity();
         resultMap.put("text", "活动热加载, 操作完成!");
         break;
      default:
         resultMap.put("text", "操作完成");
      }

   }

   public void forceSubmitTask(Map resultMap, HashMap paramMap) {
      String role_name = (String)paramMap.get("role_name");
      int task_id = Integer.parseInt((String)paramMap.get("task_id"));
      TaskData data = TaskConfigManager.getData(task_id);
      if (data == null) {
         resultMap.put("err", String.format("任务数据[%d]不存在", task_id));
      } else {
         Map tableMap = new HashMap();
         resultMap.put("table", tableMap);
         tableMap.put("oid", Integer.valueOf(1001));
         tableMap.put("title", new String[]{"玩家名", "任务ID", "任务名", "操作结果"});
         List lineList = new ArrayList();
         tableMap.put("data", lineList);
         Player player = CenterManager.getPlayerByRoleName(role_name);
         String result = "";
         if (player == null) {
            result = "操作失败, 玩家不在线";
         } else {
            Task task = (Task)player.getTaskManager().getCurrentTaskMap().get(data.getId());
            if (task == null) {
               result = "操作失败, 玩家没有该任务";
            } else {
               task.forceComplete();
               player.getTaskManager().submit(task_id, true);
               result = "操作成功!";
            }
         }

         lineList.add(new String[]{role_name, String.valueOf(task_id), data.getName(), result});
      }
   }

   public void settingRoleZJTask(Map resultMap, HashMap paramMap) throws Exception {
      String role_name = (String)paramMap.get("role_name");
      int task_id = Integer.parseInt((String)paramMap.get("task_id"));
      TaskData data = TaskConfigManager.getData(task_id);
      if (data == null) {
         resultMap.put("err", String.format("任务数据[%d]不存在", task_id));
      } else if (!data.is(TaskClazz.ZJ)) {
         resultMap.put("err", String.format("任务[%d]不是章节任务", task_id));
      } else {
         Player player = CenterManager.getPlayerByRoleName(role_name);
         if (player != null) {
            CenterManager.gameServerActivieOffChannel(player.getChannel());
         }

         int count = GMDBManager.settingRoleZJTask(role_name, task_id);
         resultMap.put("text", String.format("操作成功, %d 行数据受影响.", count));
      }
   }

   public void queryMuBan(Map resultMap, HashMap paramMap) {
      Map tableMap = new HashMap();
      resultMap.put("table", tableMap);
      tableMap.put("oid", Integer.valueOf(3001));
      tableMap.put("title", new String[]{"类型", "标识符", "时间戳"});
      List lineList = new ArrayList();
      tableMap.put("data", lineList);
      Iterator it = Global.BAN_USER_MAP.entrySet().iterator();

      Entry entry;
      while(it.hasNext()) {
         entry = (Entry)it.next();
         lineList.add(new String[]{"1", (String)entry.getKey(), (String)entry.getValue()});
      }

      it = Global.BAN_IP_MAP.entrySet().iterator();

      while(it.hasNext()) {
         entry = (Entry)it.next();
         lineList.add(new String[]{"2", (String)entry.getKey(), (String)entry.getValue()});
      }

      tableMap.put("curPage", paramMap.get("curPage") == null ? Integer.valueOf(1) : (Comparable)paramMap.get("curPage"));
      tableMap.put("pageLine", lineList.size());
      tableMap.put("maxLine", lineList.size());
   }

   public void parse4Table(Map resultMap, HashMap paramMap, int id, List lineList) throws Exception {
      Map tableMap = new HashMap();
      resultMap.put("table", tableMap);
      tableMap.put("oid", id);
      tableMap.put("title", paramMap.get("title"));
      tableMap.put("data", lineList);
      tableMap.put("maxLine", lineList.size());
   }

   public void executeQuerySQL(Map resultMap, HashMap paramMap, int id) throws Exception {
      String sql = (String)paramMap.get("sqlQuery");
      boolean isLogDB = paramMap.get("logDB") != null;
      Map tableMap = new HashMap();
      resultMap.put("table", tableMap);
      tableMap.put("oid", id);
      tableMap.put("title", paramMap.get("title"));
      List lineList = new ArrayList();
      tableMap.put("data", lineList);
      int pageLine = paramMap.get("pageLine") == null ? 15 : Integer.parseInt(((String)paramMap.get("pageLine")).trim());
      pageLine = Math.min(1000, pageLine);
      int curPage = paramMap.get("curPage") == null ? 1 : Integer.parseInt(((String)paramMap.get("curPage")).trim());

      String key;
      for(Iterator it = paramMap.keySet().iterator(); it.hasNext(); sql = sql.replaceAll("#" + key + "#", (String)paramMap.get(key))) {
         key = (String)it.next();
      }

      GMDBManager.executeQuerySQL(isLogDB, sql, lineList, (curPage - 1) * pageLine, pageLine);
      tableMap.put("curPage", curPage);
      tableMap.put("pageLine", pageLine);
      tableMap.put("maxLine", GMDBManager.executeQueryCountSQL(isLogDB, sql));
   }

   public void modifyMuBan(Map resultMap, HashMap paramMap) throws Exception {
      int type = Integer.parseInt((String)paramMap.get("type"));
      String mark = (String)paramMap.get("mark");
      int sid = Global.getServerID();
      String timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(System.currentTimeMillis());
      switch(type) {
      case 1:
         Global.BAN_USER_MAP.put(mark, timestamp);
         Player user = CenterManager.getPlayerByUserName(mark, sid);
         if (user != null && user.getChannel() != null) {
            CenterManager.gameServerActivieOffChannel(user.getChannel());
         }
         break;
      case 2:
         Global.BAN_IP_MAP.put(mark, timestamp);
      }

      GMDBManager.insertBan(type, mark, timestamp);
      this.queryMuBan(resultMap, paramMap);
   }

   public void deleteMuBan(Map resultMap, HashMap paramMap) throws Exception {
      int type = Integer.parseInt((String)paramMap.get("type"));
      String mark = (String)paramMap.get("mark");
      switch(type) {
      case 1:
         Global.BAN_USER_MAP.remove(mark);
         break;
      case 2:
         Global.BAN_IP_MAP.remove(mark);
      }

      GMDBManager.deleteBan(type, mark);
      this.queryMuBan(resultMap, paramMap);
   }

   class BroadcastRun implements Runnable {
      private int countDown = 0;
      private String message;

      public BroadcastRun(int count, String message) {
         this.countDown = count;
         this.message = message;
      }

      public void run() {
         if (this.countDown > 0) {
            --this.countDown;
            BroadcastManager.broadcastGm(this.message);
            if (this.countDown == 0 && GmServlet.broadcastFuture != null && !GmServlet.broadcastFuture.isCancelled()) {
               GmServlet.broadcastFuture.cancel(false);
            }

         }
      }
   }
}
