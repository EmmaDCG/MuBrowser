package com.mu.game;

import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.game.plat.LoginInfo;
import com.mu.game.task.schedule.ScheduleTask;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.account.DisconnectByInterRelogin;
import com.mu.io.game.packet.imp.sys.DisconnectReason;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CenterManager {
   private static final ConcurrentHashMap c2GAndG2sChannelMap = new ConcurrentHashMap();
   private static final ConcurrentHashMap g2SAndC2gChannelMap = new ConcurrentHashMap();
   private static final ConcurrentHashMap gameChannelPlayerMap = new ConcurrentHashMap();
   private static final ConcurrentHashMap userNameMap = new ConcurrentHashMap();
   private static final ConcurrentHashMap roleNameMap = new ConcurrentHashMap();
   private static final ConcurrentHashMap roleIdPlayerMap = new ConcurrentHashMap();
   private static final ConcurrentHashMap interChannelMap = new ConcurrentHashMap();
   private static final ConcurrentHashMap remoteMap = new ConcurrentHashMap();
   private static final ConcurrentHashMap serverChannelIpMap = new ConcurrentHashMap(8, 0.75F, 2);
   private static final ConcurrentHashMap uploadMap = new ConcurrentHashMap();
   private static final Logger logger = LoggerFactory.getLogger(CenterManager.class);

   public static void addG2sChannelByC2gChannel(Channel c2gChannel, Channel g2sChannel) {
      c2GAndG2sChannelMap.put(c2gChannel, g2sChannel);
   }

   public static Channel getG2sChannel(Channel c2gChannel) {
      return (Channel)c2GAndG2sChannelMap.get(c2gChannel);
   }

   public static Channel removeG2sChannel(Channel c2gChannel) {
      return (Channel)c2GAndG2sChannelMap.remove(c2gChannel);
   }

   public static void addC2gChannelByG2sChannel(Channel g2sChannel, Channel c2gChannel) {
      g2SAndC2gChannelMap.put(g2sChannel, c2gChannel);
   }

   public static Channel getC2gChannel(Channel g2sChannel) {
      return (Channel)g2SAndC2gChannelMap.get(g2sChannel);
   }

   public static Channel removeC2gChannel(Channel g2sChannel) {
      return (Channel)g2SAndC2gChannelMap.remove(g2sChannel);
   }

   public static Player getPlayerByGameChannel(Channel channel) {
      return (Player)gameChannelPlayerMap.get(channel);
   }

   public static Player getPlayerByRoleID(long rid) {
      return (Player)roleIdPlayerMap.get(rid);
   }

   public static void addChannelIp(Channel channel, String ip) {
      serverChannelIpMap.put(channel, ip);
   }

   public static String getIpByChannel(Channel channel) {
      return (String)serverChannelIpMap.get(channel);
   }

   public static String removeIp(Channel channel) {
      return (String)serverChannelIpMap.remove(channel);
   }

   public static boolean isOnline(long rid) {
      return roleIdPlayerMap.containsKey(rid);
   }

   public static ConcurrentHashMap getAllPlayerMap() {
      return roleIdPlayerMap;
   }

   public static Player getPlayerByUserName(String name, int sid) {
      return (Player)userNameMap.get(name + "_" + sid);
   }

   public static Iterator getAllPlayerIterator() {
      return roleIdPlayerMap.values().iterator();
   }

   public static void addPlayerByLogin(Player player, boolean isInterserver) {
      if (!isInterserver) {
         userNameMap.put(player.getUserName() + "_" + player.getUser().getServerID(), player);
      }

      gameChannelPlayerMap.put(player.getChannel(), player);
   }

   public static void clearNotInPlayer(Channel channel) {
      if (channel != null) {
         gameChannelPlayerMap.remove(channel);
      }
   }

   public static void addPlayerByEnterGame(Player player) {
      roleIdPlayerMap.put(player.getID(), player);
      roleNameMap.put(player.getName(), player);
      logger.info("one player {} enter game ,player size = {},channel = {}\t{}", new Object[]{player.getName(), roleIdPlayerMap.size(), player.getChannel(), System.currentTimeMillis()});
      if (logger.isDebugEnabled()) {
         logger.debug("统计:" + c2GAndG2sChannelMap.size() + "\t" + g2SAndC2gChannelMap.size() + "\t" + gameChannelPlayerMap.size() + "\t" + userNameMap.size() + "\t" + roleIdPlayerMap.size());
      }

   }

   public static Player getPlayerByRoleName(String name) {
      return (Player)roleNameMap.get(name);
   }

   public static void logOutByRelogin(Player oldPlayer, LoginInfo info) {
      DisconnectReason.pushDisconnect(info.getNewChannel(), MessageText.getText(11));
      DisconnectReason.pushDisconnect(oldPlayer.getChannel(), MessageText.getText(9));
      oldPlayer.setDestroyType(2);
      oldPlayer.destroy();
   }

   public static void logOutByInterRelogin(String userName, int sid, LoginInfo info, Channel interChannel) {
      DisconnectByInterRelogin disc = new DisconnectByInterRelogin();
      interChannel.write(disc.toBuffer());
      disc.destroy();
      removeInterChannel(userName, sid);
   }

   public static Player removePlayerByGameChannel(Channel g2sChannel) {
      return (Player)gameChannelPlayerMap.remove(g2sChannel);
   }

   public static void removePlayer(long rid, String roleName, String userName, int sid, Channel channel) {
      roleIdPlayerMap.remove(rid);
      if (userName != null) {
         userNameMap.remove(userName + "_" + sid);
      }

      if (roleName != null) {
         roleNameMap.remove(roleName);
      }

      if (channel != null) {
         gameChannelPlayerMap.remove(channel);
         logger.info("online player size = {}", getOnlinePlayerSize());
      }

      if (logger.isDebugEnabled()) {
         logger.debug("统计:" + c2GAndG2sChannelMap.size() + "\t" + g2SAndC2gChannelMap.size() + "\t" + gameChannelPlayerMap.size() + "\t" + userNameMap.size() + "\t" + roleIdPlayerMap.size());
      }

   }

   public static void gameServerActivieOffChannel(Channel channel) {
      Player player = getPlayerByGameChannel(channel);
      logger.info("server drop player....");
      if (player != null) {
         player.setShouldDestroy(true);
      } else {
         channel.close();
      }

   }

   public static void dropAllPlayers() {
      Global.setServerIsDown(true);
      Iterator it = getAllPlayerIterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         player.setShouldDestroy(true);
      }

      ScheduleTask.stop();
   }

   public static boolean uploadCheck(Channel channel, int lengh) {
      long[] data = (long[])uploadMap.get(channel);
      long now = System.currentTimeMillis();
      if (data == null) {
         uploadMap.put(channel, new long[]{now, (long)lengh});
         return false;
      } else {
         if (now - data[0] > 60000L) {
            data[0] = now;
            data[1] = (long)lengh;
         } else {
            long size = (long)lengh + data[1];
            if (size > 921600L) {
               gameServerActivieOffChannel(channel);
               return true;
            }

            data[1] = size;
         }

         return false;
      }
   }

   public static void removeUploadRecord(Channel channel) {
      uploadMap.remove(channel);
   }

   public static Channel getInterChannel(String userName, int sid) {
      return (Channel)interChannelMap.get(userName + "_" + sid);
   }

   public static void addInterChannel(String userName, int sid, Channel channel) {
      interChannelMap.put(userName + "_" + sid, channel);
   }

   public static void removeInterChannel(String userName, int sid) {
      interChannelMap.remove(userName + "_" + sid);
   }

   public static int getOnlinePlayerSize() {
      return roleIdPlayerMap.size();
   }

   public static void addRemoteChannelInfo(RemoteChannelInfo info) {
      remoteMap.put(info.getC2gChannel(), info);
   }

   public static RemoteChannelInfo getRemoteChannelInfo(Channel channel) {
      return (RemoteChannelInfo)remoteMap.get(channel);
   }

   public static RemoteChannelInfo removeRemoteChannelInfo(Channel channel) {
      return (RemoteChannelInfo)remoteMap.remove(channel);
   }

   public static void worldBroadcast(WriteOnlyPacket packet) {
      Iterator it = getAllPlayerIterator();

      while(it.hasNext()) {
         ((Player)it.next()).writePacket(packet);
      }

   }
}
