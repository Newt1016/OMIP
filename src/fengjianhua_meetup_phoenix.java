import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.distribution.BinomialDistribution;



public class fengjianhua_meetup_phoenix {

	int K = 60;//
	int I = 50;//
	int iterNum = 1000;//
	//int user_num, event_num, venue_num, org_num, word_num;
	int user_num,ori_num,eventcate_num,oricate_num,X_num,Y_num,event_num;
	int U=user_num;
	int O=ori_num;
	int W=eventcate_num;
	int C=oricate_num;
	int X=X_num;
	int Y=Y_num;
	int E=event_num;
	
	double alpha=50.0/K;//
	double beta=50/I;//
	double delta=0.01;

	//
	double kappa=0.01;
	double rho=0.01;
	double nu=0.01;
	double gamma=0.01;
	double epsilon=0.01;
	double pi=0.01;
	double tau1=0.5;
	double tau0=1-tau1;
	
	Map<Integer,List<String>> trainset;
	Map<Integer,List<String>> testset;
	Map<Integer,String> eventduizhao;//
	

	
	int[][] Iu;//
	int[][] Z;//
	int[][] S;
	
	int[][] nuz;
	//int[][] nui;
	int[][] nzi;
	int[][] nzw;
	int[][] nzc;
	int[][] nzo;
	int[][] nix;
	int[][] niy;
	int[][] nio;
	int[][] nus;
	
	int[] nuzsum;
	//int[] nuisum;
	int[] nzisum;
	int[] nzwsum;
	int[] nzcsum;
	int[] nzosum;
	int[] nixsum;
	int[] niysum;
	int[] niosum;
	int[] nussum;
	
	double[] pz;//
	double[] pI;//«·
	
	double[][] puz;
	//double[][] pui;
	double[][] pzi;
	double[][] pzw;
	double[][] pzc;
	double[][] pzo;
	double[][] pio;
	double[][] pix;
	double[][] piy;
	
	fengDataset_meetup_phoenix ds;
	
