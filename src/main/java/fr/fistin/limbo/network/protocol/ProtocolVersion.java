package fr.fistin.limbo.network.protocol;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 08:31
 */
public enum ProtocolVersion {

    HANDSHAKE(-1),
    PROTOCOL_1_8(47), // Same for all 1.8 versions
    PROTOCOL_1_9(107),
    PROTOCOL_1_9_1(108),
    PROTOCOL_1_9_2(109),
    PROTOCOL_1_9_4(110), // Same as 1.9.3
    PROTOCOL_1_10(210), // Same for all 1.10 versions
    PROTOCOL_1_11(315),
    PROTOCOL_1_11_2(316), // Same as 1.11.1
    PROTOCOL_1_12(335),
    PROTOCOL_1_12_1(338),
    PROTOCOL_1_12_2(340),
    PROTOCOL_1_13(393),
    PROTOCOL_1_13_1(401),
    PROTOCOL_1_13_2(404),
    PROTOCOL_1_14(477),
    PROTOCOL_1_14_1(480),
    PROTOCOL_1_14_2(485),
    PROTOCOL_1_14_3(490),
    PROTOCOL_1_14_4(498),
    PROTOCOL_1_15(573),
    PROTOCOL_1_15_1(575),
    PROTOCOL_1_15_2(578),
    PROTOCOL_1_16(735),
    PROTOCOL_1_16_1(736),
    PROTOCOL_1_16_2(751),
    PROTOCOL_1_16_3(753),
    PROTOCOL_1_16_5(754), // Same as 1.16.4
    PROTOCOL_1_17(755),
    PROTOCOL_1_17_1(756),

    ;

    private final int id;

    ProtocolVersion(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ProtocolVersion getVersionById(int id) {
        for (ProtocolVersion version : values()) {
            if (version.getId() == id) {
                return version;
            }
        }
        return null;
    }

}
