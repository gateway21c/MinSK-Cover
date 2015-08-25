package rtree;


import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

public class treeSimulator {
	private static String filename = "MG.txt";
	public static long getCpuTime() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported()?
				bean.getCurrentThreadCpuTime(): 0L;
	}
	public static RTree construct () throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
		String s;
		String[] array;
		RTree R = new RTree();
		int count = 1;  
		System.out.print("Insertion Start\n");
		while ((s=in.readLine())!=null){
			array = s.split("\t");
			Entry e = new Entry(Integer.parseInt(array[0]),Integer.parseInt(array[1]),Integer.parseInt(array[2]),Integer.parseInt(array[3]));
//			System.out.print("Insert "+count+" node "+e.x.l+" "+e.x.h+" "+e.y.l+" "+e.y.h+"\n"); 
			R.insert(e);
			count ++; 
		}	
		System.out.print("Insertion End\n");
		in.close();
		
		System.out.print("Nodes : "+R.nodes+" heights: "+R.height+"\n");
		return R;
	}
	public static ArrayList<Entry> construct_array() throws NumberFormatException, IOException{
		BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
		String s;
		String[] array;
		ArrayList<Entry> list  = new ArrayList<Entry>();
		while ((s=in.readLine())!=null){
			array = s.split("\t");
			Entry e = new Entry(Integer.parseInt(array[0]),Integer.parseInt(array[1]),Integer.parseInt(array[2]),Integer.parseInt(array[3]));
			list.add(e);
		}	
		in.close();		 
		return list;
	}
//	public static ArrayList<Entry> search(LinkedList<Entry> l, int xl, int xh, int yl, int yh) {
//		//int count = 1;
//		ArrayList<Entry> result = new ArrayList<Entry>(); 
//		for (Entry e : l){
//			if (((!(xh<e.x.l || xl>e.x.h))) && (!(yl>e.y.h || yh<e.y.l))){ 
//				result.add(e);
////				System.out.println("Search x: "+e.x.l+" "+e.x.h+ ", y: "+e.y.l+" "+e.y.h);
////				count++;
//			}
//		}
//		
//		return result;
//	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		RTree r = construct();
		LinList linList = new LinList(construct_array());
		int xl, xh, yl, yh;
		int size = 500000000;
		long measurement = 10000;
		long s1 = 0, s2 = 0, s3 = 0,s4 = 0, s5 = 0;
		long cpuTimeElapsed;
		/* test begins */
		System.out.println("\nTest is started with (" + size + " X " + size + ", " + measurement + " times)"); 
		for (int i = 0; i<measurement; i++){
			xl = (int) (Math.random()*1000000000);
			yl = (int) (Math.random()*1000000000);
			xh = xl+size;
			yh = yl+size;
//			System.out.print("Test "+xl+" "+xh+" "+yl+" "+yh+"begins\n");
//			System.out.print("Normal Search Start\n");
//			ArrayList<Entry> list = new ArrayList<Entry>(l);
			cpuTimeElapsed = System.nanoTime();
			ArrayList<Entry> result1 = linList.search(xl, xh, yl, yh);
			cpuTimeElapsed = System.nanoTime()-cpuTimeElapsed;

			
			s1 += cpuTimeElapsed;
//			System.out.print("\nNormal Search takes "+cpuTimeElapsed+" ns End\n");
//			System.out.print("Index Search Start\n");
			cpuTimeElapsed = System.nanoTime();
			ArrayList<Entry> result2 = r.search(xl, xh, yl, yh);
			cpuTimeElapsed = System.nanoTime()-cpuTimeElapsed;
			
			if (result1.size() != result2.size()) {
				System.out.println("Error!!!!");
				for (Entry e: result1) {
					System.out.println("Search x: "+e.x.l+" "+e.x.h+ ", y: "+e.y.l+" "+e.y.h);
				}				
				System.out.print("total: "+(result1.size())+"\n"); 
				for (Entry e: result2) {
					System.out.println("Search x: "+e.x.l+" "+e.x.h+ ", y: "+e.y.l+" "+e.y.h);
				}				
				System.out.print("total: "+(result2.size())+"\n"); 
			}
//			System.out.print("nonleaf node: "+r.nodeCount+" leaf node: "+r.leafCount+"\n");
			s3 += r.nodeCount; 	s4 += r.leafCount; s5 += result1.size();

			s2 += cpuTimeElapsed;
//			System.out.print("Index Search takes "+cpuTimeElapsed+" ns End\n");
		}
		System.out.print("Normal search avg. "+s1/measurement+" ns Index search avg. "+s2/measurement+" ns\n");
		System.out.print("List node access " + linList.size()/RTree.M + "\n");
		System.out.print("Nonleaf access avg. "+s3/measurement+" Leaf access avg. "+s4/measurement+"\n");
		System.out.print("Num of result avg. "+(double)s5/(double)measurement+ "\n");
	}
	
}

class LinList {
	ArrayList<Entry> l;
	
	public LinList (ArrayList<Entry> l) {
		this.l = l;
	}
	
	public int size() {
		return l.size();
	}
	
	public ArrayList<Entry> search(int xl, int xh, int yl, int yh) {
		//int count = 1;
		ArrayList<Entry> result = new ArrayList<Entry>(); 
		for (Entry e : l){
			if (((!(xh<e.x.l || xl>e.x.h))) && (!(yl>e.y.h || yh<e.y.l))){ 
				result.add(e);
//				System.out.println("Search x: "+e.x.l+" "+e.x.h+ ", y: "+e.y.l+" "+e.y.h);
//				count++;
			}
		}
		
		return result;
	}
}