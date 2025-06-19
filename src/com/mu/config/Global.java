package com.mu.config;

import com.mu.db.manager.GlobalDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.plat.BaiDuParser;
import com.mu.game.plat.CommonParser;
import com.mu.game.plat.G2Parser;
import com.mu.game.plat.Ku7Parser;
import com.mu.game.plat.LoginParser;
import com.mu.game.plat.QqParser;
import com.mu.game.plat.SmallParser;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import com.mu.utils.VarietalBase64;
import flexjson.JSONDeserializer;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.http.message.BasicNameValuePair;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class Global {
   private static boolean isDebug = false;
   private static boolean allowError = true;
   private static int GatewayPort = 4088;
   private static int GamePort = 4089;
   private static int ServerID = 1;
   private static String gameName = "";
   private static String LoginHost = null;
   private static String LoginServletPath = null;
   private static int LoginServletPort = 6868;
   private static boolean IsLine = false;
   private static boolean Open843Server = false;
   private static int ServletPort = 6868;
   private static boolean ServerIsDown = false;
   private static int PlatID = 1;
   private static String localHost = "127.0.0.1";
   private static boolean isInterServiceServer = false;
   private static int combineServerID = -1;
   private static String InterServerHost = null;
   private static int InterServerPort = -1;
   private static Date openServerTiem = new Date();
   private static Date combineServerTime = null;
   private static int ParserID = 1;
   private static boolean isFcm = false;
   private static boolean pushOffline = true;
   private static boolean createRobot = true;
   private static String fcmUrl = "";
   private static int gameID = 0;
   private static String userDB = "muuser";
   private static HashMap keyMap = new HashMap();
   private static HashMap urlMap = new HashMap();
   private static HashMap combineServerMap = new HashMap();
   private static boolean openWhiteList = false;
   private static HashMap whilteMap = new HashMap();
   private static boolean drOpenTime = false;
   public static String appid = "1105007049";
   public static String blueAid = "VIP.APP1105007049.PLATqqgameact";
   public static String blueOpenAid = "VIP.APP1105007049.PLATqqgamemini";
   public static String appkey = "wFfPfy3KS6yfg84Y";
   public static String QQAPIIp = "openapi.tencentyun.com";
   public static String gameId = "10368";
   public static int sxhj = 0;
   private static String serverIp = "127.0.0.1";
   private static HashMap defaultEquipMap = new HashMap();
   private static LoginParser loginParser = null;
   private static boolean checkPwd = false;
   private static boolean canGMCommand = false;
   private static boolean showAllRoles = false;
   private static boolean autoTask = true;
   private static boolean pwLogin = false;
   private static final String GameVerifyKey = "Kuejnj3lXJHnhs87wmnLmnxgsRwsxP";
   private static final String GameServletKey = "JyekxdHmOIwvxEvwxUSjheo85JK201";
   private static final String version = "2014050801";
   public static final ConcurrentHashMap BAN_USER_MAP = Tools.newConcurrentHashMap2();
   public static final ConcurrentHashMap BAN_IP_MAP = Tools.newConcurrentHashMap2();
   public static final ConcurrentHashMap JYSet = Tools.newConcurrentHashMap2();

   public static boolean isBan(String userName, String ipStr) {
      return BAN_USER_MAP.get(userName) != null || BAN_IP_MAP.get(ipStr) != null;
   }

   public static boolean isJY(long roleId) {
      return JYSet.get(roleId) != null;
   }

   public static boolean init() {
      try {
         SAXBuilder sb = new SAXBuilder();
         Document doc = sb.build("configs/game.xml");
         Element root = doc.getRootElement();
         ServerID = root.getAttribute("serverid").getIntValue();
         Attribute Open843Attribute = root.getAttribute("open843");
         if (Open843Attribute != null) {
            Open843Server = Open843Attribute.getBooleanValue();
         }

         Attribute pwLoginAttribute = root.getAttribute("pwlogin");
         if (pwLoginAttribute != null) {
            pwLogin = pwLoginAttribute.getBooleanValue();
         }

         Attribute debugAttribute = root.getAttribute("debug");
         if (debugAttribute != null) {
            isDebug = debugAttribute.getBooleanValue();
         }

         Attribute autoTaskAttribute = root.getAttribute("autoTask");
         if (autoTaskAttribute != null) {
            autoTask = autoTaskAttribute.getBooleanValue();
         }

         Attribute pushOfflineAttribute = root.getAttribute("pushOffline");
         if (pushOfflineAttribute != null) {
            pushOffline = pushOfflineAttribute.getBooleanValue();
         }

         Attribute createRobotAttribute = root.getAttribute("createRobot");
         if (createRobotAttribute != null) {
            createRobot = createRobotAttribute.getBooleanValue();
         }

         Attribute allowErrorAttribute = root.getAttribute("allowError");
         if (allowErrorAttribute != null) {
            allowError = allowErrorAttribute.getBooleanValue();
         }

         Attribute checkPwdAttribute = root.getAttribute("checkPwd");
         if (checkPwdAttribute != null) {
            checkPwd = checkPwdAttribute.getBooleanValue();
         }

         Element fcmElement = root.getChild("fcm");
         if (fcmElement != null) {
            Attribute fcmAttribute = fcmElement.getAttribute("value");
            if (fcmAttribute != null) {
               isFcm = fcmAttribute.getBooleanValue();
               if (isFcm) {
                  fcmUrl = fcmElement.getAttributeValue("url");
               }
            }
         }

         Element gameElement = root.getChild("game");
         GamePort = gameElement.getAttribute("port").getIntValue();
         Attribute lineAttribute = gameElement.getAttribute("line");
         if (lineAttribute != null) {
            IsLine = lineAttribute.getBooleanValue();
         }

         Attribute interAttribute = gameElement.getAttribute("interService");
         if (interAttribute != null) {
            isInterServiceServer = interAttribute.getBooleanValue();
         }

         Attribute gmCommanddAttribute = gameElement.getAttribute("gmcommond");
         if (gmCommanddAttribute != null) {
            canGMCommand = gmCommanddAttribute.getBooleanValue();
         }

         Attribute drOpenTimeAttribute = gameElement.getAttribute("drOpenTime");
         if (drOpenTimeAttribute != null) {
            drOpenTime = drOpenTimeAttribute.getBooleanValue();
         }

         Attribute parserAttribute = gameElement.getAttribute("parser");
         if (parserAttribute != null) {
            ParserID = parserAttribute.getIntValue();
         }

         Attribute allRolesAttribute = gameElement.getAttribute("allrole");
         if (allRolesAttribute != null) {
            showAllRoles = allRolesAttribute.getBooleanValue();
         }

         Element whiteElement = root.getChild("white");
         Element we;
         if (whiteElement != null) {
            Attribute openWhiteAttribute = whiteElement.getAttribute("isOpen");
            if (openWhiteAttribute != null) {
               openWhiteList = openWhiteAttribute.getBooleanValue();
            }

            if (openWhiteList) {
               List whiteElementList = whiteElement.getChildren("user");
               if (whiteElementList != null) {
                  Iterator var23 = whiteElementList.iterator();

                  while(var23.hasNext()) {
                     we = (Element)var23.next();
                     whilteMap.put(we.getValue().trim(), true);
                  }
               }
            }
         }

         Element gatewayElement = root.getChild("gateway");
         GatewayPort = gatewayElement.getAttribute("port").getIntValue();
         Element servletElement = root.getChild("servlet");
         ServletPort = servletElement.getAttribute("port").getIntValue();
         we = root.getChild("login");
         LoginHost = we.getAttributeValue("host");
         LoginServletPort = we.getAttribute("servletport").getIntValue();
         if (LoginHost.startsWith("http://")) {
            LoginServletPath = LoginHost + ":" + LoginServletPort + "/mu";
         } else {
            LoginServletPath = "http://" + LoginHost + ":" + LoginServletPort + "/mu";
         }

         boolean bool = true;
         if (!isDebug()) {
            bool = resetPropertiesFormCenterServer();
            resetServerIp();
         }

         loginParser = initLoginParser();
         return bool;
      } catch (Exception var24) {
         var24.printStackTrace();
         return false;
      }
   }

   public static int getParserID() {
      return ParserID;
   }

   private static void resetServerIp() {
      ArrayList list = new ArrayList();

      try {
         list.add(new BasicNameValuePair("type", "6"));
         list.add(new BasicNameValuePair("sid", String.valueOf(ServerID)));
         String[] result = Tools.getUrlPostContent(LoginServletPath, list);
         if (result[0].equals("ok") && !result[1].equals("-1")) {
            serverIp = result[1].trim();
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      } finally {
         list.clear();
      }

   }

   public static boolean resetPropertiesFormCenterServer() {
      ArrayList list = new ArrayList();

      try {
         list.add(new BasicNameValuePair("type", "1"));
         list.add(new BasicNameValuePair("sid", String.valueOf(ServerID)));
         String[] result = Tools.getUrlPostContent(LoginServletPath, list);
         if (result[0].equals("ok")) {
            String jsonData = new String(VarietalBase64.decode(result[1]));
            JSONDeserializer der = new JSONDeserializer();
            HashMap map = (HashMap)der.deserialize(jsonData);
            ParserID = ((Integer)map.get("parser")).intValue();
            gameID = ((Integer)map.get("gameID")).intValue();
            PlatID = ((Integer)map.get("plat")).intValue();
            userDB = (String)map.get("userDB");
            gameName = (String)map.get("gn");
            combineServerID = ((Integer)map.get("csi")).intValue();
            InterServerHost = (String)map.get("ish");
            InterServerPort = ((Integer)map.get("isp")).intValue();
            localHost = (String)map.get("localhost");
            isFcm = ((Boolean)map.get("fcm")).booleanValue();
            keyMap.clear();
            keyMap = (HashMap)map.get("key");
            urlMap.clear();
            urlMap = (HashMap)map.get("url");
            openServerTiem = Time.getDate((String)map.get("ot"));
            Object combineTime = map.get("ct");
            if (combineTime != null) {
               combineServerTime = Time.getDate((String)combineTime);
            }

            Object combineList = map.get("csl");
            if (combineList != null) {
               String[] tmpList = ((String)combineList).split(",");

               for(int i = 0; i < tmpList.length; ++i) {
                  combineServerMap.put(Integer.parseInt(tmpList[i]), ServerID);
               }
            }

            map.clear();
            return true;
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         list.clear();
      }

      return false;
   }

   public static void setOpenServerTime(Date date) {
      openServerTiem = date;
   }

   public static int getGatewayPort() {
      return GatewayPort;
   }

   public static int getGamePort() {
      return GamePort;
   }

   public static int getServerID() {
      return ServerID;
   }

   public static String getLoginHost() {
      return LoginHost;
   }

   public static int getLoginServletPort() {
      return LoginServletPort;
   }

   public static boolean isLine() {
      return IsLine;
   }

   public static boolean isOpen843Server() {
      return Open843Server;
   }

   public static boolean isDebug() {
      return isDebug;
   }

   public static boolean serverIsDown() {
      return ServerIsDown;
   }

   public static void setServerIsDown(boolean serverIsDown) {
      ServerIsDown = serverIsDown;
   }

   public static int getPlatID() {
      return PlatID;
   }

   public static int getServletPort() {
      return ServletPort;
   }

   public static final boolean isInterServiceServer() {
      return isInterServiceServer;
   }

   public static LoginParser getLoginParser() {
      return loginParser;
   }

   public static String getLoginServletPath() {
      return LoginServletPath;
   }

   public static String getKey(String k) {
      String key = (String)keyMap.get(k);
      if (key == null) {
         key = (String)keyMap.get("login");
      }

      return key;
   }

   public static String getLoginKey() {
      return getKey("login");
   }

   public static String getUrl(String key) {
      String url = (String)urlMap.get(key);
      return url == null ? "" : url;
   }

   public static String getPayUrl(Player player) {
      String url = (String)urlMap.get("pay");
      return url == null ? "" : url.replace("%u%", player.getUserName()).replace("%s%", String.valueOf(player.getUser().getServerID()));
   }

   public static int getGameID() {
      return gameID;
   }

   public static String getUserDB() {
      return userDB;
   }

   public static int getCombineServerID() {
      return combineServerID;
   }

   public static String getInterServerHost() {
      return InterServerHost;
   }

   public static int getInterServerPort() {
      return InterServerPort;
   }

   public static Date getOpenServerTiem() {
      return openServerTiem;
   }

   public static Date getCombineServerTime() {
      return combineServerTime;
   }

   public static boolean isCombine() {
      return combineServerTime != null;
   }

   private static LoginParser initLoginParser() {
      switch(ParserID) {
      case 1:
         return new CommonParser();
      case 2:
         return new BaiDuParser();
      case 3:
         return new G2Parser();
      case 4:
         return new Ku7Parser();
      case 5:
         return new QqParser();
      case 6:
         return new SmallParser();
      default:
         return new CommonParser();
      }
   }

   public static String getLocalHost() {
      return localHost;
   }

   public static String getGameservletkey() {
      return "JyekxdHmOIwvxEvwxUSjheo85JK201";
   }

   public static String getVersion() {
      return "2014050801";
   }

   public static boolean isCheckPwd() {
      return checkPwd;
   }

   public static boolean isFcm() {
      return isFcm;
   }

   public static String getFcmUrl() {
      return fcmUrl;
   }

   public static void initDefaultEquitment(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int profession = Tools.getCellIntValue(sheet.getCell("A" + i));
         String[] equips = Tools.getCellValue(sheet.getCell("B" + i)).split(";");
         ArrayList list = new ArrayList();

         for(int j = 0; j < equips.length; ++j) {
            String[] e = equips[j].split(",");
            list.add(new int[]{Integer.parseInt(e[0]), Integer.parseInt(e[1]), Integer.parseInt(e[2])});
         }

         defaultEquipMap.put(profession, list);
      }

   }

   public static ArrayList getDefaultEquipment(int professionType) {
      return (ArrayList)defaultEquipMap.get(professionType);
   }

   public static boolean isCanGMCommand() {
      return canGMCommand;
   }

   public static boolean isShowAllRoles() {
      return showAllRoles;
   }

   public static boolean isAllowError() {
      return allowError;
   }

   public static void addMuBan(int type, String mark, String timestamp) {
      switch(type) {
      case 1:
         BAN_USER_MAP.put(mark, timestamp);
         break;
      case 2:
         BAN_IP_MAP.put(mark, timestamp);
      }

   }

   public static String getGameName() {
      return gameName;
   }

   public static void openPayUrl(Player player) {
   }

   public static boolean isAutoTask() {
      return autoTask;
   }

   public static boolean isPwLogin() {
      return pwLogin;
   }

   public static String getServerIp() {
      return serverIp;
   }

   public static String getGameverifykey() {
      return "Kuejnj3lXJHnhs87wmnLmnxgsRwsxP";
   }

   public static int getSxhj() {
      return sxhj;
   }

   public static boolean isOpenWhiteList() {
      return openWhiteList;
   }

   public static boolean inWhiteList(String userName) {
      return whilteMap.containsKey(userName);
   }

   public static boolean isPushOffline() {
      return pushOffline;
   }

   public static boolean isCreateRobot() {
      return createRobot;
   }

   public static void checkOpenServer() {
      if (isDebug && drOpenTime) {
         GlobalDBManager.getOpenServerTime();
      }

   }
}
