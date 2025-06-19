package com.mu.game.plat;

import com.mu.config.Global;
import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executor;
import com.mu.game.CenterManager;
import com.mu.game.dungeon.Dungeon;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.dungeon.PlayerInterruptInfo;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.map.LineMap;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import com.mu.game.model.unit.player.RoleInfo;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.ListPacket;
import java.awt.Point;
import java.util.List;
import org.jboss.netty.channel.Channel;

public class LoginUtil {
   public static final int Init_Normal = 1;
   public static final int Init_InterServer = 2;
   public static final int Init_BackServer = 3;

   public static Player loginGame(LoginInfo info, Channel channel, boolean isInterServer) {
      Player player = new Player();
      player.setUser(info.getUser());
      player.setChannel(channel);
      player.setGuider(info.isGuider());
      player.setClient(info.isClient());
      player.setRoleList(PlayerDBManager.getRoleInfoList(info.getUser()));
      CenterManager.addPlayerByLogin(player, isInterServer);
      return player;
   }

   public static void initRole(Player player, RoleInfo info, int initType) {
      player.initManager();
      player.setID(info.getID());
      player.setName(info.getName());
      player.setCurrentExp(info.getCurrentExp());
      player.setHeader(info.getHeader());
      player.setGender(info.getGender());
      player.setLevel(info.getLevel());
      player.setProType(info.getProType());
      player.setProLevel(info.getProLevel());
      int professionID = Profession.getProID(info.getProType(), info.getProLevel());
      player.setProfessionID(professionID);
      player.setWarComment(info.getWarComment());
      player.setWarCommentTime(info.getWarCommentTime());
      player.setRemoteHost(info.getRemoteHost());
      player.setRemotePort(info.getRemotePort());
      int mapID = info.getMapID();
      player.setWorldMapID(mapID);
      int tX = info.getX();
      int tY = info.getY();
      player.setWorldPoint(tX, tY);
      player.setVIPLevel(info.getVipLevel());
      player.setVIPExp(info.getVipExp());
      player.setTodayOnlineTime(info.getTodayOnlineTime());
      player.setTotalOnlineTime(info.getTotalOnlineTime());
      player.setLogoutTime(info.getLogoutTime());
      player.getOnlineManager().setYesterdayOnlineTime(info.getTodayOnlineClearTime());
      player.setLoginTime(System.currentTimeMillis());
      player.initBasisData(info);
      player.setPoSTR(info.getPoSTR());
      player.setPoDEX(info.getPoDEX());
      player.setPoCON(info.getPoCON());
      player.setPoINT(info.getPoINT());
      player.setPotential(info.getPotential());
      player.getPkMode().setPKModeById(info.getPkStatus());
      player.loadEvil(info.getEvil());
      player.setMoney(info.getMoney());
      Player.initExternal(player.getExternals(), player.getProType());
      player.setRedeemPoints(info.getRedeemPoint());
      player.setBindIngot(info.getBindIngot());
      player.setFinishPreview(info.getFinishPreivew());
      MapData md;
      if (!Global.isInterServiceServer()) {
         md = MapConfig.getMapData(mapID);
         if (md.getInterMapType() == 2) {
            mapID = 10001;
            MapData mdBack = MapConfig.getMapData(mapID);
            player.setWorldMapID(mapID);
            tX = mdBack.getDefaultX();
            tY = mdBack.getDefaultY();
            info.setX(tX);
            info.setY(tY);
            player.setWorldPoint(tX, tY);
         }

         PlayerInterruptInfo interruptInfo = DungeonManager.removeInterruptInfo(player.getID());
         if (interruptInfo != null) {
            Dungeon dun = DungeonManager.getDungeon(interruptInfo.getDungeonID());
            if (dun != null) {
               try {
                  DungeonMap dunMap = dun.getMapByIndex(interruptInfo.getMapIndex());
                  if (dunMap != null) {
                     player.setMap(dunMap);
                     tX = interruptInfo.getX();
                     tY = interruptInfo.getY();
                     player.setPosition(tX, tY);
                  }
               } catch (Exception var11) {
                  var11.printStackTrace();
               }
            }
         }
      } else {
         mapID = 10001;
         md = MapConfig.getMapData(mapID);
         player.setWorldMapID(mapID);
         tX = md.getDefaultX();
         tY = md.getDefaultY();
         info.setX(tX);
         info.setY(tY);
         player.setWorldPoint(tX, tY);
      }

      if (player.getMap() == null) {
         LineMap lm = MapConfig.getLineMap(mapID);
         Map map = lm.getEarlyMap();
         player.setMap(map);
         player.setWorldPoint(info.getX(), info.getY());
         if (map.isBlocked(info.getX(), info.getY())) {
            int[] tmpPosition = MapConfig.getResurrectionPoint(map.getID());
            info.setX(tmpPosition[1]);
            info.setY(tmpPosition[2]);
            tX = tmpPosition[1];
            tY = tmpPosition[2];
            player.setWorldPoint(tmpPosition[1], tmpPosition[2]);
         }
      }

      player.getMap().addPlayer(player, new Point(tX, tY));
      player.getBuffManager().createAndStartBuff(player, 80004, 1, false, 0L, (List)null);
      if (player.getEvil() > 0) {
         player.getBuffManager().createAndStartBuff(player, 80002, 1, false, 0L, (List)null);
      }

      CenterManager.addPlayerByEnterGame(player);
      player.setRoleInfo(info);
      player.getSystemSetup().reset(info.getSetup());
      requestPlayerSystem(player, initType, player.getUserName(), player.getUser().getServerID());
   }

   private static void requestPlayerSystem(Player player, int initType, String userName, int serverId) {
      try {
         WriteOnlyPacket packet = Executor.GetPlayerSystem.toPacket(player.getID(), initType, userName, serverId);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static ListPacket getPlayerInfo(long rid) {
      ListPacket lp = new ListPacket();
      return lp;
   }
}
