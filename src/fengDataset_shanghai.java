import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class fengDataset_shanghai {
	//
	Map<Integer,List<String>> trainset;
	Map<Integer,List<String>> testset;
	Map<Integer,List<String>> negativeSet;
	Map<Integer,String> eventduizhao;
	
	Map<String,Integer> umap;
	Map<String,Integer> omap;
	Map<String,Integer> wmap;
	Map<String,Integer> cmap;
	Map<String,Integer> eemap;
	Map<String,Integer> ffmap;

//	Map<String,Integer> xmap;
//	Map<String,Integer> ymap;
	Map<String,Integer> emap;
	
	
	public fengDataset_shanghai()
	{
				
		System.out.println(" finish reading map");
		System.out.println("start read trainset and testset");
		readtrainfile(fengInput_shanghai.trainfile);
		readtestfile(fengInput_shanghai.testfile);
		readnegativefile(fengInput_shanghai.negativefile);
		
		
		readeventduizhao(fengInput_shanghai.eventduizhao_inform);
		readFiletoumap(fengInput_shanghai.userduizhao);
		readFiletoomap(fengInput_shanghai.organizerduizhao);
		readFiletowmap(fengInput_shanghai.cateduizhao);
		readFiletoemap(fengInput_shanghai.eventduizhao);
		

	}
	
	
	public void readtrainfile(String trainfile)
	{
		trainset = new HashMap<Integer,List<String>>();
		File file = new File(trainfile);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            while ((line = reader.readLine()) != null) {
            	String[] temp = line.split("::");
            	int user = Integer.parseInt(temp[0]);

            	List<String> train = new ArrayList<String>();
            	for(int i=2;i<temp.length;i++)
            	{
            		train.add(temp[i]);
            	}
            	trainset.put(user, train);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void readtestfile(String testfile)
	{
		testset = new HashMap<Integer,List<String>>();
		File file = new File(testfile);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            while ((line = reader.readLine()) != null) {
            	String[] temp = line.split("::");
            	int user = Integer.parseInt(temp[0]);
            	
            	List<String> test = new ArrayList<String>();
            	for(int i=2;i<temp.length;i++)
            	{
            		test.add(temp[i]);
            	}
            	testset.put(user, test);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void readnegativefile(String testfile)
	{
		negativeSet = new HashMap<Integer,List<String>>();
		File file = new File(testfile);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            while ((line = reader.readLine()) != null) {
            	String[] temp = line.split(":");
            	int user = Integer.parseInt(temp[0]);
            	List<String> nSet = new ArrayList<String>();

            	String[] temp2 = temp[2].split(",");
            	for (int i = 0; i < temp2.length; i ++) {
            		nSet.add(temp2[i]);
            	}

            	negativeSet.put(user, nSet);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void readeventduizhao(String eventdz)
	{
		eventduizhao = new HashMap<Integer,String>();
		File file = new File(eventdz);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            while ((line = reader.readLine()) != null) {
            	String[] temp = line.split(",");
            	int eventid = Integer.parseInt(temp[0]);
            	String inform = temp[1] + "," + temp[2] + "," + temp[3] + "," + temp[4] + "," + temp[5] + ",";
            	eventduizhao.put(eventid, inform);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void readFiletoumap(String userduizhao)
	{
		umap = new HashMap<String,Integer>();
		File file = new File(userduizhao);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            int count = -1;
            while ((line = reader.readLine()) != null) {
            	if (count == -1) {
					count = 0;
					continue;
				}
            	else {
            		String[] temp = line.split(",");
                	String userid = temp[1];
                	int userform = Integer.parseInt(temp[0]);
                	umap.put(userid, userform);
            	}
            	
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void readFiletoomap(String organizerduizhao)
	{
		omap = new HashMap<String,Integer>();
		File file = new File(organizerduizhao);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            int count = -1;
            while ((line = reader.readLine()) != null) {
            	if (count == -1) {
					count = 0;
					continue;
				}
            	else {
            		String[] temp = line.split(",");
                	String oganizerid = temp[1];
                	int oganizerform = Integer.parseInt(temp[0]);
                	omap.put(oganizerid, oganizerform);
            	}
            	
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void readFiletowmap(String cateduizhao)
	{
		wmap = new HashMap<String,Integer>();
		File file = new File(cateduizhao);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            int count = -1;
            while ((line = reader.readLine()) != null) {
            	if (count == -1) {
					count = 0;
					continue;
				}
            	else {
            		String[] temp = line.split(",");
                	String cateid = temp[1];
                	int cateform = Integer.parseInt(temp[0]);
                	wmap.put(cateid, cateform);
            	}
            	
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void readFiletocmap(String cateduizhao)
	{
		cmap = new HashMap<String,Integer>();
		File file = new File(cateduizhao);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            int count = -1;
            while ((line = reader.readLine()) != null) {
            	if (count == -1) {
					count = 0;
					continue;
				}
            	else {
            		String[] temp = line.split(",");
                	String tagid = temp[1];
                	int tagform = Integer.parseInt(temp[0]);
                	cmap.put(tagid, tagform);
            	}
            	
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void readFiletoeemap(String eduizhao)
	{
		eemap = new HashMap<String,Integer>();
		File file = new File(eduizhao);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            int count = -1;
            while ((line = reader.readLine()) != null) {
            	if (count == -1) {
					count = 0;
					continue;
				}
            	else {
            		String[] temp = line.split(",");
                	String tagid = temp[1];
                	int tagform = Integer.parseInt(temp[0]);
                	eemap.put(tagid, tagform);
            	}
            	
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	

	public void readFiletoffmap(String fduizhao)
	{
		ffmap = new HashMap<String,Integer>();
		File file = new File(fduizhao);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            int count = -1;
            while ((line = reader.readLine()) != null) {
            	if (count == -1) {
					count = 0;
					continue;
				}
            	else {
            		String[] temp = line.split(",");
                	String tagid = temp[1];
                	int tagform = Integer.parseInt(temp[0]);
                	ffmap.put(tagid, tagform);
            	}
            	
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}


	public void readFiletoemap(String eventduizhao)
	{
		emap = new HashMap<String,Integer>();
		File file = new File(eventduizhao);
		BufferedReader reader = null;
		try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            //int count = 0;
            int count = -1;
            while ((line = reader.readLine()) != null) {
            	if (count == -1) {
					count = 0;
					continue;
				}
            	else {
            		String[] temp = line.split(",");
                	String eventid = temp[1];
                	int eventform = Integer.parseInt(temp[0]);
                	emap.put(eventid, eventform);
            	}
            	
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

}