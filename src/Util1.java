
public class Util1 {
	public static double[] add(double[] x, double[] y){
		if(x.length!=y.length){
			System.out.println("The dimensions of two array must be equal!");
			return null;
		}
		double[] z = new double[x.length];
		for(int i=0;i<x.length;i++){
			z[i] = x[i]+y[i];
		}
		return z;
	}
	public static int draw(double[] a){
		double r = Math.random();
		for(int i = 0; i<a.length;i++){
			r = r - a[i];
			if(r<0) return i;
		}
		return a.length-1;
	}
	
	public static void addIn(double[][] x, double[][] y){//���ǽ����������е�Ԫ�ض�Ӧ��Ӳ���������һ��������
		if(x.length!=y.length||x[0].length!=y[0].length){
			System.out.println("The dimensions of two matrix must be equal!");
		}
		int i,j;
		for(i=0;i<x.length;i++){
			for(j=0;j<x[i].length;j++)
				x[i][j] += y[i][j];
		}
	}
	
	public static double[][] add(double[][] x, double[][] y){
		if(x.length!=y.length||x[0].length!=y[0].length){
			System.out.println("The dimensions of two matrix must be equal!");
			return null;
		}
		double[][] z = new double[x.length][x[0].length];
		for(int i=0;i<x.length;i++)
			for(int j=0;j<x.length;j++)
				z[i][j] = x[i][j]+y[i][j];
		return z;
	}
	
	public static double[] sub(double[] x,double[] y){
		if(x.length!=y.length){
			System.out.println("The dimensions of two array must be equal!");
			return null;
		}
		double[] z = new double[x.length];
		for(int i=0;i<x.length;i++){
			z[i] = x[i]-y[i];
		}
		return z;
	}
	public static double[] divid(double[] x, double a){//������ÿ��Ԫ�ض���ȥһ��ֵ
		double[] z = new double[x.length];
		for(int i=0;i<x.length;i++)
			z[i] = x[i]/a;
		return z;
	}
	
	
	public static void divid(double[][] x, double a){
		int i,j;
		for(i=0;i<x.length;i++){
			for(j=0;j<x[i].length;j++){
				x[i][j]/=a;
			}
		}
	}
	
	public static double[][] outer(double[] x, double[] y){
		double[][] z = new double[x.length][y.length];
		for(int i=0;i<x.length;i++){
			for(int j=0;j<y.length;j++)
				z[i][j] = x[i]*y[j];
		}
		return z;
	}
	
	public static void print(double[] x){
		for(double i:x)
			System.out.print(i+" ");
		System.out.println();
	}
	
	public static void print(int[] x){
		for(int i:x)
			System.out.print(i+" ");
		System.out.println();
	}
	
	public static void print(double[][] x){
		for(int i=0;i<x.length;i++){
			for(int j=0;j<x[i].length;j++){
				System.out.print(x[i][j]+" ");
			}
			System.out.println();
		}
		
	}
	
	public static void print(int[][] x){
		for(int i=0;i<x.length;i++){
			for(int j=0;j<x[i].length;j++){
				System.out.print(x[i][j]+" ");
			}
			System.out.println();
		}
		
	}
	
	public static double[] ravel(double[][] x){
		int row = x.length;
		int col = x[0].length;
		double[] z = new double[row*col];
		for(int i=0;i<x.length;i++){
			for(int j=0;j<x[i].length;j++){
				z[col*i+j] = x[i][j];
			}
		}
		return z;
	}
	
	public static int[] unravel_index(int index, int r, int c){
		if(index>r*c)
			return null;
		int[] z = new int[2];
		z[0] = index/(c);
		z[1] = index - c*z[0];
		return z;
	}
	
	public static double[][] multiply(double[][] A,double n){
		double[][] Z = new double[A.length][A[0].length];
		for(int i=0;i<A.length;i++)
			for(int j=0;j<A[0].length;j++)
				Z[i][j] = A[i][j]*n;
		return Z;
	}
	
	public static double[] multiply(double[] a, double[][] B){
		if(a.length!=B.length){
			System.out.println("deminsion error");
			return null;
		}
		double[] C = new double[B[0].length];
		for(int j=0;j<C.length;j++)
			for(int i=0;i<a.length;i++)
				C[j] += a[i]*B[i][j];
		return C;
	}
	
	public static double[][] trans(double[] a){
		double[][] A = new double[a.length][1];
		for(int i=0;i<a.length;i++)
			A[i][0] = a[i];
		return A;
	}
	public static double[][] multiply(double[][] a, double[][] b){
		
		if(a[0].length!=b.length){
			System.out.println("dimension error!");
			return null;
		}
		int R = a.length;
		int K = a[0].length;
		int C = b[0].length;
		double[][] z = new double[R][C];
		for(int i=0;i<R;i++)
			for(int j=0;j<C;j++){
				double sum = 0;
				for(int k=0;k<K;k++){
					sum += a[i][k]*b[k][j];
				}
				z[i][j] = sum;
			}
		return z;
	}
	
	public static double[][] trans(double[][] A){
		int r = A.length;
		int c = A[0].length;
		double[][] M = new double[c][r];
		for(int i=0;i<c;i++){
			for(int j=0;j<r;j++){
				M[i][j] = A[j][i];
			}
		}
		return M;
	}
	

	
	 public static boolean equal(int[] a, int[] b){
		 if(a.length!=b.length)
			 return false;
		 int c = 0;
		 for(int i=0;i<a.length;i++){
			 if(a[i]==b[i]){
				 c++;
			 }
		 }
		 return c==a.length;
	 }
	 
	 
	 public static void norm(double[] a){
		 double sum =0;
		 for(int i=0;i<a.length;i++){
			 sum += a[i];
		 }
		 for(int i=0;i<a.length;i++){
			 a[i] = a[i]/sum;
		 }
	 }
	 
	 public static double log2(double a){
			return Math.log(a)/Math.log(2);
		}
	 
	public static void main(String[] args) {
		double[] mu = {1,1};
		double[] point = {1,0.5};
		double[][] sigma = {{1,1},{1,1}};
		int[] b = {1,2,3};
		int[] c = {1,2,4};
		System.out.println(equal(b,c));
//		System.out.println(pdfNorm(point,mu,sigma));
//		Matrix m = new Matrix(b);
//		Matrix in = m.inverse();
//		System.out.println(in.getArray());
//		double[][] c = multiply(b,trans(a));
//		print(c);
//		RealMatrix M = MatrixUtils.createRealMatrix(b);
//		MatrixUtils.inverse(M,0.1);
//		M.multiplyEntry(0,0,5);
//		print(M.getData());
//		int[] z = unravel_index(6,3,2);
//		System.out.println(z[0]);
//		System.out.println(z[1]);
	}
}
