import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class jianhua_fengbaseline {
	int K = 20;
	int R = 80;
	int iterNum = 5;
	int user_num, event_num, venue_num, org_num, word_num;
	double alpha = 50.0/K;
	double beta = 0.01;
	double gamma = 50.0/R;
	double pi = 0.01, sigma = 0.01, epsilon = 0.01;
	double tau = 0.01;
	int[][] trainset;
	Map<Integer,List<Integer>> testset;
	Map<Integer,List<Integer>> gtest;
	int[][] groups;
	int[] venue;
	int[] org;
	int[][] corpus;
	int[] Rg;
	int[] Z;
	
	int[][] ngz;
	int[][] ngr;
	int[][] nzu;
	int[][] nru;
	int[][] nrv;
	int[][] nzw;
	int[][] nzh;
	int[] ngsum;
	int[] nzusum;
	int[] nrusum;
	int[][] nzrsum;
	int[] nzwsum;
	int[] nzhsum;
	int[] nrvsum;
	
	double[] pz;
	double[] pr;
	Dataset ds;
	public jianhua_fengbaseline(int K,int R){
		this.K = K;
		this.R = R;
		ds = new Dataset();
		trainset = ds.train;
		testset = ds.testset;
		groups = ds.groups;
		venue = ds.venue;
		org = ds.org;
		corpus = ds.corpus;
		user_num = ds.umap.size();
		event_num = ds.emap.size();
		org_num = ds.omap.size();
		venue_num = ds.vmap.size();
		word_num = ds.catmap.size();
		gtest = getGroupTest();
		int G = trainset.length;
		Z = new int[G];
		Rg = new int[G];
		ngz = new int[G][K];
		ngr = new int[G][R];
		nzu = new int[K][user_num];
		nru = new int[R][user_num];
		nrv = new int[R][venue_num];
		nzw = new int[K][word_num];
		nzh = new int[K][org_num];
		ngsum = new int[G];
		nzusum = new int[K];
		nrusum = new int[R];
		nzwsum = new int[K];
		nzhsum = new int[K];
		nrvsum = new int[R];
		pz = new double[K];
		pr = new double[R];
	}
	
	public void randomInitial(){
		Random rand = new Random();
		int e,gid,v,o,z,r;
		for(int g=0; g<trainset.length;g++){
			e = trainset[g][1];
			gid = trainset[g][0];
			v = venue[e];
			o = org[e];
			z = rand.nextInt(K);
			r = rand.nextInt(R);
			Z[g] = z;
			Rg[g] = r;
			ngz[g][z]++;
			ngr[g][r]++;
			ngsum[g]++;
			nrv[r][v]++;
			nrvsum[r]++;
			nzh[z][o]++;
			nzhsum[z]++;
			for(int u : groups[gid]){
				nzu[z][u]++;
				nru[r][u]++;
				nzusum[z]++;
				nrusum[r]++;
			}
			for(int j=0;j<corpus[e].length;j++){
				nzw[z][corpus[e][j]]++;
				nzwsum[z]++;
			}
		}
	}
	
	public int draw(double[] a){
		double r = Math.random();
		for(int i = 0; i<a.length;i++){
			r = r - a[i];
			if(r<0) return i;
		}
		return a.length-1;
	}
	
	public void update(){
		int e,gid,o,z,r,v;
		for(int g=0; g<trainset.length;g++){
			e = trainset[g][1];
			gid = trainset[g][0];
			o = org[e];
			v = venue[e];
			z = Z[g];
			r = Rg[g];
			ngz[g][z]--;
			ngr[g][r]--;
			nzh[z][o]--;
			nrv[r][v]--;
			nrvsum[r]--;
			ngsum[g]--;
			nzhsum[z]--;
			for(int u : groups[gid]){
				nzu[z][u]--;
				nru[r][u]--;
				nzusum[z]--;
				nrusum[r]--;
			}
			for(int j=0;j<corpus[e].length;j++){
				nzw[z][corpus[e][j]]--;
				nzwsum[z]--;
			}
			for(int k=0;k<K;k++){
				double pzru = 1,pzw = 1;
				for(int i=0;i<corpus[e].length;i++)
					pzw *= (nzw[k][corpus[e][i]]+beta)/(nzwsum[k]+word_num*beta);
				for(int u : groups[gid])
					pzru *= (nzu[k][u]+epsilon)/(nzusum[k]+user_num*epsilon);
				pz[k] = (ngz[g][k]+alpha)*(nzh[k][o]+tau)/(nzhsum[k]+org_num*tau)*pzru*pzw;
			}
			for(int j=0;j<R;j++){
				double pzru = 1;
				for(int u : groups[gid])
					pzru *= (nru[j][u]+sigma)/(nrusum[j]+user_num*sigma);
				pr[j]= (ngr[g][j]+gamma)*(nrv[j][v]+pi)/(nrvsum[j]+venue_num*pi)*pzru;
			}
			
			Util.norm(pz);
			Util.norm(pr);
			z = draw(pz);
			r = draw(pr);
			Z[g] = z;
			Rg[g] = r;
			ngz[g][z]++;
			ngr[g][r]++;
			nrv[r][v]++;
			nrvsum[r]++;
			nzh[z][o]++;
			ngsum[g]++;
			nzhsum[z]++;
			for(int u : groups[gid]){
				nzu[z][u]++;
				nru[r][u]++;
				nzusum[z]++;
				nrusum[r]++;
			}
			for(int j=0;j<corpus[e].length;j++){
				nzw[z][corpus[e][j]]++;
				nzwsum[z]++;
			}
		}
	}

	
	public void train(int iterNum,String file){
		this.iterNum = iterNum;
		double[] perp = new double[iterNum/10];
		for(int it=0;it<iterNum;it++){
			update();
			if(it%10 == 0)
				perp[it/10] = perplexity();
			if(it%100==0)
				System.out.println("iteration: "+(it+1));
		}
		Output.savePerplexity(file, perp);
	}
	
	public Model train(int iterNum, int iterb){
		this.iterNum = iterNum;
		int count = 0;
		Model model = new Model();
		model.phiZU = new double[K][user_num];
		model.phiRU = new double[R][user_num];
		model.phiRV = new double[R][venue_num];
		model.phiZW = new double[K][word_num];
		model.phiZH = new double[K][org_num];
		for(int it=0;it<iterNum;it++){
			update();
			if(it%100==0)
				System.out.println("iteration: "+it);
			
			if(it>iterb && it%10==0){
				count++;
				Util.addIn(model.phiZU, estParameter(nzu,nzusum,epsilon));
				Util.addIn(model.phiRU, estParameter(nru,nrusum,sigma));
				Util.addIn(model.phiRV, estParameter(nrv,nrvsum,pi));
				Util.addIn(model.phiZW, estParameter(nzw,nzwsum,beta));
				Util.addIn(model.phiZH, estParameter(nzh,nzhsum,tau));
			}
		}
		Util.divid(model.phiZU, count);
		Util.divid(model.phiRU, count);
		Util.divid(model.phiRV, count);
		Util.divid(model.phiZW, count);
		Util.divid(model.phiZH, count);
		return model;
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
	
	public double[][][] estZRU(int[][][] nzru, int[][] nzrsum, double tau){
		int z = nzru.length;
		int r = nzru[0].length;
		int u = nzru[0][0].length;
		double[][][] p = new double[z][r][u];
		for(int i=0;i<z;i++)
			for(int j=0;j<r;j++)
				for(int q=0;q<u;q++)
					p[i][j][q] = (nzru[i][j][q]+tau)/(nzrsum[i][j]+u*tau);
		return p;
	}
	
	public Model getModel(){
		Model prm = new Model();
		prm.phiZU = estParameter(nzu,nzusum,epsilon);
		prm.phiRU = estParameter(nru,nrusum,sigma);
		prm.phiRV = estParameter(nrv,nrvsum,pi);
		prm.phiZW = estParameter(nzw,nzwsum,beta);
		prm.phiZH = estParameter(nzh,nzhsum,tau);
		return prm;
	}
	
	public void inference(Model model){
		Random rand = new Random();
		int gnum = groups.length;
		int[][] ngz = new int[gnum][K];
		int[][] ngr = new int[gnum][R];
		int[] ngsum = new int[gnum];
//		int[] ngsum = new int[gnum];
		int[] Z = new int[gnum];
		int[] Rg = new int[gnum];
		int z,r;
		for(int gid : gtest.keySet()){
			z = rand.nextInt(K);
			r = rand.nextInt(R);
			Z[gid] = z;
			Rg[gid] = r;
			ngz[gid][z]++;
			ngr[gid][r]++;
			ngsum[gid]++;
		}
		double[] pz = new double[K];
		double[] pr = new double[R];
		double p = 0;
		for(int iter=0;iter<iterNum;iter++){
			for(int gid : gtest.keySet()){
				z = Z[gid];
				r = Rg[gid];
				ngz[gid][z]--;
				ngr[gid][r]--;
				for(int k=0;k<K;k++){
					p = 1;
					for(int u : groups[gid]){
						p*=model.phiZU[k][u];
					}
					pz[k] = (ngz[gid][k]+alpha)*p;
				}
				for(int j=0;j<R;j++){
					p = 1;
					for(int u : groups[gid])
						p*=model.phiRU[j][u];
					pr[j] = (ngr[gid][j]+gamma)*p;
				}
				Util.norm(pz);
				z = draw(pz);
				Util.norm(pr);
				r = draw(pr);
				Rg[gid] = r;
				Z[gid] = z;
				ngz[gid][z]++;
				ngr[gid][r]++;
			}
		}
		model.theta = estParameter(ngz,ngsum,alpha);
		model.phi = estParameter(ngr,ngsum,gamma);
	}
	
	public Set<Integer> getCandEvent(){
		Set<Integer> cand = new HashSet<Integer>();
		for(Map.Entry<Integer, List<Integer>> entry : testset.entrySet()){
			for(Integer eid : entry.getValue())
				cand.add(eid);
		}
		return cand;
	}
	
	public Map<Integer,int[]> recommend(Model model,int topn){
		System.out.println("generating recommendation...");
		Map<Integer,int[]> reclist = new HashMap<Integer,int[]>();
		List<Integer> candlist = new ArrayList<Integer>(getCandEvent());
		System.out.println("number of candidates: " + candlist.size());
		int k,r;
		double pr = 0,pu=0,pw=0;
		double[][] score = new double[groups.length][candlist.size()];
		
		for(int gid : gtest.keySet()){
			for(int i=0;i<candlist.size();i++){
				int eid = candlist.get(i);
				for(k=0;k<K;k++){
					pw = 1;
					for(int w : corpus[eid]){
						pw *= model.phiZW[k][w];
					}
					pr = 0;
					for(r=0;r<R;r++){
						pu = 1;
						for(int u : groups[gid])
							pu *= model.phiRU[r][u]*model.phiZU[k][u];
						pr += model.phi[gid][r]*model.phiRV[r][venue[eid]]*Math.pow(pu, 1.0/groups[gid].length);
					}
					score[gid][i] += model.theta[gid][k]*model.phiZH[k][org[eid]]*pr*Math.pow(pw, 1.0/corpus[eid].length);	
				}
			}
		}
//		Util.print(score);
		for(int i=0;i<groups.length;i++){
			int[] index = ArrayUtils.argsort(score[i],false);
			int[] array = new int[topn];
			for(int j=0;j<topn;j++)
				array[j] = candlist.get(index[j]);
			reclist.put(i, array);
		}
		return reclist;
		
	}
	
	public Map<Integer,List<Integer>> getGroupTest(){
		Map<Integer,List<Integer>> gtest = new HashMap<Integer,List<Integer>>();
//		int[][] groups = getGroups();
		boolean hasg = true;
		for(int i=0;i<groups.length;i++){
			hasg = true;
			if(!testset.containsKey(groups[i][0])) continue;
			Set<Integer> set = new HashSet<Integer>(testset.get(groups[i][0]));
			for(int u : groups[i]) {
				if(!testset.containsKey(u)) {
					hasg = false;
					break;
				}
				set.retainAll(testset.get(u));
			}
			if(hasg&&set.size()>0)
			gtest.put(i, new ArrayList<Integer>(set));
		}
		return gtest;
	}
	
	public void saveRecList(String recfile, Map<Integer,int[]> list) {
		String[] emap = new String[event_num];
		for(Map.Entry<String, Integer> entry : ds.emap.entrySet()){
			emap[entry.getValue()] = entry.getKey();
		}
		try{
			FileWriter fw = new FileWriter(recfile);
			for(Map.Entry<Integer, int[]> entry : list.entrySet()){
				int[] array = entry.getValue();
				fw.write(entry.getKey()+" ");
				for(int i=0;i<array.length;i++)
					fw.write(emap[array[i]]+" ");
				fw.write("\n");
			}
			fw.close();
			}catch (IOException e) {
	            e.printStackTrace();
		}
	}
	
	public double perplexity(){
		double log_per = 0;
		int gid,e,g,k,r,v,h;
		double theta = 0, rho = 0, p = 0, pr=0, puz = 0;
		for(g=0;g<trainset.length;g++){
			gid = trainset[g][0];
			e = trainset[g][1];
			v = venue[e];
			h = org[e];
			p = 0;
			for(k=0;k<K;k++){
				theta = (ngz[g][k]+alpha)/(ngsum[g]+K*alpha);
				rho = (nzh[k][h]+tau)/(nzhsum[k]+org_num*tau);
				pr = 0;
				for(r=0;r<R;r++){
					puz = 1;
					for(int u : groups[gid]){
						puz *= (nru[r][u]+sigma)/(nrusum[r]+user_num*sigma);
					}
					pr += (ngr[g][r]+gamma)/(ngsum[g]+R*gamma)*(nrv[r][v]+pi)/(nrvsum[r]+venue_num*pi)*Math.pow(puz,1.0/groups[gid].length);
				}
				p += pr*theta*rho;
			}
			log_per -= Math.log(p);
		}
		return Math.exp(log_per/trainset.length);
	}
	
	public static void saveTopics(){
		for(int gsize=2;gsize<=2;gsize++){
			for(int K=80;K<=80;K+=10){
				for(int R=100;R<=100;R+=10){
					for(int i=3;i<4;i++){
						Input.trainfile = String.format("dataset/beijing/train_%d.csv",i);
						Input.testfile = String.format("dataset/beijing/test_%d.csv",i);
						Input.corpusfile = "dataset/beijing/cate_tags.csv";
						Input.eventfile = "dataset/beijing/events.csv";
						Input.groupfile = String.format("dataset/beijing/groups_%d.csv",gsize);
						JianHua jh = new JianHua(K,R);
						jh.randomInitial();
//						long startTime=System.currentTimeMillis();
						Model model = jh.train(2500,2000);
						Output.printTopic("dataset/beijing/output/lambda.txt", model.phiZW, 20);
//						Output.printTopic("dataset/beijing/output/rho.txt", model.phiZH, 20);
//						Output.printTopic("dataset/beijing/output/psi.txt", model.phiZU, 20);
						Output.printTopic("dataset/beijing/output/eta.txt", model.phiRV, 20);
//						Output.printTopic("dataset/beijing/output/phi.txt", model.phiRU, 20);
//						Output.saveMap("dataset/beijing/output/user_map.csv", jh.ds.umap);
//						Output.saveMap("dataset/beijing/output/org_map.csv", jh.ds.omap);
						Output.saveMap("dataset/beijing/output/venue_map.csv", jh.ds.vmap);
						Output.saveMap("dataset/beijing/output/word_map.csv", jh.ds.catmap);
//						long endTime=System.currentTimeMillis();
//						System.out.println("running time:"+(endTime-startTime)/1000.0);
//						Model model = jh.getModel();
//						jh.inference(model);
//						Map<Integer,int[]> reclist = jh.recommend(model, 20);
//						jh.saveRecList(String.format("dataset/beijing/output/CVTM-V6/reclist_K%dR%d_%d.txt",jh.K,jh.R,i), reclist);
					}
				}
			}
			}
	}
	
	public static void testDouban(){
		for(int gsize=2;gsize<=2;gsize++){
			for(int K=80;K<=80;K+=10){
				for(int R=100;R<=100;R+=10){
						Input.trainfile = String.format("dataset/beijing/train.txt");
						Input.testfile = String.format("dataset/beijing/test.csv");
						Input.corpusfile = "dataset/beijing/category.csv";
						Input.eventfile = "dataset/beijing/events.csv";
//						Input.groupfile = String.format("dataset/beijing/groups_%d.csv",gsize);
						JianHua jh = new JianHua(K,R);
						jh.randomInitial();
						long startTime=System.currentTimeMillis();
						Model model = jh.train(2500,2000);
						long endTime=System.currentTimeMillis();
						System.out.println("running time:"+(endTime-startTime)/1000.0);
//						Model model = jh.getModel();
						jh.inference(model);
						Map<Integer,int[]> reclist = jh.recommend(model, 20);
						jh.saveRecList(String.format("dataset/beijing/output/jianhua/reclist_K%dR%d_%d_new1.txt",jh.K,jh.R), reclist);
				}
			}
		}
	}
	
//	public static void testMeetup(){
//		int gsize = 2, K = 80, R = 100;
//		Input.trainfile = "dataset/CH/train.csv";
//		Input.testfile = "dataset/CH/test.csv";
//		Input.corpusfile = "dataset/CH/category.csv";
//		Input.eventfile = "dataset/CH/events.csv";
//		Input.groupfile = String.format("dataset/CH/groups_%d.csv",gsize);
//		JianHua jh = new JianHua(K,R);
//		jh.randomInitial();
//		long startTime=System.currentTimeMillis();
//		Model model = jh.train(2500,2000);
//		long endTime=System.currentTimeMillis();
//		System.out.println("running time:"+(endTime-startTime)/1000.0);
//		jh.inference(model);
//		Map<Integer,int[]> reclist = jh.recommend(model, 20);
//		jh.saveRecList(String.format("dataset/PH/CVTM/reclist_K%dR%d.txt",jh.K,jh.R), reclist);
//	}
	
	public static void main(String[] args){	
		testDouban();
		//testMeetup();
		//saveTopics();
	}
}
