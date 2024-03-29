package fr.fistin.limbo.util.logger;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 13:30
 */
public enum ChatColor {

    BLACK('0', 0x00),
    DARK_BLUE('1', 0x1),
    DARK_GREEN('2', 0x2),
    DARK_AQUA('3', 0x3),
    DARK_RED('4', 0x4),
    DARK_PURPLE('5', 0x5),
    GOLD('6', 0x6),
    GRAY('7', 0x7),
    DARK_GRAY('8', 0x8),
    BLUE('9', 0x9),
    GREEN('a', 0xA),
    AQUA('b', 0xB),
    RED('c', 0xC),
    LIGHT_PURPLE('d', 0xD),
    YELLOW('e', 0xE),
    WHITE('f', 0xF),
    MAGIC('k', 0x10, true),
    BOLD('l', 0x11, true),
    STRIKETHROUGH('m', 0x12, true),
    UNDERLINE('n', 0x13, true),
    ITALIC('o', 0x14, true),
    RESET('r', 0x15);

    public static final char COLOR_CHAR = '\u00A7';

    private final String toString;

    private final char code;
    private final int intCode;
    private final boolean format;

    ChatColor(char code, int intCode) {
        this(code, intCode, false);
    }

    ChatColor(char code, int intCode, boolean format) {
        this.toString = new String(new char[]{COLOR_CHAR, code});
        this.code = code;
        this.intCode = intCode;
        this.format = format;
    }

    public char getCode() {
        return this.code;
    }

    public int getIntCode() {
        return this.intCode;
    }

    public boolean isFormat() {
        return this.format;
    }

    @Override
    public String toString() {
        return this.toString;
    }

}