	public fengjianhua_meetup_phoenix(int K,int I)
	{
		this.K = K;
		this.I = I;
		ds= new fengDataset_meetup_phoenix();
		//utrain=ds.utrain;
		trainset=ds.trainset;
		testset=ds.testset;
		eventduizhao=ds.eventduizhao;
		
		//trainofcate=ds.trainofcate;
		//utrainofseq=ds.utrainofseq;
//		seqmap=ds.seqmap;
//		seqduizhao=ds.seqduizhao;
//		user2seqs=ds.user2seqs;
		
		U=ds.umap.size();
		O=ds.omap.size();
		W=ds.wmap.size();
//		C=ds.cmap.size();
		C=1;
//		X=101;
//		Y=101;
		X=1001;
		Y=1001;
		
//		C=ds.cmap.size();
//		X=ds.xmap.size();
//		Y=ds.ymap.size();
		E=ds.emap.size();
//		U=user_num;
//		C=cate_num;
//		SE=seq_num;
		user_num = ds.umap.size();
		
		
		
		Z = new int[U][];
		Iu = new int[U][];
		S = new int[U][];
		
		
		nuz=new int[U][K];
		nzi=new int[K][I];
		nzw=new int[K][W];
		nzc=new int[K][C];
		nzo=new int[K][O];
		nix=new int[I][X];
		niy=new int[I][Y];
		nio=new int[I][O];
		nus=new int[U][2];
		
//		nuz = new int[U][K];
//		//nuy = new int[U][Y];
//		nzc = new int[K][cate_num];
//		nyc = new int[Y][cate_num];
//		nyL = new int[Y][seq_num];
//		nus = new int[U][2];
//		//nsc = new int[2][cate_num];
//		nzy = new int[K][Y];
		
		
		nuzsum = new int[U];
		nzisum = new int[K];
		nzwsum = new int[K];
		nzcsum = new int[K];
		nzosum = new int[K];
		nixsum = new int[I];
		niysum = new int[I];
		niosum = new int[I];
		nussum = new int[U];
		
		
//		nuzsum = new int[U];
//		nzcsum = new int[K];
//		nycsum = new int[Y];
//		nzysum = new int[K];
//		//nscsum = new int[2];
//		nyLsum = new int[Y];
//		nussum = new int[U];
		
		
		double[] pz = new double[K];//
		double[] pI = new double[I];//
		
//		pz = new double[K];
//		py = new double[Y];
		
		
		puz = new double[U][K];
		pzi = new double[K][I];
//		pui = new double[U][I];
		pzw = new double[K][W];
		pzc = new double[K][C];
		pzo = new double[K][O];
		pio = new double[I][O];
		pix = new double[I][X];
		piy = new double[I][Y];
		
		
//		puz = new double[U][K];
//		puy = new double[U][Y];
//		pzc = new double[K][cate_num];
//		pyc = new double[Y][cate_num];
//		pzy = new double[K][Y];

	}
	public void initialize()
	{
		System.out.println("initializing model...");
		int u,z,i,w,c,o,x,y,s;
		Random rand = new Random();
		int flag=0;
		for(Map.Entry<Integer, List<String>> entry : trainset.entrySet()){
			if(entry.getValue().isEmpty())continue;
			int j=0;
			u=entry.getKey();//
			flag=u;//
			//System.out.println(u);
			//System.out.println(flag);
			int usize=entry.getValue().size();
			Z[flag]=new int[usize];
			Iu[flag]=new int[usize];
			S[flag]=new int[usize];
			
			for(j=0;j<entry.getValue().size();j++)
			{
//				l=entry.getValue().get(i);//
//				c=Integer.parseInt(seqduizhao.get(l).split(",")[seqduizhao.get(l).split(",").length-1]);//
				String res=entry.getValue().get(j);
				String[] ss=res.split(",");
				w=Integer.parseInt(ss[1]);//
				o=Integer.parseInt(ss[3]);//
				c=0;//

				x=Integer.parseInt(ss[5]);//
				y=Integer.parseInt(ss[6]);//
				
				z = rand.nextInt(K);
				i = rand.nextInt(I);
				s = rand.nextInt(2);

				Z[flag][j]=z;
				Iu[flag][j]=i;
				S[flag][j]=s;
				
				
				nuz[flag][z]++;
				nuzsum[flag]++;
				
				nzi[z][i]++;
				nzisum[z]++;
				
				nzw[z][w]++;
				nzwsum[z]++;
				
				nzc[z][c]++;
				nzcsum[z]++;
				
				nzo[z][o]++;
				nzosum[z]++;
				
				nix[i][x]++;
				nixsum[i]++;
				
				niy[i][y]++;
				niysum[i]++;
				
				nio[i][o]++;
				niosum[i]++;
				
				nus[flag][s]++;
				nussum[flag]++;
				
			}
			flag++;
		}
	}
	
