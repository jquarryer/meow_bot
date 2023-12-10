package meow_bot;


import java.util.ArrayList;
import java.util.Random;

public class Main   {

    public static void main(String[] args) throws InterruptedException {
            /*Primzahl p = new Primzahl(9223372036854775807L);*/
            audio a = new audio();
            Bot bot = new Bot();
            bot.start();
/*        for (int i = 0; i< 10; i++){
            Thread thread = new Thread(new Main());
            thread.start();
        }*/
    }

/*    @Override*/
/*    public void run() {
        long start = System.currentTimeMillis();
        ArrayList<Integer> list = new ArrayList<Integer>(14);
        Random rand = new Random();
        for (int i = 0; i < 12 ; i++){
            list.add( rand.nextInt(10000));
        }
        Bogosort b = new Bogosort(list);
        list= b.sort();
        int tries= list.get(list.size()-1);
        list.remove(list.size()-1);
        String answer = list.toString();
        System.out.println("Bogosort: Liste: "+answer+ " Anzahl Versuche:"+tries);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Time elapsed: " + timeElapsed + "ms");
        System.exit(1);
    }*/
}