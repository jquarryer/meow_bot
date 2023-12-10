package meow_bot;

import java.util.ArrayList;

public class Bubblesort  {
    private ArrayList<Integer> list = new ArrayList<Integer>();
    private int[] array;
    public Bubblesort(){
    }

    public Bubblesort(ArrayList<Integer> list){
        this.list = list ;
        list_to_array();
    }

    public void list_to_array(){
        array= new int[list.size()];
        int counter = 0;
        for(int element: list){
            array[counter] = element;
            counter++;
        }
    }

    public void array_to_list(){
        int length = 0;
        length = array.length;
        list.clear();
        for(int i =0; i<length; i++){
            list.add(array[i]);
        }
    }

    public ArrayList<Integer> sort(){
        int length =0;
        boolean shuffle = false;
        int counter = 0;
        length = array.length;
        do {
            shuffle=false;
            for(int i =0; i<length-1; i++){
                if(array[i]>array[i+1]){
                    array[i] = array[i] + array[i+1];
                    array[i+1] = array[i] -array[i+1];
                    array[i] = array[i] - array[i+1];
                    shuffle = true;
                    counter++;
                }
            }
            if (Thread.currentThread().isInterrupted()){
                list.clear();
                return list;
            }
        }
        while (shuffle);
        array_to_list();
        list.add(counter);
        return list;
    }
}