	public void gibbs1()
	{
		//System.out.println("gibbs1");
		int u,z,i,w,c,o,x,y,s;
		int flag=0;
		for(Map.Entry<Integer, List<String>> entry : trainset.entrySet()){
			if(entry.getValue().isEmpty())continue;
			int j=0;
			u=entry.getKey();//
			//System.out.println(u);
			//System.out.println(flag);
			flag=u;//
			int usize=entry.getValue().size();
			for(j=0;j<usize;j++)
			{
				//double[][] p=new double[K][Y];
				double[] pk =new double[K];
				double[] pI = new double[I];

				String res=entry.getValue().get(j);
				String[] ss=res.split(",");
				w=Integer.parseInt(ss[1]);//
				o=Integer.parseInt(ss[3]);//
				c=0;
				x=Integer.parseInt(ss[5]);//
				y=Integer.parseInt(ss[6]);//
				
				z = Z[flag][j];
				i = Iu[flag][j];
				s = S[flag][j];
				nuz[flag][z]--;
				nuzsum[flag]--;
				
				nzi[z][i]--;
				nzisum[z]--;
				
				nzw[z][w]--;
				nzwsum[z]--;
				
				nzc[z][c]--;
				nzcsum[z]--;
				
				nzo[z][o]--;
				nzosum[z]--;
				
				nix[i][x]--;
				nixsum[i]--;
				
				niy[i][y]--;
				niysum[i]--;
				
				nio[i][o]--;
				niosum[i]--;
				
				
				int k,yy;
				
				
				
				if(s==1)
				{
					for(k=0;k<K;k++)
					{
						pk[k]=(nuz[flag][k]+alpha)/(nuzsum[flag]+K*alpha)*(nzc[k][c]+rho)/(nzcsum[k]+C*rho)*(nzw[k][w]+kappa)/(nzwsum[k]+W*kappa)*(nzo[k][o]+nu)/(nzosum[k]+O*nu)*(nzi[k][i]+beta)/(nzisum[k]+I*beta);
					}
					
					
					for(yy=0;yy<I;yy++)
					{
						//pI[yy]=(nzi[z][yy]+beta)/(nzisum[z]+I*beta)*(nix[yy][x]+pi)/(nixsum[yy]+X*pi)*(niy[yy][y]+epsilon)/(niysum[yy]+Y*epsilon);
						pI[yy]=(nuz[flag][z]+alpha)/(nuzsum[flag]+K*alpha)*(nzi[z][yy]+beta)/(nzisum[z]+I*beta)*(nix[yy][x]+pi)/(nixsum[yy]+X*pi)*(niy[yy][y]+epsilon)/(niysum[yy]+Y*epsilon);
					}
				}
				else
				{
					for(k=0;k<K;k++)
					{
						pk[k]=(nuz[flag][k]+alpha)/(nuzsum[flag]+K*alpha)*(nzc[k][c]+rho)/(nzcsum[k]+C*rho)*(nzw[k][w]+kappa)/(nzwsum[k]+W*kappa)*(nzi[k][i]+beta)/(nzisum[k]+I*beta);
					}
					for(yy=0;yy<I;yy++)
					{
						//pI[yy]=(nzi[z][yy]+beta)/(nzisum[z]+I*beta)*(nix[yy][x]+pi)/(nixsum[yy]+X*pi)*(niy[yy][y]+epsilon)/(niysum[yy]+Y*epsilon)*(nio[yy][o]+gamma)/(niosum[yy]+O*gamma);
						pI[yy]=(nuz[flag][z]+alpha)/(nuzsum[flag]+K*alpha)*(nzi[z][yy]+beta)/(nzisum[z]+I*beta)*(nix[yy][x]+pi)/(nixsum[yy]+X*pi)*(niy[yy][y]+epsilon)/(niysum[yy]+Y*epsilon)*(nio[yy][o]+gamma)/(niosum[yy]+O*gamma);
					}
				}
				
				
				
//				if(s==1)
//				{
//					for(k=0;k<K;k++)
//					{
//						pk[k]=(nuz[flag][k]+alpha)/(nuzsum[flag]+K*alpha)*(nzc[k][c]+rho)/(nzcsum[k]+C*rho)*(nzw[k][w]+kappa)/(nzwsum[k]+W*kappa)*(nzo[k][o]+nu)/(nzosum[k]+O*nu);
//						for(yy=0;yy<I;yy++)
//						{
//							pk[k] = pk[k]*(nzi[k][yy]+beta)/(nzisum[k]+I*beta);
//							//pI[k]=(nui[flag][i]+beta)/(nuisum[flag]+I*beta)*(nix[i][x]+pi)/(nixsum[i]+X*pi)*(niy[i][y]+epsilon)/(niysum[i]+Y*epsilon);
//							//pI[yy]*=(nzi[k][yy]+beta)/(nzisum[k]+I*beta)*(nix[yy][x]+pi)/(nixsum[yy]+X*pi)*(niy[yy][y]+epsilon)/(niysum[yy]+Y*epsilon);//
//							pI[yy]*=(nzi[k][yy]+beta)/(nzisum[k]+I*beta)*(nix[yy][x]+pi)/(nixsum[yy]+X*pi)*(niy[yy][y]+epsilon)/(niysum[yy]+Y*epsilon);//
//						}
//					}
//
//				}
//				else
//				{
//					for(k=0;k<K;k++)
//					{
//						
//						pk[k]=(nuz[flag][k]+alpha)/(nuzsum[flag]+K*alpha);
//						//pI[k]=(nui[flag][i]+beta)/(nuisum[flag]+I*beta)*(nix[i][x]+pi)/(nixsum[i]+X*pi)*(niy[i][y]+epsilon)/(niysum[i]+Y*epsilon)*(nio[i][o]+gamma)/(niosum[i]+O*gamma);
////						pI[k]=(nzi[k][k]+beta)/(nuisum[flag]+I*beta)*(nix[k][x]+pi)/(nixsum[k]+X*pi)*(niy[k][y]+epsilon)/(niysum[k]+Y*epsilon)*(nio[k][o]+gamma)/(niosum[k]+O*gamma);//
//						for(yy=0;yy<I;yy++)
//						{
//							pk[k]=pk[k]*(nzc[k][c]+rho)/(nzcsum[k]+C*rho)*(nzw[k][w]+kappa)/(nzwsum[k]+W*kappa)*(nzi[k][yy]+beta)/(nzisum[k]+I*beta);
//							//pI[yy]*=(nzi[k][yy]+beta)/(nzisum[k]+I*beta)*(nix[yy][x]+pi)/(nixsum[yy]+X*pi)*(niy[yy][y]+epsilon)/(niysum[yy]+Y*epsilon)*(nio[yy][o]+gamma)/(niosum[yy]+O*gamma);//
//							pI[yy]*=(nzi[k][yy]+beta)/(nzisum[k]+I*beta)*(nix[yy][x]+pi)/(nixsum[yy]+X*pi)*(niy[yy][y]+epsilon)/(niysum[yy]+Y*epsilon)*(nio[yy][o]+gamma)/(niosum[yy]+O*gamma);//
//							
//						}
//					}
//				}
				
				Util.norm(pk);
				z = Util.draw(pk);
				Z[flag][j] = z;
				
				Util.norm(pI);
				i = Util.draw(pI);
				Iu[flag][j] = i;
				
				
				nuz[flag][z]++;
				nuzsum[flag]++;
				
				nzi[z][i]++;
				nzisum[z]++;
				
				nzw[z][w]++;
				nzwsum[z]++;
				
				nzc[z][c]++;
				nzcsum[z]++;
				
				nzo[z][o]++;
				nzosum[z]++;
				
				nix[i][x]++;
				nixsum[i]++;
				
				niy[i][y]++;
				niysum[i]++;
				
				nio[i][o]++;
				niosum[i]++;
			}
			flag++;
		}
	}
	

