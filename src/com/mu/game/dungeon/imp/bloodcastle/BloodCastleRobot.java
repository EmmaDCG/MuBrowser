package com.mu.game.dungeon.imp.bloodcastle;

import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.robot.Robot;
import com.mu.game.model.unit.robot.RobotInfo;
import com.mu.utils.geom.MathUtil;
import java.util.ArrayList;
import java.util.Iterator;

public class BloodCastleRobot extends Robot {
   public BloodCastleRobot(RobotInfo info, BloodCastleMap map) {
      super(info, map);
      this.canSpeek = true;
   }

   public BloodCastleMap getBloodCastleMap() {
      return (BloodCastleMap)this.getMap();
   }

   public void doFollow() {
      super.doFollow();
   }

   public void searchTarget() {
      this.nextSkill = this.getRandomSkill();
      if (this.nextSkill != null) {
         ArrayList followList = this.getCurMonsterList();
         if (followList != null && followList.size() > 0) {
            this.setFollowTarget(((BloodCastleRobot.FollowMonster)followList.get(0)).getMonster());
            if (this.getFollowTarget() != null) {
               this.doFollow();
            }

            followList.clear();
         }

         followList = null;
      }
   }

   public ArrayList getCurMonsterList() {
      BloodCastleMap map = this.getBloodCastleMap();
      if (map == null) {
         return null;
      } else {
         ScheduleConditions con = map.getCurrentConditions();
         int schedule = map.getSchedule();
         ArrayList followList = new ArrayList();
         BloodCastleMonster m;
         if (schedule == 2) {
            m = map.getGateMonster();
            if (!m.isDie() && !m.isDestroy() && m.isCanBeAttacked()) {
               followList.add(new BloodCastleRobot.FollowMonster(m, 0, (BloodCastleRobot.FollowMonster)null));
            }
         } else if (schedule == 4) {
            m = map.getCoffinMonster();
            if (!m.isDie() && !m.isDestroy() && m.isCanBeAttacked()) {
               followList.add(new BloodCastleRobot.FollowMonster(m, 0, (BloodCastleRobot.FollowMonster)null));
            }
         } else if (schedule == 6) {
            m = map.getBloodAngel();
            if (!m.isDie() && !m.isDestroy() && m.isCanBeAttacked()) {
               followList.add(new BloodCastleRobot.FollowMonster(m, 0, (BloodCastleRobot.FollowMonster)null));
            }
         } else {
            Iterator it = this.getMap().getMonsterMap().values().iterator();

            while(it.hasNext()) {
               m = (BloodCastleMonster)it.next();
               if (!m.isDie() && !m.isDestroy() && m.isCanBeAttacked()) {
                  switch(schedule) {
                  case 1:
                     if (m.getBatch() == 1 && con.contains(m.getTemplateId())) {
                        followList.add(new BloodCastleRobot.FollowMonster(m, MathUtil.getDistance(this.getPosition(), m.getPosition()), (BloodCastleRobot.FollowMonster)null));
                     }
                  case 2:
                  default:
                     break;
                  case 3:
                     if (m.getBatch() == 3 && con.contains(m.getTemplateId())) {
                        followList.add(new BloodCastleRobot.FollowMonster(m, MathUtil.getDistance(this.getPosition(), m.getPosition()), (BloodCastleRobot.FollowMonster)null));
                     }
                  }
               }
            }
         }

         return followList;
      }
   }

   public void destroy() {
      super.destroy();
   }

   static class FollowMonster implements Comparable {
      private Monster monster;
      private int distance;

      private FollowMonster(Monster monster, int distance) {
         this.distance = 0;
         this.monster = monster;
         this.distance = distance;
      }

      public Monster getMonster() {
         return this.monster;
      }

      public int compareTo(BloodCastleRobot.FollowMonster o) {
         return this.distance - o.distance;
      }

      // $FF: synthetic method
      FollowMonster(Monster var1, int var2, BloodCastleRobot.FollowMonster var3) {
         this(var1, var2);
      }

      @Override
      public int compareTo(Object o) {
         return 0;
      }
   }
}
