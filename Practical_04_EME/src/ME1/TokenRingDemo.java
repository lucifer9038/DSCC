package ME1;

import java.util.Iterator;
import java.util.Random;

class Process extends Thread{
	private int id;
	private volatile boolean hashToken;
	private boolean wantsToEnterCS;
	private Process nextProcess;
	
	private static final Random random1= new Random();
	
	public Process(int id)
	{
		this.id=id;
		this.hashToken=false;
		this.wantsToEnterCS=false;
	}
	public void setNextProcess(Process next)
	{
		this.nextProcess=next;
	}
	public void receiveToken() {
		hashToken=true;
	}
	private void enterCriticalSection() {
		System.out.println("Process " +id+ " ENTERS critical Section.");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Process "+id+ " EXITS Critical Section");
	}
	@Override
	public void run() {
		while (true) {
			if (hashToken) {
				wantsToEnterCS=random1.nextBoolean();
				if (wantsToEnterCS) {
					enterCriticalSection();
				}
				hashToken=false;
				System.out.println("Process "+id+ " passes token to process "+nextProcess.id);
				nextProcess.receiveToken();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}


public class TokenRingDemo {

	public static void main(String[] args) {
		int n=5;
		Process[] processes =new Process[n];
		
		for(int i=0;i<n;i++)
		{
			processes[i] = new Process(i+1);
		}
		for(int i=0;i<n;i++)
		{
			processes[i].setNextProcess(processes[(i+1)%n]);
		}
		for(int i=0;i<n;i++)
		{
			processes[i].start();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		processes[0].receiveToken();

	}

}
