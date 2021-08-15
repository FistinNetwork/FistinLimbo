package fr.fistin.limbo;

import fr.fistin.limbo.util.References;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 13:30
 */
public class LimboBootstrap {

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0D) {
            System.err.println("*** ERROR *** " + References.NAME + " requires Java >= 8 to function!");
            return;
        }

        final LimboConfiguration limboConfiguration = LimboConfiguration.load();

        if (limboConfiguration != null) {
            final Limbo limbo = new Limbo(limboConfiguration);

            limbo.start();
        }
    }

}
