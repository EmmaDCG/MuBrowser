package com.mu.io.game.packet.imp.map;

import com.mu.game.model.gang.GangManager;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.market.MarketManager;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.unitevent.imp.HangsetEvent;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.buff.ClientShowStatus;
import com.mu.io.game.packet.imp.equip.DurabilityPrompt;
import com.mu.io.game.packet.imp.equip.EquipEffect;
import com.mu.io.game.packet.imp.equip.ExcellentCountDes;
import com.mu.io.game.packet.imp.equip.RuneSetDes;
import com.mu.io.game.packet.imp.equip.StarSetDes;
import com.mu.io.game.packet.imp.equip.StoneSetDes;
import com.mu.io.game.packet.imp.guide.FunctionOpenPacket;
import com.mu.io.game.packet.imp.item.AddItem;
import com.mu.io.game.packet.imp.market.MarketConfig;
import com.mu.io.game.packet.imp.player.ExternalChange;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import com.mu.io.game.packet.imp.player.PlayerSystemSetup;
import com.mu.io.game.packet.imp.player.PushWarCommentText;
import com.mu.io.game.packet.imp.player.RolePanelStat;
import com.mu.io.game.packet.imp.player.SelfExternal;
import com.mu.io.game.packet.imp.player.hangset.HangSet;
import com.mu.io.game.packet.imp.shortcut.AddShortcut;
import com.mu.io.game.packet.imp.skill.AddSkill;
import com.mu.io.game.packet.imp.skill.SelectSkill;
import com.mu.io.game.packet.imp.skill.UpdateSkillCD;
import com.mu.io.game.packet.imp.storage.BackpackCount;
import com.mu.io.game.packet.imp.storage.DeportCount;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.ArrayList;
import java.util.List;

public class EnterMap extends ReadAndWritePacket {
   public EnterMap(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public EnterMap() {
      super(10103, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player != null) {
         player.setID((long)this.readDouble());
         ThreadCachedPoolManager.DB_SHORT.execute(new enterMapThread(player));
      }
   }

   public static void playerEnterMap(Player player) {
      try {
         PlayerSystemSetup.pushSetup(player);
         FunctionOpenPacket.pushRoleFunctionOpen(player);
         EnterMap packet = new EnterMap();
         packet.writeBoolean(true);
         packet.writeDouble((double)player.getID());
         packet.writeShort(player.getMapID());
         packet.writeInt(player.getX());
         packet.writeInt(player.getY());
         packet.writeByte(player.getPkMode().getPKModeID());
         packet.writeByte(player.getSelfEvilEnum().getFont());
         packet.writeShort(player.getHeader());
         packet.writeByte(player.getTitleManager().getEquipId());
         packet.writeUTF(GangManager.getViewGangName(player.getGang()));
         packet.writeShort(player.getUser().getServerID());
         PlayerAttributes.setStatDetail(player, StatList2Client.getPlayerPanelStats(), packet);
         ArrayList list = player.getRoleInfo().getExternal();
         ExternalChange.writeRoleExternal(list, packet);
         int[] icons = player.getVipIcons();
         packet.writeShort(icons[0]);
         packet.writeShort(icons[1]);
         packet.writeUTF(player.getUser().getBlueVip().getBlueIcon().getPrivilege());
         player.writePacket(packet);
         packet.destroy();
         RolePanelStat.sendToClient(player);
         player.getBuffManager().startLoadBuff();
         ClientShowStatus.sendToSelf(player);
         SelfExternal.sendToClient(player);
         BackpackCount.sendToClient(player);
         DeportCount.sendToClient(player, 4, player.getDepot().getPage());
         List itemList = player.getBackpack().getAllItems();
         AddItem.sendToClient(player, itemList, 1);
         itemList.clear();
         itemList = null;
         itemList = player.getEquipment().getAllItems();
         AddItem.sendToClient(player, itemList, 0);
         itemList.clear();
         itemList = null;
         Storage depot = player.getDepot();
         itemList = depot.getAllItems();
         AddItem.sendToClient(player, itemList, 4);
         itemList.clear();
         itemList = null;
         List itemList2 = MarketManager.getRoleItems(player.getID());
         AddItem.sendToClient(player, itemList2, 11);
         itemList2.clear();
         itemList = null;
         itemList = player.getTreasureHouse().getAllItems();
         AddItem.sendToClient(player, itemList, 14);
         itemList.clear();
         itemList = null;
         EquipEffect.sendToClient(player, player.getEquipment().getUnEffectMap());
         DurabilityPrompt.sendWhenFirstEnter(player);
         AddSkill.sendToClient(player);
         UpdateSkillCD.sendToClient(player);
         player.getTaskManager().onEventLogin(false);
         Skill skill = player.getSkillManager().getAutoSkill();
         skill = skill == null ? player.getSkillManager().getCommonSkill() : skill;
         SelectSkill.sendToClient(player, skill.getSkillID());
         AddShortcut.sendToClient(player, player.getShortcut().getEntries());
         SwitchLine.pushCurrentLine(player);
         HangSet.sendToClient(player, false);
         player.addMomentEvent(new HangsetEvent(player));
         RuneSetDes.sendToClient(player);
         StarSetDes.sendToClient(player);
         ExcellentCountDes.sendToClient(player);
         StoneSetDes.sendToClient(player);
         MarketConfig.sendToClient(player);
         PushWarCommentText.pushText(player);
         player.getItemManager().handleExpiredItem(true);
         player.getVIPManager().onEnterGame();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