	public void gibbs2()
	{
		//System.out.println("gibbs1");
		int u,z,i,w,c,o,x,y,s;
		int flag=0;
		for(Map.Entry<Integer, List<String>> entry : trainset.entrySet()){
			if(entry.getValue().isEmpty())continue;
			int j=0;
			u=entry.getKey();//
			flag=u;//
			//System.out.println(u);
			//System.out.println(flag);
			int usize=entry.getValue().size();
			for(j=0;j<usize;j++)
			{
//				double[] pk =new double[K];
//				double[] py = new double[Y];
				double[] ps = new double[2];				
				String res=entry.getValue().get(j);
				String[] ss=res.split(",");
				w=Integer.parseInt(ss[1]);//
				o=Integer.parseInt(ss[3]);//
				c=0;
				x=Integer.parseInt(ss[5]);//
				y=Integer.parseInt(ss[6]);//
				
				z = Z[flag][j];
				i = Iu[flag][j];
				s = S[flag][j];
				
				nus[flag][s]--;
				nussum[flag]--;
				
				nzo[z][o]--;
				nzosum[z]--;
				
				nio[i][o]--;
				niosum[i]--;
				
				ps[1]=(nus[flag][1]+tau1)/(nussum[flag]+1)*(nzo[z][o]+nu)/(nzosum[z]+O*nu);
				ps[0]=(nus[flag][0]+tau0)/(nussum[flag]+1)*(nio[i][o]+gamma)/(niosum[i]+O*gamma);
				
				Util.norm(ps);
				s=Util.draw(ps);
				S[flag][j]=s;
				
				nus[flag][s]++;
				nussum[flag]++;
				
				nzo[z][o]++;
				nzosum[z]++;
				
				nio[i][o]++;
				niosum[i]++;
			}
			flag++;
		}
	}
	
	public void gibbs(int iterNum)
	{
		for(int iter=0;iter<iterNum;iter++){
			gibbs1();
			gibbs2();
			//System.out.println("iteration "+iter);
		}
	}
	
	public double[][] estParameter(int[][] m, int[] sum, double sp){
		int r = m.length;
		int c = m[0].length;
		double[][] p = new double[r][c];
		for(int i=0;i<r;i++)
			for(int j=0;j<c;j++)
				p[i][j] = (m[i][j]+sp)/(sum[i]+c*sp);
		return p;
	}
	
