import java.util.*;
public class Median {
    public static void main(String[] args){
        int[][] server = {{1,2,3,4}, {5,6,7,8},{9,10,11,12}};
        //System.out.println(calculateMedian(server,4));
        int[][] server2 = {{1,2,4}, {2,3,7},{8,8,8}};
        //System.out.println(calculateMedian(server2,4));
        int[][] server3 = {{1,0,0,}, {2,3,7},{4,6,7}};
        //System.out.println(calculateMedian(server3,4));
        int[][] server4 = {{1,0,0},{1,0,0}, {2,3,7},{4,6,7}};
        //System.out.println(calculateMedian(server4,4,calculateEstimateMean(server4)));
        int[][] server5 = {{1,4,7},{2,5,8}, {3,6,9},{0,0,0}};
        //System.out.println(calculateMedian(server5,6,calculateEstimateMean(server5)));
        int[][] server6 = {{1,2,4}, {2,3,7},{8,8,8},{9,9,9},{9,9,9}};
        System.out.println(calculateMedian(server6,4, calculateEstimateMean(server6)));

    }
    public static double calculateMedian(int [][] servers,int limit, double med){
        
        System.out.println(med);
        Window w = new Window( med, limit);
        for(int[] i : servers){
            w.addList(i);
        }
        System.out.println(w.getPqleft());
        System.out.println(w.getPqright());
        System.out.println();
        try{            
            return w.getMedian();
        }catch (Exception e){
            System.out.println("Window method doesn't work.");
            return calculateMedian(servers,limit, Double. parseDouble(e.getMessage()));
        }
        
    }
    public static double calculateEstimateMean(int[][] servers ){
        double[] median = new double[servers.length];
        for(int i = 0 ; i < servers.length; i++){
            median[i] = (getMedianForOneList(servers[i]));            
        }
        double med = getMedianForOneList(median);
        return med;
    }

    public static double getMedianForOneList(int[] list){
        ArrayList<Integer> l = new ArrayList<>();
        for(int i : list){
            l.add(i);
        }
        Collections.sort(l);
        if(l.size() %2 == 1){
            return l.get(l.size()/2);
        }else{
            return (l.get(l.size()/2) + l.get(l.size()/2 - 1))/2d;
        }
    }
    public static double getMedianForOneList(double[] list){
        ArrayList<Double> l = new ArrayList<>();
        for(double i : list){
            l.add(i);
        }
        Collections.sort(l);
        if(l.size() %2 == 1){
            return l.get(l.size()/2);
        }else{
            return (l.get(l.size()/2) + l.get(l.size()/2 - 1))/2d;
        }
    }
    
    
}

class Window{
    int leftCount;
    int rightCount;
    PriorityQueue<Integer> pqleft;
    PriorityQueue<Integer> pqright;
    int limit;
    double middle;

    public Window(double middle, int limit){
        //minheap
        this.pqleft = new PriorityQueue<>();
        //maxheap
        this.pqright = new PriorityQueue<>(Collections.reverseOrder());
        this.middle = middle;
        this.limit = limit;         
    }
    public PriorityQueue<Integer> getPqleft() {
        return pqleft;
    }



    public PriorityQueue<Integer> getPqright() {
        return pqright;
    }

    public void addList(int[] list){
        for(int i : list){
            if(i <= middle){
                pqleft.add(i);
                if(pqleft.size() > limit/2){
                    pqleft.poll();
                }
                leftCount++;

            }else{
                pqright.add(i);
                if(pqright.size() > limit/2){
                    pqright.poll();
                }
                rightCount++;
            }
        }
    }

    public double getMedian() throws Exception{
        int delta = leftCount - rightCount;
        int absdelta = Math.abs(delta);
        int first = 0;
        int second = 0;
        List<Integer> left = heapToList(pqleft);
        this.pqleft = null;
        List<Integer> right = heapToList(pqright);
        this.pqright = null;
        if(absdelta > limit/2){
            if(delta < 0 ){                
                throw new Exception(String.valueOf(middle+.5));
            }else{
                throw new Exception(String.valueOf(middle-.5));
            }
        }else{
            if(delta == 1|| delta == 0){
                first = left.get(left.size() - 1);
                second = right.get(0);
            }else if(delta >0){
                for(int i = left.size()-1 ; i >=0; i--){                    
                    second = first;
                    first = left.get(i);
                    if(absdelta == 0) break;
                    absdelta --;
                }                
            }else{
                for(int i = 0 ;i < right.size(); i++){
                    first = second;
                    second = right.get(i);                   
                    absdelta --;
                    if(absdelta == 0) break;
                }
            }

        }
        if((leftCount + rightCount) %2 ==1){
            return first;
        }else{
            return (first + second)/2d;
        }
       
    }
    public List<Integer> heapToList(PriorityQueue<Integer> pq){
        List<Integer> l = new ArrayList<>();
        while(pq.size()>0){
            l.add(pq.poll());
        }
        Collections.sort(l);
        return l;
    }
}