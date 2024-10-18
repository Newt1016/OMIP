import java.io.FileWriter;
import java.io.IOException;

public class Model {
	
	public double[][] phiZH;
	public double[][] phiZW;
	public double[][] phiRV;
	public double[][] phiZU;
	public double[][][] phiZRU;
	public double[][] phiRU;
	public double[][] phiUZ;
	public double[][] phiUR;
	public double[][] phiCU;
	public double[][] phiCV;
	public double[][] phiCH;
	public double[][] phiZE;
	public double[][] phiRE;
	public double[][] theta;
	public double[][] phi;
	
	
	protected void writeFile(double[][] phi, String fileName){
		try{
		FileWriter fw = new FileWriter(fileName);
		for(int i=0;i<phi.length;i++){
			for(int j=0;j<phi[i].length;j++)
				fw.write(phi[i][j]+" ");
			fw.write("\n");
		}
		fw.close();
		}catch (IOException e) {
            e.printStackTrace();
		}
	}
	
//	public void saveParam(){
//		writeFile(phiZH,"phiZH.txt");
//		writeFile(phiZW,"phiZW.txt");
//		writeFile(phiZU,"phiZU.txt");
//		writeFile(phiZE,"phiZE.txt");
//		writeFile(phiRU,"phiRU.txt");
//		writeFile(phiRE,"phiRE.txt");
//		
//	}
	
}
