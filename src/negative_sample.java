import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;

public class negative_sample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String trainSet_file = "C:/Users/hxf/eclipse-workspace/paper1_preprocess/douban_beijing_2017_csv/model_input_trainset_efx1000_beijing.txt";
		HashMap<String, ArrayList<String>> trainSet = new HashMap<String, ArrayList<String>>();
		trainSet = getTrainTestSet(trainSet_file);
		System.out.println(trainSet.size());
		
		String testSet_file = "C:/Users/hxf/eclipse-workspace/paper1_preprocess/douban_beijing_2017_csv/model_input_testset_efx1000_beijing.txt";
		HashMap<String, ArrayList<String>> testSet = new HashMap<String, ArrayList<String>>();
		testSet = getTrainTestSet(testSet_file);
		System.out.println(testSet.size());
		
		String outfile = "./fengresult/beijing/negativeSample.txt";
		get_negative_sample(outfile, trainSet, testSet);

	}
	
	
	public static void get_negative_sample (String outfile, HashMap<String, ArrayList<String>> trainSet, HashMap<String, ArrayList<String>> testSet){
		
		
		ArrayList<String> eventSetAll = new ArrayList<String>();
		ArrayList<String> userSet = new ArrayList<String>();
		
//		int count = 0;
//		for (String uid1 : trainSet.keySet()) {
//			System.out.println("first------"+count);
//			for (String uid2 : testSet.keySet()) {
//				//train all event
//				for (int i = 0; i < trainSet.get(uid1).size(); i ++) {
//					
//					String eid1 = trainSet.get(uid1).get(i);
//					if (!eventSetAll.contains(eid1)) {
//						eventSetAll.add(eid1);
//					}
//				}
//				//test all event
//				for (int i = 0; i < testSet.get(uid2).size(); i ++) {
//					
//					String eid2 = testSet.get(uid2).get(i);
//					if (!eventSetAll.contains(eid2)) {
//						eventSetAll.add(eid2);
//					}
//				}
//			}
//			if (!userSet.contains(uid1)) {
//				userSet.add(uid1);
//			}
//			count ++;
//		}
		
		ArrayList<String> eventSetTrain = new ArrayList<String>();
		for (String uid1 : trainSet.keySet()) {
			for (int i = 0; i < trainSet.get(uid1).size(); i ++) {
				if (!eventSetAll.contains(trainSet.get(uid1).get(i))) {
					eventSetAll.add(trainSet.get(uid1).get(i));
				}
			}
			if (!userSet.contains(uid1)) {
				userSet.add(uid1);
			}
		}
		System.out.println("eventSetAll.size()="+eventSetAll.size());
		
		ArrayList<String> eventSetTest = new ArrayList<String>();
		for (String uid2 : testSet.keySet()) {
			for (int i = 0; i < testSet.get(uid2).size(); i ++) {
				if (!eventSetTest.contains(testSet.get(uid2).get(i))) {
					eventSetTest.add(testSet.get(uid2).get(i));
				}
			}
		}
		System.out.println("eventSetTest.size()="+eventSetTest.size());
		
		for (int j = 0; j < eventSetTest.size(); j ++) {
			if (!eventSetAll.contains(eventSetTest.get(j))) {
				eventSetAll.add(eventSetTest.get(j));
			}	
		}
		System.out.println("eventSetAll.size()="+eventSetAll.size());

		
		HashMap<String, ArrayList<String>> userNegativeSample = new HashMap<String, ArrayList<String>>();
		for (String uid : userSet) {
//			System.out.println("second------"+count2);
			
			int[] random = generateRandomArray(100, 0, 99);
			ArrayList<String> negativeSample = new ArrayList<String>();
			for (String eid : eventSetAll) {
				if (!trainSet.get(uid).contains(eid) & !testSet.get(uid).contains(eid)) {
					negativeSample.add(eid);
				}
			}
			
			String res = uid + ":" + random.length + ":";
			for (int j = 0; j < random.length; j ++) {
				res = res + negativeSample.get(random[j]) + ",";
			}
			res = res + "\n";
			appendMethodA(outfile, res);
			
			
			
//			userNegativeSample.put(uid, negativeSample);
		}
		
//		for (String uid : userNegativeSample.keySet()) {
//			
//			int[] random = generateRandomArray(100, 0, 99);
//			
//			String res = uid + ":" + random.length + ":";
//			for (int j = 0; j < random.length; j ++) {
//				res = res + userNegativeSample.get(uid).get(random[j]) + ",";
//			}
//			res = res + "\n";
//			appendMethodA(outfile, res);
//		}
		
		
		
		
	}
	
	public static int[] generateRandomArray(int size, int min, int max) {
		Random random = new Random();
		int[] arr = random.ints(size, min, max + 1).toArray();
		return arr;
	}
	
	public static  HashMap<String, ArrayList<String>> getTrainTestSet (String infile){

		HashMap<String, ArrayList<String>> TrainTestSet = new HashMap<String, ArrayList<String>>();
		
		File file = new File(infile);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			//double[] featurevec = null;
			while ((line = reader.readLine()) != null) {// 
				String[] temp = line.split("::");
				
				String userid = temp[0];//
				ArrayList<String> eventSet = new ArrayList<String>();
				for (int i = 2; i < temp.length; i++) {
					
					String[] temp2 = temp[i].split(",");
					eventSet.add(temp2[0]);
				}
				TrainTestSet.put(userid, eventSet);
				
			}
			reader.close();	
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return TrainTestSet;
	}
	
	public static void appendMethodA(String fileName, String content) {
		try 
		{
			File file = new File(fileName);
			File fileParent = file.getParentFile();
			if (!fileParent.exists()) 
			{
				fileParent.mkdirs();
			}
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	

}
