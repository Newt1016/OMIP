import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dataset {
	Map<String,Integer> umap;
	Map<String,Integer> emap;
	Map<String,Integer> omap;
	Map<String,Integer> vmap;
	Map<String,Integer> catmap;

	int[] org;
	int[] venue;
	int[][] corpus;
	int[][] groups;
	Map<Integer,List<Integer>> utrain;
	Map<Integer,List<Integer>> gtrain;
	int[][] train;
	Map<Integer,List<Integer>> testset;
	
	
	public Dataset(){
		readEvent(Input.eventfile);
		readTrainTest(Input.trainfile,Input.testfile);
		readGroups(Input.groupfile);
		getOrgVenue(Input.eventfile);
		getGtrain();
		readCategory(Input.corpusfile);
	}
	
//	public void readTest(String filename){
//		testset = new HashMap<Integer,List<Integer>>
//	}
	
	public void getGtrain(){
		gtrain = new HashMap<Integer,List<Integer>>();
		for(int gid = 0; gid<groups.length;gid++){
			int[] users = groups[gid];
			Set<Integer> set = new HashSet<Integer>(emap.values());
			for(int uid : users){
				if(!utrain.containsKey(uid)) set.clear();
				else set.retainAll(utrain.get(uid));
			}
			gtrain.put(gid, new ArrayList<Integer>(set));
		}
		List<int[]> data = new ArrayList<int[]>();
		for(Map.Entry<Integer, List<Integer>> entry : gtrain.entrySet()){
			if(entry.getValue().isEmpty())continue;
			for(int eid : entry.getValue()){
				int[] array = new int[2];
				array[0] = entry.getKey();
				array[1] = eid;
				data.add(array);
			}
			
		}
		train = data.toArray(new int[][] {});
	}
	
	public void readGroups(String filename){
		File file = new File(filename);
		BufferedReader reader = null;
		List<int[]> data = new ArrayList<int[]>();

		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
//            int count = 0;
            while ((line = reader.readLine()) != null) {
//            	if(count++==0) continue;
            	String[] temp = line.split(",");
            	int[] members = new int[temp.length];
            	for(int i=0;i<temp.length;i++)
            		members[i] = umap.get(temp[i]);
            	data.add(members);
            }
            reader.close();
            groups = data.toArray(new int[][] {});
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	

	public void readCategory(String filename){
		File file = new File(filename);
		BufferedReader reader = null;
		catmap = new HashMap<String,Integer>();
		corpus = new int[emap.size()][];
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
            	if(count++==0) continue;
            	String[] temp = line.split(",");
            	for(int i=1;i<temp.length;i++)
            	if(!catmap.containsKey(temp[i]))
            	catmap.put(temp[i], catmap.size());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
            	if(count++==0) continue;
            	String[] temp = line.split(",");
            	int[] words = new int[temp.length-1];
            	for(int i=1;i<temp.length;i++)
            		words[i-1] = catmap.get(temp[i]);
            	corpus[emap.get(temp[0])] = words;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}
	
	public void readEvent(String filename){
		File file = new File(filename);
		emap = new HashMap<String,Integer>();
		omap = new HashMap<String,Integer>();
		vmap = new HashMap<String,Integer>();
		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
            	if(count++==0) continue;
            	String[] temp = line.split(",");
            	emap.put(temp[0], emap.size());
            	if(!vmap.containsKey(temp[2]))
            	vmap.put(temp[2], vmap.size());
            	if(!omap.containsKey(temp[3]));
            	omap.put(temp[3], omap.size());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void getOrgVenue(String filename){
		File file = new File(filename);
		org = new int[emap.size()];
		venue = new int[emap.size()];
		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
            	if(count++==0) continue;
            	String[] temp = line.split(",");
//            	if(!omap.containsKey(temp[3])) System.out.println(temp[3]);
            	org[emap.get(temp[0])] = omap.get(temp[3]);
            	venue[emap.get(temp[0])] = vmap.get(temp[2]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void readTrainTest(String trainfile, String testfile){
		umap = new HashMap<String, Integer>();
		File file = new File(trainfile);
		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
            	if(count++==0) continue;
            	String[] temp = line.split(",");
            	if(!umap.containsKey(temp[0]))
            	umap.put(temp[0],umap.size());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = new File(testfile);
//		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
            	if(count++==0) continue;
            	String[] temp = line.split(",");
            	if(!umap.containsKey(temp[0]))
            	umap.put(temp[0],umap.size());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = new File(trainfile);
        utrain = new HashMap<Integer,List<Integer>>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
            	if(count++==0) continue;
            	String[] temp = line.split(",");
            	int uid = umap.get(temp[0]);
//            	if(!emap.containsKey(temp[1])) System.out.println(temp[1]);
            	int eid = emap.get(temp[1]);
            	if(utrain.containsKey(uid)){
            		utrain.get(uid).add(eid);
            	}else{
            		List<Integer> list = new ArrayList<Integer>();
            		list.add(eid);
            		utrain.put(uid, list);
            	}
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = new File(testfile);
        testset = new HashMap<Integer,List<Integer>>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
            	if(count++==0) continue;
            	String[] temp = line.split(",");
            	int uid = umap.get(temp[0]);
            	if(!emap.containsKey(temp[1])) 
            		System.out.println(temp[1]);
            	int eid = emap.get(temp[1]);
            	if(testset.containsKey(uid)){
            		testset.get(uid).add(eid);
            	}else{
            		List<Integer> list = new ArrayList<Integer>();
            		list.add(eid);
            		testset.put(uid, list);
            	}
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	public static void main(String[] args) {
		Dataset ds = new Dataset();
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < ds.corpus[i].length; j++)
				System.out.println(ds.corpus[i][j]);
//		System.out.println(ds.umap.size());
//		System.out.println(ds.emap);
//		System.out.println(ds.catmap);
//		System.out.println(ds.catmap.get("party"));
//		for(int i : ds.category)
//			System.out.println(i);
//		System.out.println(ds.omap.size());
//		System.out.println(ds.vmap.size());
//		System.out.println(ds.groups.length);
		
//		ds.utrain.get(52).retainAll(ds.utrain.get(116));
//		System.out.println(ds.utrain.get(52));
//		System.out.println(ds.gtrain);
//		System.out.println(ds.train.length);
	}
}
