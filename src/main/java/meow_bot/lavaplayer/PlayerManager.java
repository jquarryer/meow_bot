package meow_bot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.*;
import com.sedmelluq.discord.lavaplayer.tools.*;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.*;


public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager(){
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, String trackUrl){
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
                textChannel.sendMessage("Adding  to queue **`")
                        .addContent(audioTrack.getInfo().title.replace("*","☆"))
                        .addContent("`** by **`")
                        .addContent(audioTrack.getInfo().author.replace("*","☆"))
                        .addContent("`**")
                        .queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                List<AudioTrack> tracks = new ArrayList<AudioTrack>();
                if (audioPlaylist.isSearchResult()) {
                    tracks = audioPlaylist.getTracks();
                    final AudioTrack[] audioTrack = {null};
                    tracks.stream().limit(1).forEach(e -> audioTrack[0] = e);
                    tracks.clear();
                    tracks.add(audioTrack[0]);
                } else {
                    tracks = audioPlaylist.getTracks();
                }
                if (!tracks.isEmpty()) {
                    System.out.println(tracks.get(0));
                    for (AudioTrack track : tracks) {
                        musicManager.scheduler.queue(track);

                    }
                    textChannel.sendMessage("Adding Playlist to queue   **`")
                            .addContent(audioPlaylist.getName())
                            .addContent("`**")
                            .addContent("\nFirst track in Playlist:   **`")
                            .addContent(tracks.get(0).getInfo().title.replace("*","☆"))
                            .addContent("`** by **`")
                            .addContent(tracks.get(0).getInfo().author.replace("*","☆"))
                            .addContent("`**")
                            .queue();
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }


    public static PlayerManager getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

}