	public fengmodel getModel(){
		fengmodel model = new fengmodel();
		
		model.phiUZ = estParameter(nuz,nuzsum,alpha);
		model.phiZI = estParameter(nzi,nzisum,beta);
		model.phiZW = estParameter(nzw,nzwsum,kappa); 
		model.phiZC = estParameter(nzc,nzcsum,rho);
		model.phiZO = estParameter(nzo,nzosum,nu);

		model.phiIO = estParameter(nio,niosum,gamma);
		model.phiIX = estParameter(nix,nixsum,pi);
		model.phiIY = estParameter(niy,niysum,epsilon);
		//model.lambdaU;
		
		double[] lambdaU = new double[U];
		for(int u=0;u<U;u++)
		{
			lambdaU[u]=(nus[u][1]+tau1)/(nussum[u]+1);
		}
		model.lambdaU=lambdaU;
		return model;
	}
	
	
	public void inference(fengmodel model)
	{
		Random rand = new Random();
		int unum = user_num;
		int[][] nuz = new int[unum][K];
		int[] nusum = new int[unum];
		int[][] Z = new int[unum][];
		
//		int[][] nzi = new int[K][I];
//		int[] nzisum = new int[K];
//		int[][] II = new int[unum][];
		
		for(Map.Entry<Integer, List<String>> entry  : testset.entrySet()){
			int uid=entry.getKey();
			//Z[uid] = new int[groups[gid].length];
			
			Z[uid] = new int[entry.getValue().size()];
//			II[uid] = new int[entry.getValue().size()];
			for(int j=0;j<entry.getValue().size();j++){
				int z = rand.nextInt(K);
				Z[uid][j] = z;
				nuz[uid][z]++;
				
				
				int i = rand.nextInt(I);
//				II[uid][j] = i;
//				nzi[uid][i]++;
				
				nusum[uid]++;
//				nzisum[uid]++;
			}
		}
		double[] pZ = new double[K];
		double[] pI = new double[I];
		for(int iter=0;iter<iterNum;iter++){
			for(Map.Entry<Integer, List<String>> entry  : testset.entrySet()){
				int uid=entry.getKey();
				for(int j=0;j<entry.getValue().size();j++)
				{
					int z=Z[uid][j];
					//int c=entry.getValue().get(i);
					nuz[uid][z]--;
					//nuzsum[uid]--;
					for(int k=0;k<K;k++)
					{
						pZ[k] = (nuz[uid][k]+alpha);
					}
					Util1.norm(pZ);
					z = Util1.draw(pZ);
					Z[uid][j]=z;
					nuz[uid][z]++;
					//nusum[uid]++;
					
//					int i=II[uid][j];
					//int c=entry.getValue().get(i);
//					nui[uid][i]--;
					//nuisum[uid]--;
//					for(int k=0;k<I;k++)
//					{
//						pI[k] = (nui[uid][k]+beta);
//					}
//					Util1.norm(pI);
//					i = Util1.draw(pI);
//					II[uid][j]=i;
//					nui[uid][i]++;
					//nusum[uid]++;
				}
			}
		}
		model.theta=estParameter(nuz,nusum,alpha);
//		model.phi=estParameter(nui,nuisum,beta);
	}
	
	public Set<Integer> getCandEvent(){
		Set<Integer> cand = new HashSet<Integer>();
		for(Map.Entry<Integer, List<String>> entry : testset.entrySet()){
			for(String eid : entry.getValue())
			{
				//
				//int e = Integer.parseInt(seqduizhao.get(eid).split(",")[seqduizhao.get(eid).split(",").length-1]);//
				//cand.add(c);
				int e=Integer.parseInt(eid.split(",")[0]);
				cand.add(e);
				//
				//cand.add(eid);//
			}
				
		}
		return cand;
	}
	
