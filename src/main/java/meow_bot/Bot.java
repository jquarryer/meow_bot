package meow_bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.cdimascio.dotenv.Dotenv;
import meow_bot.lavaplayer.GuildMusicManager;
import meow_bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Bot extends ListenerAdapter implements Runnable{
    public Guild guild;
    public ArrayList<Thread> thread_list= new ArrayList<Thread>();
    public String content;
    public SlashCommandInteractionEvent event;
    public Timer timer;
    public TimerTask task;
    public int timeout = 600000;

    private HashMap<String, TextChannel> channelmap = new HashMap<>();

    private HashMap<String, VoiceChannel> voicechannelmap = new HashMap<>();

    TextChannel allgemein;
    TextChannel botzentrale;
    TextChannel meow;
    VoiceChannel main;


    public Bot(){
    }
    public Bot(Guild guild,HashMap<String, TextChannel> channelmap, HashMap<String, VoiceChannel> voicechannelmap ,String content, SlashCommandInteractionEvent event){
        this.guild = guild;
        this.channelmap = channelmap;
        this.voicechannelmap = voicechannelmap;
        this.content=content;
        this.event = event;
        allgemein = channelmap.get("allgemein");
        botzentrale = channelmap.get("botzentrale");
        meow = channelmap.get("meow");
        main = voicechannelmap.get("Illegal Rave Party");
    }




    public  void start() {
        Dotenv dotenv = Dotenv.load();
        JDA bot = JDABuilder.createDefault(dotenv.get("Bot_Token"))
                .addEventListeners(new Bot())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES)
                .enableIntents(GatewayIntent.GUILD_MODERATION)
                .setActivity(Activity.competing("Gigacat: Mega eraser of worlds"))
                .build();
    }

    @Override
    public void run() {
        System.out.println(content);
        switch (content) {
            case "clear" -> clear(event);
            case "play" -> play(event);
            case "stop" -> stop();
            case "skip" -> skip(event);
            case "pause" -> pause();
            case "queue" -> getQueue();
            case "shuffle" -> shuffle(event);
            case "clown" -> clown(event);
            case "join" -> join(event);
            case "help" -> help();
            case "rec" -> get_recommendation(event);
            case "now_playing" -> now_playing();
            case "remove" -> remove_from_queue(event);
            case "bogosort" -> bogosort(event);
            case "bubblesort" -> bubblesort(event);
            case "mergesort" -> mergesort(event);
            case "sort" -> sort(event);
        }

    }

    @Override
    public void onReady(ReadyEvent event){
        guild = event.getJDA().getGuildById("580065346363457536");
        guild.updateCommands().addCommands(
                Commands.slash("kill","Stopt alle laufend Commands"),
                Commands.slash("kritik","Teile uns deine Kritik mit")
                        .addOption(OptionType.STRING, "kanal", "Kanalname", true, false),
                Commands.slash("clear","Löscht alle Nachrichten aus angegebenen Textkanälen")
                        .addOption(OptionType.STRING, "kanal", "Kanalname(all für alle Hauptkanäle),nichts für aktuellen Kanal", false, true),
                Commands.slash("play","Teile uns deine Kritik mit")
                        .addOption(OptionType.STRING, "name", "Url oder Name", true, false),
                Commands.slash("stop","Teile uns deine Kritik mit"),
                Commands.slash("skip","Teile uns deine Kritik mit"),
                Commands.slash("pause","Teile uns deine Kritik mit"),
                Commands.slash("shuffle","Teile uns deine Kritik mit"),
                Commands.slash("clown","Teile uns deine Kritik mit"),
                Commands.slash("queue","Teile uns deine Kritik mit"),
                Commands.slash("join","Teile uns deine Kritik mit")
                        .addOption(OptionType.STRING, "kanal", "Kanalname", false, true),
                Commands.slash("help","Teile uns deine Kritik mit"),
                Commands.slash("rec","Teile uns deine Kritik mit")
                        .addOption(OptionType.INTEGER, "index", "index", false, false),
                Commands.slash("now_playing","Teile uns deine Kritik mit"),
                Commands.slash("remove","Teile uns deine Kritik mit")
                .addOption(OptionType.INTEGER, "index", "index", false, false),
                Commands.slash("bogosort","Teile uns deine Kritik mit")
                .addOption(OptionType.STRING, "liste", "Liste", true, false),
                Commands.slash("bubblesort","Teile uns deine Kritik mit")
                .addOption(OptionType.STRING, "liste", "Liste", true, false),
                Commands.slash("mergesort","Teile uns deine Kritik mit")
                .addOption(OptionType.STRING, "liste", "Liste", true, false),
                Commands.slash("sort","Teile uns deine Kritik mit")
                .addOption(OptionType.STRING, "liste", "Liste", true, false),
                Commands.slash("settimeout","Teile uns deine Kritik mit")
                        .addOption(OptionType.INTEGER, "timeout", "timeout in s", true, false)
            ).queue();
        timer = new Timer();
        task = new Meow(channelmap.get("meow"));
        /*timer.schedule(task,3000,3000);*/
        timer.schedule(task,600000,600000);
        List <TextChannel> channels = guild.getTextChannels();
        List <VoiceChannel> voicechannels = guild.getVoiceChannels();
        for (TextChannel channel : channels){
            channelmap.put(channel.getName(),channel);
        }
        for (VoiceChannel channel :voicechannels){
            voicechannelmap.put(channel.getName(),channel);
        }
    }
    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (event.getName().equals("clear") && event.getFocusedOption().getName().equals("kanal")) {
            List<Command.Choice> options =channelmap.keySet().stream()
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                    .map(word -> new Command.Choice(word, word)) // map the words to choices
                    .collect(Collectors.toList());

            options.add(new Command.Choice("all","all"));
            event.replyChoices(options).queue();
        }
        if (event.getName().equals("join") && event.getFocusedOption().getName().equals("kanal")) {
            List<Command.Choice> options =voicechannelmap.keySet().stream()
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                    .map(word -> new Command.Choice(word, word)) // map the words to choices
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        System.out.println(event.getName());
        event.reply("Meow").queue();
        String content = event.getName();
        reset_timer();
        switch (event.getName()) {
            case "kill" :
                kill();
                return;
            case "settimeout":
                set_timeout(event);
                return;
        }
        Thread thread = new Thread(new Bot(guild,channelmap,voicechannelmap,content,event));
        thread.start();
        thread_list.add(thread);
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("Beschwerdeformula")) {
            String muell = event.getValue("muell").getAsString();
            if(!event.getMember().isOwner()){
                event.getMember().timeoutFor(30, TimeUnit.SECONDS);
            }
            event.reply("Kritik ist unerwünscht. "+event.getMember().getNickname()+" du Geringverdiener").setEphemeral(true).queue();
        }
    }
    public void modal(SlashCommandInteractionEvent event){
        TextInput subject = TextInput.create("muell", "Kritik", TextInputStyle.SHORT)
                .build();
        Modal modal = Modal.create("Beschwerdeformula", "Beschwerdeformula")
                .addComponents(ActionRow.of(subject))
                .build();
        event.replyModal(modal).queue();
    }
    public void sort(SlashCommandInteractionEvent event) {
        Thread thread = new Thread(new Bot(guild,channelmap,voicechannelmap,"bogosort",event));
        thread.start();
        Thread thread2 = new Thread(new Bot(guild,channelmap,voicechannelmap,"bogosort",event));
        thread2.start();
        Thread thread3 = new Thread(new Bot(guild,channelmap,voicechannelmap,"bogosort",event));
        thread3.start();
        while (!Thread.currentThread().isInterrupted()){
            if(!thread.isAlive() && !thread2.isAlive() &&!thread3.isAlive()){
                System.out.println("sort finished");
                return;
            }
            try{
                Thread.sleep(5000);
            }
            catch (Exception e){
                if(thread.isAlive()){
                    thread.interrupt();
                    System.out.println("Bogosort killed");
                }
                if(thread2.isAlive()){
                    thread2.interrupt();
                    System.out.println("Bubblesort killed");
                }
                if(thread3.isAlive()){
                    thread3.interrupt();
                    System.out.println("Mergesort killed");
                }
            }
        }

    }
    public void kill(){
        for (Thread thread: thread_list ){
            if(thread.isAlive()){
                thread.interrupt();
                System.out.println("Thread: "+thread.getName()+" killed");
            }
        }
        thread_list.clear();
    }
    public void bogosort(SlashCommandInteractionEvent event){
        ArrayList<Integer> list= new ArrayList<Integer>();
        String msg = (event.getOption("liste").getAsString());
        String[] splitted = msg.split("\\s+");
        String answer = "";
        int tries = 0;
        for(int i = 0; i<splitted.length;i++){
            try{
                list.add(Integer.valueOf(splitted[i]));
            }
            catch (NumberFormatException e){
                botzentrale.sendMessage("Ungültige Eingabe: "+splitted[i]+ " ist keine Zahl").queue();
                return;
            }
        }
        Bogosort bogosort = new Bogosort(list);
        list = bogosort.sort();
        if(list.isEmpty()){
            return;
        }
        tries= list.get(list.size()-1);
        list.remove(list.size()-1);
        answer = list.toString();
        botzentrale.sendMessage("Bogosort: Liste: "+answer+ " Anzahl Versuche:"+tries).queue();
        System.out.println("Bogosort finished");
    }
    public void mergesort(SlashCommandInteractionEvent event){
        ArrayList<Integer> list= new ArrayList<Integer>();
        String msg = (event.getOption("liste").getAsString());
        String[] splitted = msg.split("\\s+");
        String answer = "";
        int tries = 0;
        for(int i = 0; i<splitted.length;i++){
            try{
                list.add(Integer.valueOf(splitted[i]));
            }
            catch (NumberFormatException e){
                botzentrale.sendMessage("Ungültige Eingabe: "+splitted[i]+ " ist keine Zahl").queue();
                return;
            }
        }
        Mergesort mergesort = new Mergesort(list);
        list = mergesort.sort();
        if(list.isEmpty()){
            return;
        }
        tries= list.get(list.size()-1);
        list.remove(list.size()-1);
        answer = list.toString();
        botzentrale.sendMessage("Mergesort: Liste: "+answer+ " Anzahl Tauschoperationen:"+tries).queue();
        System.out.println("Mergesort finished");
    }
    public void bubblesort(SlashCommandInteractionEvent event){
        ArrayList<Integer> list= new ArrayList<Integer>();
        String msg = (event.getOption("liste").getAsString());
        String[] splitted = msg.split("\\s+");
        String answer = "";
        int operations = 0;
        for(int i = 0; i<splitted.length;i++){
            try{
                list.add(Integer.valueOf(splitted[i]));
            }
            catch (NumberFormatException e){
                botzentrale.sendMessage("Ungültige Eingabe: "+splitted[i]+ " ist keine Zahl").queue();
                return;
            }
        }
        Bubblesort bubblesort = new Bubblesort(list);
        list = bubblesort.sort();
        if(list.isEmpty()){
            return;
        }
        operations= list.get(list.size()-1);
        list.remove(list.size()-1);
        answer = list.toString();
        botzentrale.sendMessage("Bubblesort: Liste: "+answer+ " Anzahl Tauschoperationen:"+operations).queue();
        System.out.println("Bubblesort finished");
    }
    public void now_playing(){
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            botzentrale.sendMessage("There is no track playing currently.").queue();
            return;
        }
        String title = audioPlayer.getPlayingTrack().getInfo().title;
        String author = audioPlayer.getPlayingTrack().getInfo().author;

        botzentrale.sendMessage("Now playing: **`" + title + "`** by **`" + author + "`**.").queue();
    }
    public void help(){
        botzentrale.sendMessage("List of Command: ")
                .addContent("\n!join")
                .addContent("\n!play")
                .addContent("\n!pause")
                .addContent("\n!stop")
                .addContent("\n!skip")
                .addContent("\n!queue")
                .addContent("\n!shuffle")
                .addContent("\n!now_playing")
                .addContent("\n!clown")
                .addContent("\n!clear")
                .queue();
    }
    public void remove_from_queue(SlashCommandInteractionEvent event){
        int index = 0;
        index = (event.getOption("index")).getAsInt();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        if(index>10){
            botzentrale.sendMessage("Nur Zahlen bis 10 sind erlaubt").queue();
            return;
        }
        for (AudioTrack track : musicManager.scheduler.queue) {
            if (index-- == 0){
                if(musicManager.scheduler.queue.remove(track)) {
                    botzentrale.sendMessage("Der Track ")
                            .addContent("**`")
                            .addContent(track.getInfo().title.replace("\"", "").replace("*", "★"))
                            .addContent("`** by **`")
                            .addContent(track.getInfo().author.replace("\"", "").replace("*", "★"))
                            .addContent("`**")
                            .addContent("`wird aus der Queue entfernt.")
                            .queue();
                    return;
                }
                botzentrale.sendMessage("Fehler beim Entfernen").queue();
            }
        }
    }
    public void join(SlashCommandInteractionEvent event){
        AudioManager manager = guild.getAudioManager();
        String msg = "";
        if(!event.getOptions().isEmpty()){
            msg = (event.getOption("kanal")).getAsString();
        }
        if (msg.isEmpty()){
            VoiceChannel channel;
            try {
                channel = event.getMember().getVoiceState().getChannel().asVoiceChannel();
            }
            catch (NullPointerException e){
                channel = main;
            }
            if(!manager.isConnected()){
                manager.openAudioConnection(channel);
                return;
            }
            if(manager.getConnectedChannel().equals(channel) ){
                botzentrale.sendMessage("Bot already in Voice Channel:" +manager.getConnectedChannel().getName())
                        .addContent("\n"+" L + ratio")
                        .queue();
                return;
            }
            manager.openAudioConnection(channel);
        }
        else {
            VoiceChannel channel;
            if(!voicechannelmap.containsKey(msg)){
                botzentrale.sendMessage("Es existiert kein Voice-Channel mit diesen Namen").queue();
                return;
            }
            channel = voicechannelmap.get(msg);
            if (!manager.isConnected()){
                manager.openAudioConnection(channel);
                return;
            }
            if(manager.getConnectedChannel().equals(channel)){
                botzentrale.sendMessage("Bot already in Voice Channel: " +channel.getName())
                        .addContent("\n"+" L + ratio")
                        .queue();
                return;
            }
            manager.openAudioConnection(channel);
        }
    }
    public void clown(SlashCommandInteractionEvent event){
        List<Member> members =  event.getGuild().getMembers();
        Random rand = new Random();
        int random= rand.nextInt(members.size());
        meow.sendMessage(members.get(random).getNickname()+" ist der Clown des Tages").queue();
    }
    public void skip(SlashCommandInteractionEvent event){
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        if(audioPlayer.getPlayingTrack() == null){
            botzentrale.sendMessage("There is no track playing currently.").queue();
            return;
        }
        if(musicManager.scheduler.nextTrack()==1){
            botzentrale.sendMessage("There is no track next in queue.").queue();
            PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.player.stopTrack();
            PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.clear();
            return;
        }
        botzentrale.sendMessage("Skipped the current track. Now playing **`" + musicManager.audioPlayer.getPlayingTrack().getInfo().title + "`** by **`" + musicManager.audioPlayer.getPlayingTrack().getInfo().author + "`**.").queue();
    }
    public void shuffle(SlashCommandInteractionEvent event){
        Random rand = new Random();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        List<AudioTrack> bufferlist = new ArrayList<AudioTrack>(musicManager.scheduler.queue);
        while (!bufferlist.isEmpty()){
            AudioTrack randomTrack = bufferlist.get(rand.nextInt(bufferlist.size()));
            bufferlist.remove(randomTrack);
            musicManager.scheduler.queue.remove(randomTrack);
            musicManager.scheduler.queue.add(randomTrack);
        }
        botzentrale.sendMessage("Shuffling done").queue();
    }
    public void getQueue(){
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        String queue_message = "";
        int limit = 10;
        int i = 0;
        if(musicManager.scheduler.queue.isEmpty()){
            botzentrale.sendMessage("Queue ist leer").queue();
            return;
        }
        if(musicManager.scheduler.queue.size()<limit){
            limit=musicManager.scheduler.queue.size();
        }
        for (AudioTrack track : musicManager.scheduler.queue) {
            if (i++ == limit  ) break;
            queue_message = queue_message + (i)+":";
            queue_message = queue_message + "**`";
            queue_message = queue_message + track.getInfo().title.replace("\"", "").replace("*", "★");
            queue_message = queue_message + "`** by **`";
            queue_message = queue_message + track.getInfo().author.replace("\"", "").replace("*", "★");
            queue_message = queue_message + "`**";
            queue_message = queue_message + "\n";
        }
        botzentrale.sendMessage("Next "+limit+" in Queue:").queue();
        botzentrale.sendMessage(queue_message).queue();

    }
    public void pause() {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        if (!musicManager.scheduler.player.isPaused()) {
            musicManager.scheduler.player.setPaused(true);
            botzentrale.sendMessage("Player wird pausiert").queue();
            return;
        }
        musicManager.scheduler.player.setPaused(false);
        botzentrale.sendMessage("Player spielt weiter").queue();
    }
    public void stop(){
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        final AudioManager manager = guild.getAudioManager();
        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();
        manager.closeAudioConnection();
        botzentrale.sendMessage("Player stopt und die Queue wird geleert").queue();
    }
    public void play(SlashCommandInteractionEvent event){
        String url = (event.getOption("name").getAsString() );
        if(url.startsWith("http")){
            System.out.println(url);
        }
        else {
            url = "ytsearch:" + url + " audio";
            System.out.println(url);
        }
        final AudioManager manager = guild.getAudioManager();
        VoiceChannel channel;
        if (!manager.isConnected()){
            try {
                channel = event.getMember().getVoiceState().getChannel().asVoiceChannel();
            }
            catch (NullPointerException e){
                event.getChannel().asTextChannel().sendMessage("Nutzer in keinen Voicechannel, Bot tritt Illegal Rave Party bei").queue();
                channel = voicechannelmap.get("Illegal Rave Party");
            }
            manager.openAudioConnection(channel);
        }
        PlayerManager.getInstance().loadAndPlay(botzentrale, url);
    }
    public void play(String url, VoiceChannel channel){
        if(url.startsWith("http")){
            System.out.println(url);
        }
        else {
            url = "ytsearch:" + url + " audio";
            System.out.println(url);
        }
        final AudioManager manager = guild.getAudioManager();
        if (!manager.isConnected()){
            manager.openAudioConnection(channel);
        }
        PlayerManager.getInstance().loadAndPlay(botzentrale, url);
    }
    public void clear(SlashCommandInteractionEvent event) {
        String msg = "";
        TextChannel tchannel;
        if(!event.getOptions().isEmpty()){
            msg = (event.getOption("kanal")).getAsString();
        }
        if(msg.equals("")){
            tchannel = event.getChannel().asTextChannel();
            if(meow != tchannel && botzentrale != tchannel && allgemein != tchannel){
                tchannel.sendMessage("Ungültiger Channel").queue();
                return;
            }
            delete_channelhistory(tchannel);
            tchannel.sendMessage("Löschen abgeschlossen").queue();
            return;
        }
        if(msg.equals("all")){
            delete_channelhistory(botzentrale);
            delete_channelhistory(meow);
            delete_channelhistory(allgemein);
            botzentrale.sendMessage("Löschen abgeschlossen").queue();
            return;
        }
        List <TextChannel> channels = guild.getTextChannelsByName(msg,true);
        if(channels.isEmpty()){
            botzentrale.sendMessage("Es existiert kein Textchannel mit diesen Namen").queue();
            return;
        }
        TextChannel channel = channels.get(0);
        delete_channelhistory(channel);
        botzentrale.sendMessage("Löschen abgeschlossen").queue();
    }
    public void delete_channelhistory(TextChannel channel){
        MessageHistory history= new MessageHistory(channel);
        List<Message> messages = history.retrievePast(100).complete();
        if (messages.isEmpty()){
            return ;
        }
        messages.forEach(message -> {
            //message.delete().queue();
            message.delete().complete();
        });
    }
    public void get_recommendation(SlashCommandInteractionEvent event) {
        int index = 0;
        if (!event.getOptions().isEmpty()){
            index = event.getOption("index").getAsInt();
        }
        MessageHistory history= new MessageHistory(channelmap.get("music-vorschläge"));
        List<Message> messages = history.retrievePast(100).complete();
        int counter = 1;
        String send = "";
        if (index==0){
            for (Message message : messages) {
                send = send+"\n" +counter + ": " + message.getContentRaw();
                counter++;
            }
            botzentrale.sendMessage(send).queue();
        }
        else {
            VoiceChannel channel;
            try {
                channel = event.getMember().getVoiceState().getChannel().asVoiceChannel();
            }
            catch (NullPointerException e){
                channel = main;
            }
            play(messages.get(index).getContentRaw(),channel);
        }
    }
    public void set_timeout(SlashCommandInteractionEvent event){
        timeout = Integer.parseInt(event.getOption("timeout").getAsString())*1000;
        reset_timer();
    }
    public void reset_timer(){
        timer.cancel();
        timer = new Timer();
        task = new Meow(meow);
        timer.schedule(task,timeout,timeout);
    }
}