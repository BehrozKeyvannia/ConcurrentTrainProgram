import TSim.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Lab1 {
	private final static int maxspeedTrain = 15;
	private final static int maxspeedSim = 100;

	private Thread first;
	private Thread second;
	private TSimInterface tsi;
	private static Semaphore [] semaphores;

	public static void main(String[] args) {
		Lab1 lab1 = new Lab1(args);
		lab1.start();
	}

	public Lab1(String[] args) {
		tsi = TSimInterface.getInstance();
		int speedSim;
		int speed1;
		int speed2;
		int len = args.length;
		
		//Arguments handling. If speed is missing set a random integer between 1 and maxspeed.
		if(len==0){
			speedSim = maxspeedSim;
			speed1 = randInt();
			speed2 = randInt();
		}
		else if(len==1){
			speedSim = maxspeedSim;
			speed1 = Integer.parseInt(args[0]);
			speed2 = randInt();
		}
		else if(len==2){
			speedSim = maxspeedSim;
			speed1 = Integer.parseInt(args[0]);
			speed2 = Integer.parseInt(args[1]);
		}
		else {
			speedSim = Integer.parseInt(args[2]);
			speed1 = Integer.parseInt(args[0]);
			speed2 = Integer.parseInt(args[1]);
		}
		
		//Set debugger
		tsi.setDebug(false);
		//Initiate semaphores
		semaphores = new Semaphore [] {new Semaphore(1, true),
		     new Semaphore(1, true), new Semaphore(1, true), new Semaphore(1, true), 
		     new Semaphore(1, true), new Semaphore(1, true), 
		     new Semaphore(1, true), new Semaphore(1, true), 
		     new Semaphore(1, true)};
		//Initiate threads
		first = new Thread(new Train(tsi,speedSim, speed1, 1, semaphores));
		second = new Thread(new Train(tsi,speedSim, speed2, 2, semaphores));
	}

	private void start() {
		first.start();
		second.start();
	}
	
	private static int randInt() {		
	    Random rand = new Random();
	    int randomNum = rand.nextInt((maxspeedTrain - 1) + 1) + 1;
	    return randomNum;
	}
}
