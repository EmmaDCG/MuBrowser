package com.mu.game.model.map;

import com.mu.game.model.unit.Creature;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AStar {
   public static double SCORE_SL = 1.0D;
   public static double SCORE_OL = Math.sqrt(2.0D);
   static Logger logger = LoggerFactory.getLogger(AStar.class);
   private static int TILE_CENTER = 300;
   public static final int MAX_FIND_COUNT = 2000;

   public static ScheduledFuture pursue(final Creature creature, Creature mostHated, final Point endPosition) {
      final Map map = creature.getMap();
      ScheduledFuture future = ThreadFixedPoolManager.POOL_FINDWAY.schedule(new Runnable() {
         public void run() {
            ArrayList optimizePath = new ArrayList();
            Point startPosition = creature.getActualPosition();
            int s_x = startPosition.x;
            int s_y = startPosition.y;
            long now = System.currentTimeMillis();
            if (AStar.astarFindingWay(optimizePath, map, s_x, s_y, endPosition.x, endPosition.y) != null) {
               long cost = System.currentTimeMillis() - now;
               if (cost >= 20L) {
                  AStar.logger.error("astar cost time = {},creature is {}", cost, creature.getName() + "\t" + map.getID());
               }

               Point[] path = new Point[optimizePath.size() + 2];

               for(int i = 0; i < optimizePath.size(); ++i) {
                  path[i + 1] = (Point)optimizePath.get(i);
               }

               path[0] = new Point(s_x, s_y);
               path[path.length - 1] = new Point(endPosition);
               creature.startMove(path, System.currentTimeMillis());
               path = null;
            } else {
               creature.startMove(new Point[]{creature.getActualPosition(), endPosition}, System.currentTimeMillis());
            }

            optimizePath.clear();
            optimizePath = null;
         }
      }, 0L);
      return future;
   }

   public static List astarFindingWay(List optimizePath, Map map, int s_x, int s_y, int e_x, int e_y) {
      if (!map.isBlocked(s_x, s_y) && !map.isBlocked(e_x, e_y)) {
         s_x = map.getTileX(s_x);
         s_y = map.getTileY(s_y);
         e_x = map.getTileX(e_x);
         e_y = map.getTileY(e_y);
         LinkedList newfindPath = new LinkedList();
         ArrayList openList = new ArrayList();
         byte[][] blocks = map.getBlocks();
         AstarNode[][] nodeMap = new AstarNode[blocks.length][blocks[0].length];
         AstarNode startNode = new AstarNode(s_x, s_y, false);
         nodeMap[s_x][s_y] = startNode;
         startNode.g = 0.0D;
         startNode.h = assessmentScore(s_x, s_y, e_x, e_y);
         startNode.updateF();
         openList.add(startNode);
         int var11 = 0;

         while(!openList.isEmpty()) {
            AstarNode currentNode = subsidenceOpenNode(openList);
            if (currentNode.x == e_x && currentNode.y == e_y) {
               newfindPath.clear();

               for(AstarNode node = currentNode; node != startNode; node = node.parent) {
                  newfindPath.addFirst(node);
               }

               newfindPath.addFirst(startNode);
               calculatePath(map, newfindPath, optimizePath);
               nodeMap = null;
               openList.clear();
               openList = null;
               return newfindPath;
            }

            if (var11++ > 2000) {
               return null;
            }

            int rs_x = Math.max(0, currentNode.x - 1);
            int re_x = Math.min(map.getBlocks().length - 1, currentNode.x + 1);
            int rs_y = Math.max(0, currentNode.y - 1);
            int re_y = Math.min(map.getBlocks()[0].length - 1, currentNode.y + 1);

            for(int i = rs_x; i <= re_x; ++i) {
               for(int j = rs_y; j <= re_y; ++j) {
                  if (!map.tileIsBlocked(i, j) && (i != currentNode.x || j != currentNode.y) && (!map.tileIsBlocked(i, currentNode.y) || !map.tileIsBlocked(currentNode.x, j))) {
                     double g = currentNode.g + (i != currentNode.x && j != currentNode.y ? SCORE_OL : SCORE_SL);
                     double h = assessmentScore(i, j, e_x, e_y);
                     double f = g + h;
                     if (nodeMap[i][j] != null) {
                        if (nodeMap[i][j].f > f) {
                           nodeMap[i][j].g = g;
                           nodeMap[i][j].f = f;
                           nodeMap[i][j].parent = currentNode;
                           if (nodeMap[i][j].openIndex >= 0) {
                              ebullitionOpenNode(openList, nodeMap[i][j].openIndex + 1);
                           }
                        }
                     } else {
                        AstarNode subNode = new AstarNode(i, j, false);
                        nodeMap[i][j] = subNode;
                        subNode.g = g;
                        subNode.h = h;
                        subNode.f = f;
                        subNode.parent = currentNode;
                        subNode.openIndex = openList.size();
                        openList.add(subNode);
                        ebullitionOpenNode(openList, openList.size());
                     }
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static void ebullitionOpenNode(List openList, int index) {
      int parentIndex;
      for(; index > 1; index = parentIndex) {
         parentIndex = index / 2;
         AstarNode n1 = (AstarNode)openList.get(index - 1);
         AstarNode n2 = (AstarNode)openList.get(parentIndex - 1);
         if (n2.f > n1.f) {
            n2.openIndex = index - 1;
            n1.openIndex = parentIndex - 1;
            openList.set(n2.openIndex, n2);
            openList.set(n1.openIndex, n1);
         }
      }

   }

   private static AstarNode subsidenceOpenNode(List openList) {
      AstarNode minNode = (AstarNode)openList.get(0);
      AstarNode parentNode = (AstarNode)openList.remove(openList.size() - 1);
      if (openList.size() > 0) {
         parentNode.openIndex = 0;
         openList.set(parentNode.openIndex, parentNode);
         int index = 1;

         while(true) {
            int temp = index;
            int subIndex1 = 2 * index;
            if (subIndex1 <= openList.size()) {
               AstarNode subNode1 = (AstarNode)openList.get(subIndex1 - 1);
               if (subNode1.f < ((AstarNode)openList.get(index - 1)).f) {
                  index = subIndex1;
               }
            }

            int subIndex2 = 2 * index + 1;
            AstarNode subNode;
            if (subIndex2 <= openList.size()) {
               subNode = (AstarNode)openList.get(subIndex2 - 1);
               if (subNode.f < ((AstarNode)openList.get(index - 1)).f) {
                  index = subIndex2;
               }
            }

            if (index == index) {
               break;
            }

            parentNode = (AstarNode)openList.get(temp - 1);
            subNode = (AstarNode)openList.get(index - 1);
            parentNode.openIndex = index - 1;
            subNode.openIndex = temp - 1;
            openList.set(subNode.openIndex, subNode);
            openList.set(parentNode.openIndex, parentNode);
         }
      }

      minNode.openIndex = -1;
      return minNode;
   }

   private static double assessmentScore(int sx, int sy, int tx, int ty) {
      return (double)(Math.abs(sx - tx) + Math.abs(sy - ty));
   }

   private static void calculatePath(Map map, List findPath, List optimizePath) {
      if (!findPath.isEmpty()) {
         AstarNode[] nodeArr = (AstarNode[])findPath.toArray(new AstarNode[findPath.size()]);
         double lineX1 = (double)nodeArr[0].x + 0.5D;
         double lineY1 = (double)nodeArr[0].y + 0.5D;
         optimizePath.add(new Point(map.getXCenterByTile(nodeArr[0].x), map.getYCenterByTile(nodeArr[0].y)));
         Point point = new Point(-1, -1);
         int i = nodeArr.length - 1;

         for(int index = 0; i > 0; --i) {
            double lineX2 = (double)nodeArr[i].x + 0.5D;
            double lineY2 = (double)nodeArr[i].y + 0.5D;
            if (!checkLineBlock(map, point, lineX1, lineY1, lineX2, lineY2) || index >= i - 1) {
               point.move(-1, -1);
               lineX1 = lineX2;
               lineY1 = lineY2;
               optimizePath.add(new Point(map.getXCenterByTile(nodeArr[i].x), map.getYCenterByTile(nodeArr[i].y)));
               if (i == nodeArr.length - 1) {
                  break;
               }

               index = i;
               i = nodeArr.length;
            }
         }

      }
   }

   private static boolean checkLineBlock(Map map, Point point, double x1, double y1, double x2, double y2) {
      if (x2 < x1) {
         return checkLineBlock(map, point, x2, y2, x1, y1);
      } else {
         int shortX = point.x;
         int incrY = y2 == y1 ? 0 : (y2 < y1 ? -1 : 1);
         point.move((int)x1, (int)y1);
         if (x1 == x2) {
            return checkXSegmentBlocked(map, point, point.x, y1, y2, incrY);
         } else {
            double k = (y2 - y1) / (x2 - x1);
            double b = y2 - k * x2;
            int x = point.x;
            if (x < shortX && shortX < (int)x2 && (int)x2 - x > 15 && checkXSegmentBlocked(map, point, shortX, k * (double)shortX + b, k * (double)(shortX + 1) + b, incrY)) {
               return true;
            } else {
               double y = y1;

               while(x < (int)x2) {
                  if (checkXSegmentBlocked(map, point, x++, y, y = k * (double)x + b, incrY)) {
                     return true;
                  }
               }

               return (double)x != x2 ? checkXSegmentBlocked(map, point, x, y, y2, incrY) : map.tileIsBlocked(x, (int)y2) || isDiagonalBlocked(map, point.x, point.y, x, (int)y2);
            }
         }
      }
   }

   private static boolean checkXSegmentBlocked(Map map, Point point, int x, double y1, double y2, int incrY) {
      boolean passSelf = incrY == 1;
      int passY = (passSelf || (double)((int)y1) != y1) && (!passSelf || y2 != (double)((int)y2)) ? Math.abs((int)y2 - (int)y1) + 1 : Math.abs((int)y2 - (int)y1);
      if (passY < 1) {
         return map.tileIsBlocked(point.x = x, point.y = (int)y1);
      } else {
         int y = !passSelf && (double)((int)y1) == y1 ? (int)y1 + incrY : (int)y1;
         if (isDiagonalBlocked(map, point.x, point.y, x, y)) {
            return true;
         } else {
            point.move(x, y);

            for(int i = 0; i < passY; ++i) {
               if (map.tileIsBlocked(point.x, point.y)) {
                  return true;
               }

               point.y += incrY;
            }

            point.y -= incrY;
            return false;
         }
      }
   }

   private static boolean isDiagonalBlocked(Map map, int x1, int y1, int x2, int y2) {
      return 1 == Math.abs(x2 - x1) && 1 == Math.abs(y2 - y1) && map.tileIsBlocked(x1, y2) && map.tileIsBlocked(x2, y1);
   }

   public static boolean isLineBlocked(Map map, Point start, Point end) {
      return checkLineBlock(map, new Point(-1, -1), (double)map.getTileX(start.x) + 0.5D, (double)map.getTileY(start.y) + 0.5D, (double)map.getTileX(end.x) + 0.5D, (double)map.getTileY(end.y) + 0.5D);
   }
}
