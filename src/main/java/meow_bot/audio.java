package meow_bot;
import javax.sound.sampled.*;

public class audio  {

    public void test(){
        Mixer.Info[]   mix = AudioSystem.getMixerInfo();

        for (Mixer.Info info : mix){
            System.out.println(info.getName()+"......"+info.getDescription());
        }
    }
}
