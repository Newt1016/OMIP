import java.io.FileWriter;
import java.io.IOException;


public class fengmodel {
	public double[][] phiUZ;
	public double[][] phiUI;
	public double[][][] phiUZI;
	public double[][] phiZI;
	public double[][] phiZW;
	public double[][] phiZC;
	public double[][] phiZO;
	//public double[][] phiZY;
	public double[][] phiIO;
	public double[][] phiIX;
	public double[][] phiIY;
	public double[] lambdaU;

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
}