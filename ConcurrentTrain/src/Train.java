import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import TSim.*;

public class Train implements Runnable {
	private int trainId;
	private int speed;
	private int speedSim;
	private TSimInterface tsi;
	private Point position;
	private Semaphore[] semaphores;
	private boolean toUp;
	private boolean[] Sensor;

	public Train(TSimInterface t, int speedSim, int speed, int tId,
			Semaphore[] semsaphores) {
		trainId = tId;
		tsi = t;
		this.speed = speed;
		this.speedSim = speedSim;
		this.semaphores = semsaphores;
		position = new Point(0, 0);
		toUp = trainId == 2 ? true : false;
	}

	@Override
	public void run() {
		start(trainId);
		while (true) {
		try {
			SensorEvent se = tsi.getSensor(trainId);
			Sensor = new boolean[] {position.getX() == 0 && position.getY() == 0 ? true : false, 
							position.getX() == 14 && position.getY() == 11 ? true : false, 
							position.getX() == 14 && position.getY() == 11 ? true : false,};
			if (se.getStatus() == SensorEvent.ACTIVE) {
				if(toUp) { //down-to-up
					position.setLocation(se.getXpos(), se.getYpos());	//Update the train position
					sensorPosition();
					if (Sensor[1]) { 
						semaphores[1].acquire();
						System.err.println("Down-to-up: Semaphore 1 has acquired");
						System.err.println("I'm starting to go up");
					} else if (Sensor[2]) { 
						//Unnecessary in the case down to up 
					} else if (Sensor[3]) {	 
						stop(trainId);
						semaphores[2].acquire();
						System.err.println("Down-to-up: Semaphore 2 has acquired");
						tsi.setSwitch(3, 11, tsi.SWITCH_LEFT);
						start(trainId);
						semaphores[1].release();
						System.err.println("Down-to-up: Semaphore 1 has released");
					} else if (Sensor[4]) {
						stop(trainId);
						semaphores[2].acquire();
						System.err.println("Down-to-up: Semaphore 2 has acquired");
						tsi.setSwitch(3, 11, tsi.SWITCH_RIGHT);
						start(trainId);
					} else if (Sensor[5]) {
						stop(trainId);
						if (semaphores[3].tryAcquire()){
							System.err.println("Down-to-up: Semaphore 3 have acquired");
							tsi.setSwitch(4, 9, tsi.SWITCH_LEFT);
							start(trainId);
						}
						else {
							System.err.println("Down-to-up: Semaphore 3 have NOT acquired");
							tsi.setSwitch(4, 9, tsi.SWITCH_RIGHT);
							start(trainId);
						}
					} else if (Sensor[6]) {
						semaphores[2].release();
						System.err.println("Down-to-up: Semaphore 2 have released");
					} else if (Sensor[7]) {
						semaphores[2].release();
						System.err.println("Down-to-up: Semaphore 2 have released");
					} else if (Sensor[8]) {
						stop(trainId);
						semaphores[4].acquire();
						System.err.println("Down-to-up: Semaphore 4 have acquired");
						tsi.setSwitch(15, 9, tsi.SWITCH_RIGHT);
						semaphores[3].release();
						System.err.println("Down-to-up: Semaphore 3 have released");
						start(trainId);
					} else if (Sensor[9]) {
						stop(trainId);
						semaphores[4].acquire();
						System.err.println("Down-to-up: Semaphore 4 have acquired");
						tsi.setSwitch(15, 9, tsi.SWITCH_LEFT);
						start(trainId);
					} else if (Sensor[10]) {
						stop(trainId);
						if (semaphores[5].tryAcquire()){ 
							System.err.println("Down-to-up: Semaphore 5 have acquired");
							tsi.setSwitch(17, 7, tsi.SWITCH_RIGHT);
							start(trainId);
						} else {
							System.err.println("Down-to-up: Semaphore 5 have NOT acquired");
							tsi.setSwitch(17, 7, tsi.SWITCH_LEFT);
							start(trainId);
						}
					} else if (Sensor[11]) {
						semaphores[4].release();
						System.err.println("Down-to-up: Semaphore 4 have released");
					} else if (Sensor[12]) {
						semaphores[4].release();
						System.err.println("Down-to-up: Semaphore 4 have released");
					} else if (Sensor[13]) {
						stop(trainId);
						semaphores[6].acquire();
						System.err.println("Down-to-up: Semaphore 6 have acquired");
						start(trainId);
					} else if (Sensor[14]) {
						stop(trainId);
						semaphores[6].acquire();
						System.err.println("Down-to-up: Semaphore 6 have acquired");
						start(trainId);
					} else if (Sensor[15]) {
						semaphores[6].release();
						System.err.println("Down-to-up: Semaphore 6 have released");
					} else if (Sensor[16]) {
						semaphores[6].release();
						System.err.println("Down-to-up: Semaphore 6 have released");
					} else if (Sensor[17]) { //station
						System.err.println("Down-to-up: I'm in station");
						station(trainId);
					} else if (Sensor[18]) { //station
						System.err.println("Down-to-up: I'm in station");
						station(trainId);
					}
				}
				else {	//up-to-down
					position.setLocation(se.getXpos(), se.getYpos());
					sensorPosition();
					if (Sensor[18]) {
						System.err.println("I'm starting to go down");
						semaphores[5].acquire();
						System.err.println("Up-to-down:  Semaphore 5 have acquired");
					} else if (Sensor[17]) {
						//Unnecessary in the case down to up
					} else if (Sensor[15]) {
						stop(trainId);
						semaphores[6].acquire();
						System.err.println("Up-to-down:  Semaphore 6 have acquired");
						start(trainId);
					} else if (Sensor[16]) {
						stop(trainId);
						semaphores[6].acquire();
						System.err.println("Up-to-down:  Semaphore 6 have acquired");
						start(trainId);
					} else if (Sensor[13]) {
						semaphores[6].release();
						System.err.println("Up-to-down:  Semaphore 6 have released");
					} else if (Sensor[14]) {
						semaphores[6].release();
						System.err.println("Up-to-down:  Semaphore 6 have released");
					} else if (Sensor[11]) {
						stop(trainId);
						semaphores[4].acquire();
						System.err.println("Up-to-down:  Semaphore 4 have acquired");
						semaphores[5].release();
						System.err.println("Up-to-down:  Semaphore 5 have released");
						tsi.setSwitch(17, 7, tsi.SWITCH_RIGHT);
						start(trainId);
					} else if (Sensor[12]) {
						stop(trainId);
						semaphores[4].acquire();
						System.err.println("Up-to-down:  Semaphore 4 have acquired");
						tsi.setSwitch(17, 7, tsi.SWITCH_LEFT);
						start(trainId);
					} else if (Sensor[10]) {
						stop(trainId);
						if (semaphores[3].tryAcquire()){ 
							System.err.println("Up-to-down:  Semaphore 3 have acquired");
							tsi.setSwitch(15, 9, tsi.SWITCH_RIGHT);
							start(trainId);
						} else {
							System.err.println("Up-to-down:  Semaphore 4 have NOT acquired");
							tsi.setSwitch(15, 9, tsi.SWITCH_LEFT); //Maybe Wrong. Test it
							start(trainId);
						}	
					} else if (Sensor[8]) {
						semaphores[4].release();
						System.err.println("Up-to-down:  Semaphore 4 have released");
					} else if (Sensor[9]) {
						semaphores[4].release();
						System.err.println("Up-to-down:  Semaphore 4 have released");
					} else if (Sensor[6]) {
						stop(trainId);
						semaphores[2].acquire();
						System.err.println("Up-to-down:  Semaphore 2 have acquired");
						tsi.setSwitch(4, 9, tsi.SWITCH_LEFT);
						start(trainId);
						semaphores[3].release();
						System.err.println("Up-to-down:  Semaphore 3 have released");
					} else if (Sensor[7]) {
						stop(trainId);
						semaphores[2].acquire();
						System.err.println("Up-to-down:  Semaphore 2 have acquired");
						tsi.setSwitch(4, 9, tsi.SWITCH_RIGHT);
						start(trainId);
					} else if (Sensor[5]) {
						stop(trainId);
						if (semaphores[1].tryAcquire()){
							System.err.println("Up-to-down:  Semaphore 1 have acquired");
							tsi.setSwitch(3, 11, tsi.SWITCH_LEFT);
							start(trainId);
						} else {
							System.err.println("Up-to-down:  Semaphore 1 have NOT acquired");
							tsi.setSwitch(3, 11, tsi.SWITCH_RIGHT);
							start(trainId);
						}
					} else if (Sensor[3]) {
						semaphores[2].release();
						System.err.println("Up-to-down:  Semaphore 2 have released");
					} else if (Sensor[4]) {
						semaphores[2].release();
						System.err.println("Up-to-down:  Semaphore 2 have released");
					} else if (Sensor[1]) { //station
						station(trainId);
					} else if (Sensor[2]) { //station
						station(trainId);
					} 
				}
			}
				
			} catch (CommandException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	private void start(int tId) {
		try {
			tsi.setSpeed(tId, speed);
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stop(int tId) {
		try {
			tsi.setSpeed(tId, 0);
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void station(int tId) {
		stop(trainId);
		try {
			Thread.sleep(500 + 2 * speedSim * Math.abs(speed));
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		toUp = !toUp;
		speed = -1 * speed;
		start(trainId);
	}
	
	private void sensorPosition(){
		Sensor = new boolean[] {position.getX() == 0 && position.getY() == 0 ? true : false, 
				position.getX() == 14 && position.getY() == 11 ? true : false, 
				position.getX() == 15 && position.getY() == 13 ? true : false, 
				position.getX() == 6 && position.getY() == 11 ? true : false,
				position.getX() == 6 && position.getY() == 13 ? true : false,
				position.getX() == 1 && position.getY() == 10 ? true : false,
				position.getX() == 7 && position.getY() == 9 ? true : false,
				position.getX() == 7 && position.getY() == 10 ? true : false,
				position.getX() == 12 && position.getY() == 9 ? true : false,
				position.getX() == 12 && position.getY() == 10 ? true : false,
				position.getX() == 19 && position.getY() == 8 ? true : false,
				position.getX() == 14 && position.getY() == 7 ? true : false,
				position.getX() == 14 && position.getY() == 8 ? true : false,
				position.getX() == 10 && position.getY() == 7 ? true : false,
				position.getX() == 10 && position.getY() == 8 ? true : false,
				position.getX() == 6 && position.getY() == 6 ? true : false,
				position.getX() == 9 && position.getY() == 5 ? true : false,
				position.getX() == 15 && position.getY() == 5 ? true : false,
				position.getX() == 14 && position.getY() == 3 ? true : false,};
	}
}
