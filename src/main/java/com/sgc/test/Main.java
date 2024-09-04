package com.sgc.test;

import java.lang.*;
import java.util.*;

// 拓扑排序
class Main{
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        Map<Integer, List<Integer>> map = new HashMap<>();
        while(n-->0){
            int u = scanner.nextInt(), v = scanner.nextInt();
            // node1
            List<Integer> list  = null;
            if(map.containsKey(u)){
                list = map.get(u);
            }else{
                list = new ArrayList<Integer>();
            }
            list.add(v);
            map.put(u, list);
            // node2
            list = null;
            if(map.containsKey(v)){
                list = map.get(v);
            }else{
                list = new ArrayList<Integer>();
            }
            map.put(v, list);
        }

        while(!map.isEmpty()){
            List<Integer> nowList = new ArrayList<>();
            List<Integer> removeList = new ArrayList<>();
            for(Map.Entry<Integer, List<Integer>> entry : map.entrySet()){
                // if(entry.getValue() == null)
                // System.out.println(entry.getKey() + " " + entry.getValue());
                if(check(entry.getValue())){

                    visited[entry.getKey()] = 1;
                    nowList.add(entry.getKey());
                    removeList.add(entry.getKey());
                }
            }
            // map.remove(entry.getKey());
            for(int i=0 ; i<removeList.size() ; i++){
                map.remove(removeList.get(i));
            }
            for(int i=0 ; i<nowList.size() ; i++){
                if(i==0){
                    System.out.print(nowList.get(i));
                }else{
                    System.out.print(" " + nowList.get(i));
                }
            }
        }

    }
    static int[] visited = new int[10005];
    public static boolean check(List<Integer> list){
        boolean flg = true;
        for(int i=0 ; i<list.size() ; i++){
            if(visited[list.get(i)] == 0)
                return false;
        }
        return true;
    }
}