	public Map<Integer,int[]> recommendori(fengmodel model,int topn,String inout)
	{
		//
		nuz=null;
		nzi=null;
		nzw=null;
		nzc=null;
		nzo=null;
		nix=null;
		niy=null;
		nio=null;
		nus=null;
		
		nuzsum = null;
		nzisum = null;
		nzwsum = null;
		nzcsum = null;
		nzosum = null;
		nixsum = null;
		niysum = null;
		niosum = null;
		nussum = null;
		//System.out.println(nuz.length);

		System.out.println("generating recommendation...");
		Map<Integer,int[]> reclist = new HashMap<Integer,int[]>();
//		List<Integer> candlist = new ArrayList<Integer>(getCandEventori());
//		List<Integer> candlistofcate = new ArrayList<Integer>(getCandEventori2());
		List<Integer> candlist = new ArrayList<Integer>(getCandEvent());
		
		if("inmatrix".equals(inout))
		{
			double[][] score = new double[U][E];
			int k;
			boolean hist = false;
		}
		else
		{
			double[][] score = new double[U][E];
			int k;
			for(int uid:testset.keySet())
			{
				System.out.println(uid);
				for(int i=0;i<candlist.size();i++)
				{
					int eid =candlist.get(i);
					String res=eventduizhao.get(eid);
					String[] ss=res.split(",");
//					System.out.println(ss.length);
					int w=Integer.parseInt(ss[0]);//
					int o=Integer.parseInt(ss[2]);//
					int c=0;
					int x=Integer.parseInt(ss[4]);//
					int y=Integer.parseInt(ss[5]);//

					
					double uscore=0;
					for(k=0;k<K;k++)
					{
						double uscore1=0;
						for(int j=0;j<I;j++)
						{
//							uscore1+=model.theta[uid][k]*(model.lambdaU[uid]*model.phiZW[k][w]*model.phiZC[k][c]*model.phiZO[k][o]+
//									(1-model.lambdaU[uid])*model.phiZI[k][j]*model.phiIX[j][x]*model.phiIY[j][y]*model.phiIO[j][o]);
							
							uscore1+=model.phiUZ[uid][k]*(model.lambdaU[uid]*model.phiZW[k][w]*model.phiZC[k][c]*model.phiZO[k][o]+
									(1-model.lambdaU[uid])*model.phiZI[k][j]*model.phiIX[j][x]*model.phiIY[j][y]*model.phiIO[j][o]);
							
							//no   uscore1+=model.phiUZ[uid][k]*model.phiZW[k][w]*model.phiZC[k][c]*model.phiIX[j][x]*model.phiIY[j][y]*(model.lambdaU[uid]*model.phiZO[k][o]+(1-model.lambdaU[uid])*model.phiZI[k][j]*model.phiIO[j][o]);
							//uscore1+=model.phiUZ[uid][k]*model.phiZW[k][w]*model.phiZC[k][c]*(model.lambdaU[uid]*model.phiZO[k][o]+(1-model.lambdaU[uid])*model.phiZI[k][j]*model.phiIO[j][o]*model.phiIX[j][x]*model.phiIY[j][y]);
		
						}
						uscore+=uscore1;
					}
					
//					for(k=0;k<K;k++)
//					{
//						double uscore1=0;
//						for(int j=0;j<I;j++)
//						{
//							double uscore2=0;
//							double fff=binomialsampler(1,model.lambdaU[uid]);
//							if(fff!=1)
//							{
//								uscore2=model.phiZI[k][j]*model.phiIX[j][x]*model.phiIY[j][y]*model.phiIO[j][o];
//							}
//							else
//							{
//								uscore2=model.phiZW[k][w]*model.phiZC[k][c]*model.phiZO[k][o];
//							}
//							
//							uscore1+=model.theta[uid][k]*uscore2;
//							
//						}
//						uscore+=uscore1;
//					}
					
					score[uid][eid]+=uscore;
					//score[uid][eid]+=uscore0;

				}
			}
			
			for(int j=0;j<U;j++)
			{
				int[] index = ArrayUtils.argsort(score[j],false);
				int[] array = new int[topn];
				for(int g=0;g<topn;g++)
				{
					int eid=index[g];
					{
						array[g] = eid;
					}
					
					//
					//array[g] = candlist.get(index[g]);
				}
				reclist.put(j, array);
			}
//			System.out.println("reclist.size=" + reclist.size());
			return reclist;
			
			
		}		
		return null;
	}
	public static double binomialsampler(int trials, double p){
        BinomialDistribution binomial=new BinomialDistribution(trials,p);
        return binomial.sample();
    }
	public void saveRecList(String recfile,String recfilenorm, Map<Integer,int[]> list) {
		String[] emap = new String[E];
		//
		System.out.println("write");
		for(Map.Entry<String, Integer> entry : ds.emap.entrySet()){
			emap[entry.getValue()] = entry.getKey();
		}
		try{
			FileWriter fw = new FileWriter(recfile);
			FileWriter fw2 = new FileWriter(recfilenorm);
			for(Map.Entry<Integer, int[]> entry : list.entrySet()){
				int[] array = entry.getValue();
				fw.write(entry.getKey()+"::");
				fw2.write(entry.getKey()+"::");
				for(int i=0;i<array.length;i++)
				{
					fw.write(emap[array[i]]+",");
					fw2.write(""+array[i]+",");
				}
					
				
				fw.write("\n");
				fw2.write("\n");
			}
			fw.close();
			fw2.close();
			}catch (IOException e) {
	            e.printStackTrace();
		}
	}
	public void appendMethodA(String fileName,String content){  
		try {  
		RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");  
		long fileLength = randomFile.length();  
		randomFile.seek(fileLength);  
		randomFile.writeBytes(content);  
		randomFile.close();  
		} catch (IOException e){  
		e.printStackTrace();  
		}  
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] K1=new int[]{100};
		int[] I1=new int[]{80};
		int i=0,j=0;
//		System.out.println("K1="+K1.length);
//		System.out.println("Y1="+I1.length);
		
