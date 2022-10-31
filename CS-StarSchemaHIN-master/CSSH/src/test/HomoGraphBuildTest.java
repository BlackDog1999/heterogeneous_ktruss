package test;

import bean.MetaPath;
import csh.HomoGraphBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class HomoGraphBuildTest {

    private HomoGraphBuilder homoGraphBuilder = null;

    private String root = "..\\new_data\\foursquare\\";

    private int graph[][];

    private Map<Integer, int[]> homoGraph;


    public HomoGraphBuildTest(int vertex[], int edge[]) {

        readGraph();
        MetaPath metaPath = getMetaPath(vertex, edge);

        ArrayList<Integer> vType = new ArrayList<Integer>();
        ArrayList<Integer> eType = new ArrayList<Integer>();
        try {
            BufferedReader vReader = new BufferedReader(new FileReader(root + "vertex.txt"));
            BufferedReader eReader = new BufferedReader(new FileReader(root + "edge.txt"));
            String line = null;
            while ((line = vReader.readLine()) != null) {
                String s[] = line.split(" ");
                vType.add(Integer.parseInt(s[1]));
            }
            line = null;
            while ((line = eReader.readLine()) != null) {
                String s[] = line.split(" ");
                eType.add(Integer.parseInt(s[1]));
            }
            vReader.close();
            eReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.homoGraphBuilder = new HomoGraphBuilder(this.graph, toArray(vType), toArray(eType), metaPath);
    }

    private int[] toArray(List<Integer> l) {
        int ans[] = new int[l.size()];
        for(int i = 0;i < l.size();i++) {
            ans[i] = l.get(i);
        }
        return ans;
    }

    private int[][] toDoubleArray(List<int[]> l) {
        int ans[][] = new int[l.size()][];
        for(int i = 0;i < l.size();i++) {
            ans[i] = new int[l.get(i).length];
            for (int j = 0;j < l.get(i).length;j++) {
                ans[i][j] = l.get(i)[j];
            }
        }
        return ans;
    }

    public boolean readGraph() {
        ArrayList<int[]> graph = new ArrayList<int[]>();
        try {
            BufferedReader stdin = new BufferedReader(new FileReader(this.root + "graph.txt"));
            String line = null;
            while ((line = stdin.readLine()) != null) {
                String s[] = line.split(" ");
                int t[] = new int[s.length-1];
                for(int i = 1;i < s.length;i++) {
                    t[i-1] = Integer.parseInt(s[i]);
                }
                graph.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        this.graph = toDoubleArray(graph);
        return true;
    }

    public MetaPath getMetaPath(int vertex[], int edge[]) {
        return new MetaPath(vertex, edge);
    }

    public void transform() {
        this.homoGraph = this.homoGraphBuilder.build();
    }

    public boolean saveHomoGraph(String path) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
            for (int key : homoGraph.keySet()) {
                String line = "";
                for (int neighbour : homoGraph.get(key)) {
                    if (key < neighbour) {
                        line += Integer.toString(key) + " ";
                        line += Integer.toString(neighbour) + "\n";
                    }
                }
                bufferedWriter.write(line);
            }
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Map<Integer, int[]> getHomoGraph() {
        return this.homoGraph;
    }


    public static void main(String[] args) {
        int vertex[] = new int[]{1, 0, 2, 0, 1};
        int edge[] = new int[]{1, 2, 3, 0};

        HomoGraphBuildTest hgmt = new HomoGraphBuildTest(vertex, edge);
        hgmt.transform();
        hgmt.saveHomoGraph("D:\\CS-StarSchemaHIN-master (2)\\CSSH\\src\\test\\homo_graph\\foursquare_graph.txt");



    }


}
