package tool;

import java.util.*;

public class ComputeHomCC {
    private Map<Integer, Set<Integer>> graph = null;
    private int queryId = -1;
    private int edgeNum = 0;

    public ComputeHomCC(Map<Integer, Set<Integer>> graph, int queryId){
        this.graph = graph;
        this.queryId = queryId;
    }

    public Set<Integer> getCC(){
        Set<Integer> reSet = new HashSet<Integer>();
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.offer(queryId);
        reSet.add(queryId);
        while(!queue.isEmpty()){
            int curVertexId = queue.poll();
            Set<Integer> tempSet = graph.get(curVertexId);
            for (int nextVertexId: tempSet){
                edgeNum += 1;
                if (!reSet.contains(nextVertexId)){
                    queue.offer(nextVertexId);
                    reSet.add(nextVertexId);
                }
            }
        }
        return reSet;
    }

    public int getEdgeNum(){
        return this.edgeNum / 2;
    }
}
