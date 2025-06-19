package com.mu.io.game.packet.imp.account;

import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.config.VariableConstant;
import com.mu.db.manager.GlobalLogDBManager;
import com.mu.db.manager.PlayerDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import com.mu.game.model.unit.player.RoleInfo;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.DFA;
import com.mu.utils.RndNames;
import com.mu.utils.Time;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateRole extends ReadAndWritePacket {
   private static final Logger logger = LoggerFactory.getLogger(CreateRole.class);

   public CreateRole(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public CreateRole() {
      super(10002, (byte[])null);
   }

   private synchronized void createRole(Player player, int proType, String name) throws Exception {
      long now = System.currentTimeMillis();
      CreateRole cr = new CreateRole();
      int hair = -1;
      long[] result = PlayerDBManager.createRole(player, proType, hair, name);
      if (result[0] == 1L) {
         RndNames.deleteName(player.getUserName());
         ArrayList rList = PlayerDBManager.getRoleInfoList(player.getUser());
         player.setRoleList(rList);
         RoleInfo info = player.getRoleInfoById(result[1]);
         if (info != null) {
            cr.writeBoolean(true);
            cr.writeDouble((double)info.getID());
            cr.writeUTF(info.getName());
            cr.writeShort(info.getLevel());
            int pid = Profession.getProID(info.getProType(), info.getProLevel());
            cr.writeByte(pid);
            cr.writeShort(Profession.getProfession(pid).getLoginImg());
            Global.getLoginParser().doNewRegister(result[1]);
            GlobalLogDBManager.insertRole(info.getID(), player.getUserName(), info.getName(), player.getUser().getServerID(), Time.getTimeStr(now), 1, info.getProType(), info.getProLevel());
            if (Global.getLoginParser().needPostWhenCreate()) {
               Global.getLoginParser().doCreateRole(player);
            }
         } else {
            cr.writeBoolean(false);
         }
      } else {
         String msg = MessageText.getText((int)result[0]);
         cr.writeBoolean(false);
         SystemMessage.writeMessage(player, msg, (int)result[0]);
         cr.writeUTF(msg);
      }

      player.writePacket(cr);
      cr.destroy();
      cr = null;
      if (result[0] == 1L) {
         GetRoleList.sendRoles(player, player.getChannel());
      }

      if (logger.isDebugEnabled()) {
         logger.debug("create role cost time {}", System.currentTimeMillis() - now);
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      String name = this.readUTF().trim();
      byte profession = this.readByte();
      String msg;
      if (name.length() > VariableConstant.MaxNameSize) {
         this.writeBoolean(false);
         msg = MessageText.getText(1040).replace("%s%", String.valueOf(VariableConstant.MaxNameSize));
         this.writeUTF(msg);
         SystemMessage.writeMessage(player, msg, 1040);
         player.writePacket(this);
      } else if (name.length() >= 1 && !DFA.hasKeyWords(name) && name.indexOf(" ") == -1 && name.indexOf("　") == -1 && name.indexOf("[") == -1 && name.indexOf("]") == -1) {
         this.createRole(player, profession, name);
      } else {
         this.writeBoolean(false);
         msg = MessageText.getText(1003);
         this.writeUTF(msg);
         SystemMessage.writeMessage(player, msg, 1003);
         player.writePacket(this);
      }
   }
}
