import java.util.*;
import java.io.*;
public class EMS{
    static List<String> sortedFile;
    public static void main(String[] args){
        sortedFile = new ArrayList<>();
        File f = new File("inputFilePath.txt");
        Scanner s = null;
        try {
            s = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String filePath = s.nextLine();
        s.close();
        System.out.println("Median is: "+getMedian(filePath, sortedFile, 2));
    }

    public static double getMedian(String filePath, List<String> sortedFile, int maxThread){
        //Sort arr on server with limit of max number of thread.
        externalSort(filePath, sortedFile, maxThread);
        //Create scanner for every sorted file.
        File[] files = new File[sortedFile.size()];
        Scanner[] scs = new Scanner[sortedFile.size()];
        for(int i = 0 ; i < sortedFile.size(); i++){
            files[i] = new File(sortedFile.get(i));
            System.out.println("Adding " + files[i]);
            try {
                scs[i] = new Scanner(files[i]).useDelimiter(",");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        int length = 0;
        //use map to record the current min number at every file.       
        Map<Integer, Integer> mp = new HashMap<>();
        //count the total number of Integer and initialize map with first number of sorted file.
        for(int i = 0 ; i < scs.length; i++){
            if(scs[i].hasNextInt()){
                length += scs[i].nextInt();
            }
            if(scs[i].hasNextInt()){
                mp.put(i, scs[i].nextInt());
            }
        }
        System.out.println("Length of all input is: " + length);
        int first = -1;
        int second = -1;
        int count = 0;
        // merge half of the sorted number to find the median;
        while (count <= length / 2){
            int minIdx = -1;
            int minVal = Integer.MAX_VALUE;
            // locate the minimun number among all file;
            for(Map.Entry<Integer, Integer> e : mp.entrySet()){
                if(e.getValue() < minVal){
                    minIdx = e.getKey();
                    minVal = e.getValue();
                }
            }
            count++;
            first = second;
            second = minVal;
            //update map with the next value, put max int if reach end of file.
            if(scs[minIdx].hasNextInt()){                
                mp.put(minIdx, scs[minIdx].nextInt());
            }else{
                mp.put(minIdx,Integer.MAX_VALUE);
            }
        }
        for(Scanner s : scs){
            s.close();
        }
        if(length%2 == 1){
            return second;
        }else{
            return (first + second)/2d;
        }
    }

    public static void externalSort(String filePath, List<String> sortedFile,int maxThread){
        File folder = new File(filePath);
        File[] ls = folder.listFiles();
        Thread[] ths = new Thread[ls.length];
        int count = 0;
        int finishedIdx = 0;
        for(int i = 0 ;  i < ls.length; i++){
            if(ls[i].isFile()&& getFileExtension(ls[i]).equals(".txt")){
                String out = filePath + "\\sorted\\sorted" + ls[i].getName();
                WorkerThread w = new WorkerThread(ls[i].getAbsolutePath(), out);
                sortedFile.add(out);
                ths[i] = new Thread(w);               
                ths[i].start();
                count++;
                if( count >=maxThread){
                    try {
                        ths[finishedIdx].join();
                        finishedIdx++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }  
            }          
        }
        for(Thread t: ths){
            try {
                if(t != null)
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}


class WorkerThread implements Runnable{
    protected String filePath;
    protected String outFilePath;
    private List<Integer> list;
    public WorkerThread(String filePath,String outFilePath){
        this.filePath = filePath;
        this.outFilePath = outFilePath;
        this.list = new LinkedList<>();
    }

    @Override
    public void run(){
        File f;
        File outf;
        Scanner sc;
        PrintWriter pw; 
        try {                            
            f = new File(filePath);
            sc = new Scanner(f).useDelimiter(",");   
            int count = 0;        
            while(sc.hasNextInt()){
                list.add(sc.nextInt());
                count++;
            }
            sc.close();
            Collections.sort(list);
            outf = new File(outFilePath);
            pw = new PrintWriter(outf);
            pw.write(String.valueOf(count));
            pw.write(",");
            for(int i : list){
                pw.write(String.valueOf(i));
                pw.write(",");
            }
            pw.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            
        }
    }
}

