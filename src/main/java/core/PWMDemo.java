package core;

import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;
import io.silverspoon.bulldog.raspberrypi.RaspiNames;
import io.silverspoon.bulldog.raspberrypi.bcm.PwmClockDivider;
import io.silverspoon.bulldog.raspberrypi.pwm.RaspiPwm;

public final class PWMDemo {

    private PWMDemo() {
    }

    public static void main(String... args) {
        Board b = Platform.createBoard();
        final long wait = 50;
        RaspiPwm pwm = new RaspiPwm(b.getPin(RaspiNames.PWM_PIN));
        System.out.println("Got the PWM pin.");
        pwm.setup();
        pwm.setDivider(PwmClockDivider.BCM2835_PWM_CLOCK_DIVIDER_16);
        if (!pwm.isSetup()) {
            throw new IllegalStateException("Cannot proceed further, PWM pin"
                    + "was not setup properly.");
        }
        System.out.println("Pwm successfully setup.");
        int round = 0;
        final int rounds = 1000;
        final double highestPercentage = 1.0;
        final double onePercent = 0.01;
        final double startingP = 0.1;
        while (round < rounds) {
            try {
                System.out.println("Iteration no :" + ++round);
                for (double i = startingP; i < highestPercentage;
                     i += onePercent) {
                    pwm.setDuty(i);
                    Thread.sleep(wait);
                }
                for (double i = highestPercentage; i > 0.0; i -= onePercent) {
                    pwm.setDuty(i);
                    Thread.sleep(wait);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
