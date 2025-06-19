package com.mu.utils;

import com.mu.game.model.equip.external.EquipmentEffect;
import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.Creature;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import jxl.Cell;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.netty.channel.Channel;

public class Tools {
   public static double printMemory() {
      Runtime rt = Runtime.getRuntime();
      long total = rt.totalMemory();
      long free = rt.freeMemory();
      double use = (double)(total - free) / 1024.0D / 1024.0D;
      System.out.printf("use memory: %.2fM", use).println();
      return use;
   }

   public static int getInterParameter(HttpServletRequest request, String name) {
      String value = request.getParameter(name);
      return value != null && isNumber(value) ? Integer.parseInt(value) : -1;
   }

   public static boolean isChineseChar(String str) {
      boolean temp = false;
      Pattern p = Pattern.compile("[一-龥]");
      Matcher m = p.matcher(str);
      if (m.find()) {
         temp = true;
      }

      return temp;
   }

   public static int getRndFace() {
      int r = Rnd.get(2);
      return r == 0 ? Rnd.get(-99, -1) : Rnd.get(1, 99);
   }

   public static boolean isNumber(String str) {
      if (str == null) {
         return false;
      } else {
         String format = "^[0-9]+$";
         Pattern p = Pattern.compile(format, 2);
         Matcher m = p.matcher(str);
         return m.matches();
      }
   }

   public static ConcurrentHashMap newConcurrentHashMap() {
      return newConcurrentHashMap(16);
   }

   public static ConcurrentHashMap newConcurrentHashMap2() {
      return newConcurrentHashMap(2);
   }

   public static ConcurrentHashMap newConcurrentHashMap4() {
      return newConcurrentHashMap(4);
   }

   public static ConcurrentHashMap newConcurrentHashMap(int concurrencyLevel) {
      return new ConcurrentHashMap(16, 0.75F, concurrencyLevel);
   }

   public static String getUUID() {
      return UUID.randomUUID().toString();
   }

