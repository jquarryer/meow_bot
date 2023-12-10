package meow_bot;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Random;
import java.util.TimerTask;

public class Meow extends TimerTask {
    public TextChannel meow;
    public Meow(){
    }
    public Meow(TextChannel meow){
        this.meow = meow;
    }



    @Override
    public void run() {
        meow.sendMessage(messages()).queue();
    }

    public String messages(){
        switch (random_int(4)){
            case 0 :
                return "Meow";
            case 1 :
                return "Spiel mit mir";
            case 2 :
                return "Fütter mich";
            case 3 :
                return "Rawr";
        }
        return "";
    }

    public int random_int(int range){
        Random random = new Random();
        return random.nextInt(range);
    }
}