		String timefile="timefile.txt";
		for(i=0;i<K1.length;i++)
		{
			for(j=0;j<I1.length;j++)
			{
				int K=K1[i],Y=I1[j];
				System.out.println(""+K+","+Y);
				int iternum = 100;
				int iterb=40;
				int topn = 20;
				String matrix = "outmatrix";
//				System.out.println("K="+K);
//				System.out.println("Y="+Y);
				fengjianhua_meetup_phoenix jh = new fengjianhua_meetup_phoenix(K,Y);
				jh.initialize();
				long startTime=System.currentTimeMillis();
				jh.gibbs(iternum);
				long endTime=System.currentTimeMillis();
				System.out.println("running time:"+(endTime-startTime)/1000.0);
				String timeout="K="+K+","+"Y="+Y+","+"time="+(endTime-startTime)/1000.0+"\n";
				jh.appendMethodA(timefile, timeout);
				fengmodel model = jh.getModel();
				//
//				Output.printTopic("C:/Users/dz/Desktop/douban_shanghai/phiUZ.txt", model.phiUZ, 20);
//				Output.printTopic("C:/Users/dz/Desktop/douban_shanghai/phiZI.txt", model.phiZI, 20);
//				Output.printTopic("C:/Users/dz/Desktop/douban_shanghai/ZW.txt", model.phiZW, 20);
//				Output.printTopic("C:/Users/dz/Desktop/douban_shanghai/ZC.txt", model.phiZC, 20);
//				Output.printTopic("C:/Users/dz/Desktop/douban_shanghai/ZO.txt", model.phiZO, 20);
//				Output.printTopic("C:/Users/dz/Desktop/douban_shanghai/IO.txt", model.phiIO, 20);
//				Output.printTopic("C:/Users/dz/Desktop/douban_shanghai/IX.txt", model.phiIX, 20);
//				Output.printTopic("C:/Users/dz/Desktop/douban_shanghai/IY.txt", model.phiIY, 20);
//				//Output.printTopic("C:/Users/dz/Desktop/douban_shanghai/ZO.txt", model.phiZO, 20);
//				Output.printTopic2("C:/Users/dz/Desktop/douban_shanghai/lamdaUS.txt", model.lambdaU, 20);				
				
				//jh.inference(model);//
				//Output.printTopic4("dataset/foursquare/output3/thetaUZ.txt", model.theta, 20);
				//Output.printTopic4("dataset/gowalla/output2/thetaUZ.txt", model.theta, 20);
				System.out.println("finish can stop");
				
				//
				Map<Integer,int[]> lists = jh.recommendori(model, topn, matrix);
				String recfile="fengresult/meetup/phoenix/efx1000/reccateK"+K+"Y"+Y+"_efx1000_m2.txt";//
				String recfilenorm="fengresult/meetup/phoenix/efx1000/reccateK"+K+"Y"+Y+"norm_efx1000_m2.txt";
			
				
				//String candpoi="";
				File file = new File(recfile);
				if(file.exists())
				{
					file.delete();
					jh.saveRecList(recfile,recfilenorm,lists);
				}
				else
				{
					jh.saveRecList(recfile,recfilenorm,lists);
				}
				//System.out.println(lists.size());
			}
		}
	}

}
