package com.mu.game.plat;

import com.mu.config.Global;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Base64;
import com.mu.utils.MD5;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class G2Parser extends LoginParser {
   Logger logger = LoggerFactory.getLogger(G2Parser.class);
   private ConcurrentHashMap newRegMap = Tools.newConcurrentHashMap2();

   public void doNewRegister(long rid) {
      this.newRegMap.put(rid, System.currentTimeMillis());
   }

   public void doNewPlayer(Player player) {
      Long timeLong = (Long)this.newRegMap.remove(player.getID());
      if (timeLong != null) {
         try {
            StringBuffer sb = new StringBuffer();
            sb.append("pid=1&");
            sb.append("gkey=").append(Global.getKey("gkey")).append("&");
            sb.append("level=").append(player.getLevel()).append("&");
            sb.append("name=").append(player.getName()).append("&");
            sb.append("playid=").append(player.getID()).append("&");
            sb.append("qid=").append(player.getUserName()).append("&");
            sb.append("server_id=").append(player.getUser().getServerID()).append("&");
            sb.append("toptype=").append("战斗力").append("&");
            sb.append("topvalue=").append(player.getWarComment()).append("&");
            sb.append("type=").append(1).append("&");
            sb.append("regdate=").append(timeLong.longValue() / 1000L).append("&");
            String singStr = Global.getKey("gkey") + player.getLevel() + player.getName() + player.getID() + player.getUserName() + timeLong.longValue() / 1000L + player.getUser().getServerID() + "战斗力" + player.getWarComment() + "1" + Global.getLoginKey();
            String sign = MD5.md5s(singStr);
            sb.append("sign=").append(sign);
            final String url = sb.toString();
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  Tools.getUrlGetContent(Global.getUrl("postRole") + url);
               }
            });
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   public boolean checkSign(HashMap paras) {
      String auth = (String)paras.get("auth");
      String sign = (String)paras.get("sign");
      String[] tmp = null;

      try {
         String m = new String(Base64.decode(auth));
         tmp = m.split("&");
         if (tmp.length != 5) {
            this.logger.error("参数数量不对{}", tmp.length);
            return false;
         }

         for(int i = 0; i < tmp.length; ++i) {
            String[] strP = tmp[i].split("=");
            if (strP.length != 2) {
               this.logger.error("参数strP数量不对{}", strP.length);
               return false;
            }

            paras.put(strP[0].trim(), strP[1].trim());
         }

         try {
            String a = "pid=" + (String)paras.get("pid") + "&sid=" + (String)paras.get("sid") + "&uid=" + (String)paras.get("uid") + "&time=" + (String)paras.get("time") + "&indulge=" + (String)paras.get("indulge");
            String a1 = new String(Base64.encode(a.getBytes()));
            if (!MD5.md5s(a1 + Global.getLoginKey()).equals(sign)) {
               this.logger.error("md5验证失败,{}", paras.get("uid"));
               return false;
            }

            return true;
         } catch (Exception var12) {
            var12.printStackTrace();
            return false;
         }
      } catch (Exception var13) {
         ;
      } finally {
         tmp = null;
      }

      return false;
   }

   public boolean needPostWhenLevelUp(Player player) {
      return true;
   }

   public boolean needPostWhenLogin() {
      return true;
   }

   public LoginInfo getLoginInfo(HashMap paras) {
      String uid = (String)paras.get("uid");
      String sid = (String)paras.get("sid");
      String cmFlag = ((String)paras.get("indulge")).toLowerCase();
      LoginInfo info = new LoginInfo();
      info.setNeedAntiAddiction(cmFlag.equals("y"));
      info.setServerID(Integer.parseInt(sid));
      info.setUserName(uid);
      return info;
   }
}
