package meow_bot;

import java.util.ArrayList;
import java.lang.Math;

public class Primzahl {
    private long end= 0L;
    private int counter = 0;
    private final ArrayList<Long>primzahlen = new ArrayList <Long>(2140000000);
    public Primzahl(long i){
        end = i;
    }

    public void primzahl(){
        primzahlen.add(2L);
        double root = 1;
        Boolean isPrimzahl;
        for(long i = 3L; i<=end;i+=2){
            isPrimzahl = true;
            for (int z = 0; z < primzahlen.size(); z++){
                root = (double) i;
                if(Math.sqrt(root)<primzahlen.get(z)){
                    break;
                }
                if (i%primzahlen.get(z)==0){
                    isPrimzahl=false;
                    break;
                }
            }
            if(isPrimzahl){
                primzahlen.add(i);
                counter++;
                System.out.println(counter+": "+i);
            }
        }
    }

    public void print_list(){
        for (long element: primzahlen){
            System.out.println(element);
        }
    }
}
