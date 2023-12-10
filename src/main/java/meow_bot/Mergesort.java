package meow_bot;

import java.util.ArrayList;

public class Mergesort {
    private int[] array;

    private int counter;
    private ArrayList<Integer> list = new ArrayList<Integer>();
    public Mergesort(ArrayList<Integer> list){
        this.list=list;
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
        sort(array);
        array_to_list();
        list.add(counter);
        return list;
    }

    public void sort(int[] array){
        int length = array.length;
        if (length<=1)return;
        int subarray_length = array.length/2;
        int[] left_array = new int[subarray_length];
        int[] right_array = new int[length-subarray_length];
        int index_right = 0;
        for (int i = 0; i< length; i++){
            if(i<subarray_length){
                left_array[i] = array[i];
            }
            else {
                right_array[index_right] = array[i];
                index_right++;
            }
        }
        if (Thread.currentThread().isInterrupted()){
            list.clear();
            return ;
        }
        sort(left_array);
        if (Thread.currentThread().isInterrupted()){
            list.clear();
            return ;
        }
        sort(right_array);
        if (Thread.currentThread().isInterrupted()){
            list.clear();
            return ;
        }
        merge(array, left_array, right_array);
    }

    public void merge(int[] array,int[] left_array,int[] right_array){
        int length_left = left_array.length;
        int length_right = right_array.length;
        int index_left = 0;
        int index_right = 0;
        for (int i = 0 ; i < array.length; i++){
            if(index_left >=length_left){
                array[i] = right_array[index_right];
                index_right++;
                counter++;
                continue;
            }
            if(index_right >=length_right){
                array[i] = left_array[index_left];
                index_left++;
                counter++;
                continue;
            }
            if(right_array[index_right]>left_array[index_left]){
                array[i] = left_array[index_left];
                index_left++;
                counter++;
            }
            else {
                array[i] = right_array[index_right];
                index_right++;
                counter++;
            }

        }
    }
}

