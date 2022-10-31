package test;

import bean.MetaPath;
import tool.ComputeBCoreFast;
import util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class FastnmcTest {

    private int graph[][] = null;
    private int vertexType[] = null;
    private int edgeType[] = null;
    private HashMap<Integer, String> id2NameDict = null;
    private HashMap<String, Integer> name2IdDict = null;

    public FastnmcTest() {
        readDict();
    }

    private void readDict() {
        id2NameDict = new HashMap<>();
        name2IdDict = new HashMap<>();
        try {
            BufferedReader stdin = new BufferedReader(new FileReader("..\\new_data\\city\\poi_id_to_name_dict.txt"));

            String line = null;
            while ((line = stdin.readLine()) != null) {
                String s[] = line.split("_");
                int poiId = Integer.parseInt(s[0]);
                String poiName = s[1];
                if (s.length > 2) {
                    for (int i = 2; i < s.length; i++) {
                        poiName += "——";
                        poiName += s[i];
                    }
                }
//                System.out.println(poiId);
//                System.out.println(poiName);
                id2NameDict.put(poiId, poiName);
                name2IdDict.put(poiName, poiId);
            }
            stdin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return poiId2NameDict;
    }

    private ArrayList<String> idList2NameList(HashMap<Integer, String> dict, ArrayList<Integer> idList) {
        ArrayList<String> nameList = new ArrayList<>();
        for (int id : idList) {
            nameList.add(dict.get(id));
        }
        return nameList;
    }

    private void prettyPrintList(ArrayList<String> inputList) {
        for (String s : inputList) {
            System.out.println(s);
        }
        System.out.println(String.format("Size is %d", inputList.size()));
    }

    public void onlineExp() {
        Config config = new Config();
        int[][] schemaGraph = config.getSchema("city");
        DataReader dataReader = new DataReader(Config.newCityGraph, Config.newCityVertex, Config.newCityEdge);

        int graph[][] = dataReader.readGraph();
        int vertexType[] = dataReader.readVertexType();
        int edgeType[] = dataReader.readEdgeType();

//        String queryName = "星巴克——华宇广场"; // 为咖啡厅，但社区包括西式快餐？
//        String queryName = "阿迪达斯——华达商城"; // 运动户外，但社区包括服装？内衣？
//        String queryName = "赛百味SUBWAY——来福士广场";
//        int queryType = 0;

        String queryName = "报喜鸟";
        int queryType = 1;

        int queryK = 50;

        ArrayList<Integer> queryIdList = new ArrayList<Integer>();
        queryIdList.add(name2IdDict.get(queryName));

        ArrayList<MetaPath> tempMetaPathList = new ArrayList<MetaPath>();
//        tempMetaPathList.add(new MetaPath("0 0 1 1 0")); // POI - 品牌 - POI
//        tempMetaPathList.add(new MetaPath("0 2 2 3 0")); // POI - 商场 - POI
//        tempMetaPathList.add(new MetaPath("0 4 3 5 0")); // POI - 区域 - POI
//        tempMetaPathList.add(new MetaPath("0 6 4 7 0")); // POI -  类型 - POI

//        tempMetaPathList.add(new MetaPath("1 8 2 9 1")); // 品牌 - 商场 - 品牌
//        tempMetaPathList.add(new MetaPath("1 10 3 11 1")); // 品牌 - 区域 - 品牌
        tempMetaPathList.add(new MetaPath("1 12 4 13 1")); // 品牌 - 类型 - 品牌
        // 转同构图的时候，规定有两条meta-path instance才有边？
        // 在筛选过后的community中，根据用户信息再寻找小community？

//        tempMetaPathList.add(new MetaPath("0 0 1 12 4 13 1 1 0")); // POI - 品牌 -  类型 - 品牌 - POI

        // 打印查询的输入
        System.out.println("\nQuery input and parameter: ");
        System.out.println(String.format("Query set is: %s", idList2NameList(id2NameDict, queryIdList).toString()));
        System.out.println(String.format("Query K is: %d", queryK));
//        System.out.println("Meta-path set is: ");

        // get the vertex set of the HIN
        Set<Integer> initSet = new HashSet<Integer>();
        for (int j = 0; j < graph.length; j++) {
            if (vertexType[j] == queryType) {
                initSet.add(j);
            }
        }
        ComputeBCoreFast computeBCoreFast = new ComputeBCoreFast(graph, vertexType, edgeType);
        Set<Integer> communityIDSet = computeBCoreFast.getBasicKPCore(initSet, queryIdList, tempMetaPathList, queryK);

        if (communityIDSet != null) {
            System.out.println("\nCommunity search result: ");
            ArrayList<String> communityNameList = idList2NameList(id2NameDict, new ArrayList<Integer>(communityIDSet));
            prettyPrintList(communityNameList);
        } else {
            System.out.println("community is null");
        }
    }

    public static void main(String[] args) {
        FastnmcTest test = new FastnmcTest();
        test.onlineExp();
    }

}
