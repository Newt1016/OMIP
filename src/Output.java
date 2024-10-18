import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Output {
	public static void savePerplexity(String file, double[] perp){
		try{
			FileWriter fw = new FileWriter(file);
			for(double p : perp)
				fw.write(p+"\n");
			fw.close();
			}catch (IOException e) {
	            e.printStackTrace();
		}
	}
	
	public static void printTopic(String file, double[][] phi, int topn){
		try{
			FileWriter fw = new FileWriter(file);
		int rows = phi.length;
		for(int i = 0; i < rows; i++){
			int[] index = ArrayUtils.argsort(phi[i],false);
			for(int j = 0; j < topn && j < index.length; j++){
				fw.write(index[j]+" ");
			}
			fw.write("\n");
		}
		fw.close();
		}catch (IOException e) {
            e.printStackTrace();
		}
	}
	
	public static void saveMap(String file, Map<String,Integer> map){
		Map<Integer,String> invmap = new HashMap<>();
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			invmap.put(entry.getValue(), entry.getKey());
		}
		try{
			FileWriter fw = new FileWriter(file);
			for(int id = 0; id < invmap.size(); id++){
				fw.write(id + "," + invmap.get(id) + "\n");
			}
			fw.close();
		}catch (IOException e) {
            e.printStackTrace();
		}
	}
}
