package com.mu.game.model.rewardhall.sign;

import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.rewardhall.RewardItemData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignRewardData {
   private static Logger logger = LoggerFactory.getLogger(SignRewardData.class);
   private int id;
   private String name;
   private int count;
   private List roundList1 = new ArrayList();
   private List roundList2 = new ArrayList();

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public void addRoundReward(List rewardList1, List rewardList2) {
      SignRewardData.RoundReward rr1 = new SignRewardData.RoundReward(rewardList1);
      SignRewardData.RoundReward rr2 = new SignRewardData.RoundReward(rewardList2);
      this.roundList1.add(rr1);
      this.roundList2.add(rr2);
   }

   public List getRewardList(Player player, int round) {
      return ((SignRewardData.RoundReward)this.roundList1.get(Math.min(this.roundList1.size() - 1, Math.max(0, round - 1)))).getRewardList(player.getProType());
   }

   public List getVIPRewardList(Player player, int round) {
      return ((SignRewardData.RoundReward)this.roundList2.get(Math.min(this.roundList2.size() - 1, Math.max(0, round - 1)))).getRewardList(player.getProType());
   }

   class RoundReward {
      List list0 = new ArrayList();
      List list1 = new ArrayList();
      List list2 = new ArrayList();

      public RoundReward(List rewardList) {
         Iterator it = rewardList.iterator();

         while(true) {
            while(true) {
               while(it.hasNext()) {
                  RewardItemData rid = (RewardItemData)it.next();
                  ItemModel im = ItemModel.getModel(rid.getModelID());
                  if (im != null) {
                     if (im.getProfession().isEmpty()) {
                        this.list0.add(rid);
                        this.list1.add(rid);
                        this.list2.add(rid);
                     } else {
                        Iterator var7 = im.getProfession().iterator();

                        while(var7.hasNext()) {
                           Integer proID = (Integer)var7.next();
                           int proType = Profession.getProfessionID(proID.intValue());
                           switch(proType) {
                           case 0:
                              if (!this.list0.contains(rid)) {
                                 this.list0.add(rid);
                              }
                              break;
                           case 1:
                              if (!this.list1.contains(rid)) {
                                 this.list1.add(rid);
                              }
                              break;
                           case 2:
                              if (!this.list2.contains(rid)) {
                                 this.list2.add(rid);
                              }
                           }
                        }
                     }
                  } else {
                     SignRewardData.logger.error("SignRewardData[{}] config error, not found item by id[{}]", SignRewardData.this.id, rid.getModelID());
                  }
               }

               return;
            }
         }
      }

      public List getRewardList(int profession) {
         return profession == 0 ? this.list0 : (profession == 1 ? this.list1 : (profession == 2 ? this.list2 : null));
      }
   }
}
