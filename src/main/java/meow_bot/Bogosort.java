package meow_bot;


import java.util.ArrayList;
import java.util.Random;

public class Bogosort   {
    private ArrayList<Integer> list = new ArrayList<Integer>();
    private final ArrayList<Integer> new_list = new ArrayList<Integer>();


    public Bogosort(ArrayList<Integer> list){
         this.list = list ;
    }

    public Bogosort(){
    }

    public ArrayList<Integer> sort(){
        int counter  = 0;
            while (!check()){
                counter++;
                int length = 0;
                int random_number = 0;
                length = list.size();
                for(int i = length; i>0; i--){
                    random_number = get_random_number(list.size());
                    new_list.add(list.get(random_number));
                    list.remove(random_number);
                }
                list = new_list;
            }
        if (Thread.currentThread().isInterrupted()){
            return list;
        }
        list.add(counter);
        return list;
    }

    public boolean check(){
        int number_before = -2147483648;
        if (Thread.currentThread().isInterrupted()){
            list.clear();
            return true;
        }
        for(int element : list){
            if(number_before > element){
                return false;
            }
            number_before = element;
        }
        return true;
    }

    public int get_random_number(int range){
        Random rand = new Random();
        return rand.nextInt(range);
    }
}
