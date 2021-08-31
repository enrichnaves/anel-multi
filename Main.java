package com.enrich;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
public class Main {
    static Boolean achou;
    private static final SynchronousQueue<String> FILA =
            new SynchronousQueue<>();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        achou = true;

        Runnable r1 = () -> {
            String letras = "qwertyuiopasdfghjklzxcvbnm";

            Random random = new Random();
            String mensagem = "";
            int index = -1;
            for( int i = 0; i < 80; i++ ) {
                index = random.nextInt( letras.length() );
                mensagem += letras.substring( index, index + 1 );
            }
            put(mensagem);
        };
        executor.execute(r1);


        ArrayList<Runnable> threads = new ArrayList<Runnable>();
        Runnable modelo = () ->{
            String msg;
            msg = take();
            if (msg!=null)
            {
                System.out.println(msg);
                char c;
                if (Character.isUpperCase(msg.charAt(79)))
                {
                    achou = false;
                }else {
                    for (int i = 0; i < msg.length(); i++) {
                        if (Character.isLowerCase(msg.charAt(i))) {
                            msg = msg.replaceFirst(String.valueOf(msg.charAt(i)), String.valueOf(Character.toUpperCase(msg.charAt(i))));
                            //dorme();
                            put(msg);
                            break;
                        }
                    }
                }
            }
        };

        for (int i=0;i<30;i++){
            threads.add(modelo);
        }

        while(achou)
        {
            for (Runnable i: threads)
            {
                if(!achou)
                {
                    break;
                }
                executor.execute(i);
            }
        }
        executor.shutdown();
    }

    private static String take() {
        try {
            return FILA.poll(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return "Exceção!";
        }
    }

    private static void put(String mensagem) {
        try {
            FILA.put(mensagem);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private static void dorme()
    {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