   public static String[] getUrlGetContent(String url) {
      String[] result = new String[2];

      try {
         HttpClient client = new DefaultHttpClient();
         client.getParams().setParameter("http.connection.timeout", Integer.valueOf(30000));
         client.getParams().setParameter("http.socket.timeout", Integer.valueOf(30000));
         HttpGet request = new HttpGet();
         request.setURI(new URI(url));
         HttpResponse response = client.execute(request);
         int status = response.getStatusLine().getStatusCode();
         if (status == 200) {
            String strResult = EntityUtils.toString(response.getEntity());
            result[0] = "ok";
            result[1] = strResult;
         } else {
            result[0] = "" + status;
            result[1] = "网络请求失败";
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         result[0] = "error";
         result[1] = var7.getMessage();
      }

      return result;
   }

   public static String[] getUrlPostContent(String url, List params) {
      HttpPost httpRequest = new HttpPost(url);
      String[] result = new String[2];

      try {
         httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
         HttpClient client = new DefaultHttpClient();
         client.getParams().setParameter("http.connection.timeout", 500000);
         client.getParams().setParameter("http.socket.timeout", 500000);
         HttpResponse httpResponse = client.execute(httpRequest);
         int status = httpResponse.getStatusLine().getStatusCode();
         if (status == 200) {
            String strResult = EntityUtils.toString(httpResponse.getEntity());
            result[0] = "ok";
            result[1] = strResult;
         } else {
            result[0] = "" + status;
            result[1] = null;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
         result[0] = "error";
         result[1] = var8.getMessage();
      }

      return result;
   }

   public static String getIP(Channel channel) {
      if (channel != null && channel.isConnected()) {
         String ip = channel.getRemoteAddress().toString();
         int begin = ip.indexOf("/");
         int end = ip.indexOf(":");
         if (begin != -1 && end != -1) {
            return ip.substring(begin + 1, end);
         }
      }

      return "";
   }

   public static int getFace(int x1, int y1, int x2, int y2) {
      int vectorX = x2 - x1;
      int vectorY = y2 - y1;
      double r = Math.atan2((double)(-vectorY), (double)vectorX) / 3.141592653589793D;
      r = (1.5D - r) % 2.0D;
      r /= 0.125D;
      int f = (int)(Math.ceil(r) / 2.0D) % 8;
      return f;
   }

   public static boolean isGuilder(String uid) {
      return false;
   }

   public static boolean isInterval(Creature ac1, Creature ac2, double maxInterval, double minInterval) {
      return ac1 != null && ac2 != null && ac1.getMapID() == ac2.getMapID() ? isInterval(ac1.getPosition(), ac2.getPosition(), maxInterval, minInterval) : false;
   }

   private static boolean isInterval(Point point1, Point point2, double maxInterval, double minInterval) {
      if (point1 != null && point2 != null) {
         double distance = (double)MathUtil.getDistance(point1, point2);
         return minInterval <= distance && distance <= maxInterval;
      } else {
         return false;
      }
   }

   public static boolean isIntervalLittle(Point point1, Point point2, double interval) {
      if (point1 != null && point2 != null) {
         double distance = (double)MathUtil.getDistance(point1, point2);
         return distance <= interval;
      } else {
         return false;
      }
   }

   public static long getCellLongValue(Cell cell) {
      return Long.parseLong(cell.getContents().trim());
   }

   public static int getCellIntValue(Cell cell) {
      return Integer.parseInt(cell.getContents().trim());
   }

   public static float getCellFloatValue(Cell cell) {
      return Float.parseFloat(cell.getContents().trim());
   }

   public static String getCellValue(Cell cell) {
      if (cell == null) {
         return null;
      } else {
         String tmp = cell.getContents();
         if (tmp != null) {
            tmp = tmp.trim();
         }

         return tmp;
      }
   }

   public static int getMasterLevel(int level) {
      return level > 400 ? level - 400 : level;
   }

   public static boolean isMaster(int level) {
      return level > 400;
   }

   public static ArrayList parseItemList(String str) throws Exception {
      ArrayList itemList = new ArrayList();
      if (str != null && str.trim().length() > 0) {
         String[] tmps = str.trim().split(";");

         for(int i = 0; i < tmps.length; ++i) {
            String[] itemS = tmps[i].split(",");
            if (itemS.length != 5) {
               throw new Exception("活动奖励错误...\t" + str);
            }

            int modelId = Integer.parseInt(itemS[0]);
            int num = Integer.parseInt(itemS[1]);
            boolean bind = Integer.parseInt(itemS[2]) == 1;
            int aId = Integer.parseInt(itemS[3]);
            long time = Long.parseLong(itemS[4]);
            ItemDataUnit unit = new ItemDataUnit(modelId, num, bind);
            unit.setExpireTime(time);
            unit.setStatRuleID(aId);
            Item item = ItemTools.createItem(2, unit);
            ItemTools.setSystemExpire(item, time);
            itemList.add(item);
         }
      }

      return itemList;
   }

   public static ArrayList parseItemDataUnitList(String str) {
      ArrayList unitList = new ArrayList();
      if (str != null && str.trim().length() > 0) {
         String[] tmps = str.trim().split(";");

         for(int i = 0; i < tmps.length; ++i) {
            unitList.add(parseItemDataUnit(tmps[i]));
         }
      }

      return unitList;
   }

   public static ItemDataUnit parseItemDataUnit(String str) {
      String[] itemS = str.split(",");
      int modelId = Integer.parseInt(itemS[0]);
      int num = Integer.parseInt(itemS[1]);
      boolean bind = Integer.parseInt(itemS[2]) == 1;
      int aId = Integer.parseInt(itemS[3]);
      long time = Long.parseLong(itemS[4]);
      ItemDataUnit unit = new ItemDataUnit(modelId, num, bind);
      unit.setExpireTime(time);
      unit.setStatRuleID(aId);
      return unit;
   }

   public static ExternalEntry parseExternalEntry(String s) {
      String[] equipExternal = s.split(",");
      int type = Integer.parseInt(equipExternal[0]);
      int modelId = Integer.parseInt(equipExternal[1]);
      ItemModel model = ItemModel.getModel(modelId);
      int externalModelId = model.getExternalModelMenRight(1);
      if (type == 6) {
         externalModelId = model.getExternalModelMenLeft(1);
      }

      int effect = EquipmentEffect.getExternalEffectID(modelId, Integer.parseInt(equipExternal[2]));
      return new ExternalEntry(type, externalModelId, effect);
   }
}
