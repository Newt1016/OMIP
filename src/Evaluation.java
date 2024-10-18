import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Evaluation {
	
	public static void evaluate(Map<Integer,int[]> lists,Map<Integer,List<Integer>> testset, String metric,int n){
		if("Recall".equals(metric)){
			int hit = 0;
			double recall = 0;
			double precision =0;
			int truth = 0;
			int cnt;
//			int gnum = testset.size();
			for(int gid: testset.keySet()){
				truth += testset.get(gid).size();
				cnt = 0;
				for(int e : lists.get(gid)){
					if(testset.get(gid).contains(e)&&cnt<n)
						hit++;
					cnt++;
				}
//				precision += ((double)hit)/n;
//				recall += ((double)hit)/truth;
			}
//			System.out.println(recall);
			precision = ((double)hit)/(n*testset.size());
			recall = ((double)hit)/truth;
			System.out.println("topn="+n);
			System.out.println("hit="+hit);
			System.out.println("truth="+truth);
			System.out.println("Precision="+precision);
			System.out.println("Recall="+recall);
			System.out.println();
//			return recall;
		}
		else if("NDCG".equals(metric)){
			double ndcg = 0;
			int count = 0,i;
//			int[] d = new int[n];
			for(int gid : testset.keySet()){
				int[] array = new int[n];
				for(i = 0;i<n;i++){
					array[i] = lists.get(gid)[i];
//					d[i] = time[lists.get(gid)[i]];
				}
				double ndcg1 = ndcg(array,testset.get(gid));
				ndcg += ndcg1;
//				if(ndcg1>0)
					count++;
			}
			System.out.println("nDCG="+ndcg/count);
		}
	}
	
	public static double ndcg(int[] list, List<Integer> truth){
		double dcg = 0;
		int i;
		double[] rel = new double[list.length];
		for(i=0;i<list.length;i++){
			if(truth.contains(list[i]))
				rel[i] = 1;
//			System.out.println(rel[i]);
		}
//		for(i=0;i<list.length;i++)
//			System.out.print(rel[i]+" ");
//		System.out.println();
		for(i=0;i<rel.length;i++){
			dcg += rel[i]/Util.log2(i+2);
		}
//		System.out.println(dcg);
		if(dcg==0)
			return dcg;
		
		Arrays.sort(rel);
		double[] irel = new double[rel.length];
		for(i=0;i<rel.length;i++)
			irel[i] = rel[rel.length-i-1];
		double idcg = 0;
//		print(irel);
		for(i=0;i<irel.length;i++){
			idcg += irel[i]/Util.log2(i+2);
		}
//		System.out.println(idcg);
		return dcg/idcg;
	}
	
//	public static void main(String[] args) {
//		Integer[] a = {0,1,2};
//		List<Integer> truth = Arrays.asList(a);
//		int[] list = {0,1,2,3,4};
//		int[] list2 = {0,3,2,1,4};
//		int[] time = {1,2,3};
////		System.out.println(tndcg(list2,time,truth));
//		System.out.println(ndcg(list2,truth));
//	}
	
}
