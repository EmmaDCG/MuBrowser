package com.mu.io.game.packet.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.item.Item;
import com.mu.game.model.pet.PetPropertyData;
import com.mu.game.model.pet.PetRank;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.unit.CreatureTemplate;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.robot.RobotManager;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.manager.SkillFactory;
import com.mu.game.top.TopManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.skill.AddSkill;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OtherPlayerDetail extends ReadAndWritePacket {
   public OtherPlayerDetail() {
      super(10249, (byte[])null);
   }

   public OtherPlayerDetail(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      final Player player = this.getPlayer();
      final long otherID = (long)this.readDouble();
      Player other = CenterManager.getPlayerByRoleID(otherID);
      if (other != null) {
         this.writeBytes(other.getView());
         player.writePacket(this);
      } else {
         Player other2 = RobotManager.getRobot(otherID);
         if (other2 != null) {
            this.writeBytes(other2.getView());
            player.writePacket(this);
         } else {
            byte[] bytes = TopManager.getView(otherID);
            if (bytes != null) {
               this.writeBytes(bytes);
               player.writePacket(this);
            } else {
               ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
                  public void run() {
                     OtherPlayerDetail.writeDetailByDB(player, otherID);
                  }
               });
            }
         }
      }

   }

   private static void writeDetailByDB(Player player, long rid) {
      try {
         byte[] bytes = PlayerDBManager.getRoleView(rid);
         if (bytes != null) {
            OtherPlayerDetail pd = new OtherPlayerDetail();
            pd.writeBytes(bytes);
            player.writePacket(pd);
            TopManager.addView(rid, bytes);
            bytes = null;
            pd.destroy();
            pd = null;
         } else {
            SystemMessage.writeMessage(player, 1021);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void writePlayer(Player other, WriteOnlyPacket packet) throws Exception {
      packet.writeDouble((double)other.getID());
      packet.writeUTF(other.getName());
      packet.writeShort(other.getLevel());
      packet.writeByte(other.getProfessionID());
      packet.writeUTF(other.getWarCommentText());
      packet.writeUTF(other.getGang() == null ? "" : other.getGang().getName());
      List itemList = other.getEquipment().getAllItems();
      packet.writeByte(itemList.size());
      Iterator var4 = itemList.iterator();

      Item stats;
      while(var4.hasNext()) {
         stats = (Item)var4.next();
         GetItemStats.writeItem(stats, packet);
      }

      itemList.clear();
      itemList = null;
      List stats2 = StatList2Client.getOtherPlayerStats();
      PlayerAttributes.setStatDetail(other, stats2, packet);
      int[] vipIcons = other.getVipIcons();
      packet.writeShort(vipIcons[0]);
      packet.writeShort(vipIcons[1]);
      stats2.clear();
      stats = null;
   }

   public static void writePlayerPet(Player player, WriteOnlyPacket packet) throws Exception {
      PetRank rank = player.getPetManager().getRank();
      boolean open = player.getPetManager().isOpen() && rank != null;
      Integer zdl = (Integer)player.getPetManager().getJoinAttributeMap().get(StatEnum.DOMINEERING);
      packet.writeBoolean(open);
      if (open) {
         int level = player.getPetManager().getLevel();
         PetPropertyData propertyData = rank.getPropertyData(level);
         packet.writeByte(rank.getRank());
         packet.writeUTF(rank.getName());
         packet.writeShort(rank.getModel());
         packet.writeShort(rank.getIcon());
         packet.writeByte(rank.getScale());
         packet.writeInt(propertyData.getPetZDL());
         packet.writeInt(zdl == null ? 0 : zdl.intValue());
         Skill skill = SkillFactory.createSkill(rank.getSkill(), 1, player);
         AddSkill.writeSkillDetail(skill, packet);
         packet.writeShort(rank.getRiseLevel());
         ArrayList list = CreatureTemplate.getTemplate(rank.getTemplateId()).getEquipEntry();
         packet.writeByte(list.size());
         Iterator var10 = list.iterator();

         while(var10.hasNext()) {
            ExternalEntry entry = (ExternalEntry)var10.next();
            packet.writeByte(entry.getType());
            packet.writeShort(entry.getModelID());
            packet.writeShort(entry.getEffectID());
         }
      }

   }
}
