package fr.fistin.limbo.player.settings;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 22:43
 */
public class ClientSettings {

    private String locale;
    private byte viewDistance;
    private ChatMode chatMode;
    private boolean chatColors;
    private int displayedSkinParts;
    private MainHand mainHand;
    private boolean textFiltering;

    public ClientSettings() {}

    public ClientSettings(String locale, byte viewDistance, ChatMode chatMode, boolean chatColors, int displayedSkinParts, MainHand mainHand, boolean textFiltering) {
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.chatMode = chatMode;
        this.chatColors = chatColors;
        this.displayedSkinParts = displayedSkinParts;
        this.mainHand = mainHand;
        this.textFiltering = textFiltering;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public byte getViewDistance() {
        return this.viewDistance;
    }

    public void setViewDistance(byte viewDistance) {
        this.viewDistance = viewDistance;
    }

    public ChatMode getChatMode() {
        return this.chatMode;
    }

    public void setChatMode(ChatMode chatMode) {
        this.chatMode = chatMode;
    }

    public boolean isChatColors() {
        return this.chatColors;
    }

    public void setChatColors(boolean chatColors) {
        this.chatColors = chatColors;
    }

    public int getDisplayedSkinParts() {
        return this.displayedSkinParts;
    }

    public void setDisplayedSkinParts(int displayedSkinParts) {
        this.displayedSkinParts = displayedSkinParts;
    }

    public MainHand getMainHand() {
        return this.mainHand;
    }

    public void setMainHand(MainHand mainHand) {
        this.mainHand = mainHand;
    }

    public boolean isTextFiltering() {
        return this.textFiltering;
    }

    public void setTextFiltering(boolean textFiltering) {
        this.textFiltering = textFiltering;
    }

}
