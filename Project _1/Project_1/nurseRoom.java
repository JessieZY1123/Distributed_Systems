
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
//import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class nurseRoom implements Runnable {
	private String nurse;
	private int capacity;
	private int positiveTest = 0;
	public int maxSick = 3;
	public boolean isOver = false;
	PrincipalThread principalThread;

	Set<Integer> COVIDSet = new HashSet<Integer>();
	List<StudentThread> list = new LinkedList<>();

	public nurseRoom(int capacity) {
		this.capacity = capacity;
		this.nurse = "NurseThread_" + 1;

	}

	public int getRandom(int capacity) {
		int n = 0;
		while (true) {
			Random r = new Random();
			n = r.nextInt(capacity);
			if (!COVIDSet.contains(n)) {
				COVIDSet.add(n);
				break;
			}
		}
		return n;
	}

	public boolean hitTarget() {
		this.COVIDSet.clear();
		int num = getRandom(100);
		if (num < 3) {
			return true;
		} else {
			return false;
		}
	}

	public void run() {
		try {
			synchronized (this) {
				this.wait();
			}

			 for(int i = 0; i < list.size(); i++) {

				if(capacity>0) {
	               boolean isHit = hitTarget();
	                StudentThread student =list.get(i);
				    msg(student.getsName()+" enters the Nurse's room and waits to check COVID. ");
					
	                if(isHit) {
	            	   positiveTest++;
	                   student.setOver(true);

	               }
	                synchronized(student) {
	                    student.notify();
	                }
	               
	            }  
				capacity--;	
	            if(capacity==0) {
	            	 msg(" is checking ");
	            	 Thread.sleep(1000);
	            	capacity= 2;
	           }
			}

			if (positiveTest >= maxSick) {

				principalThread.setOver(true);
				principalThread.notify();
				msg(" more than 3 positive students are detected, all perple go home");
				return;
			}
			msg(" check over and go home.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis()) + "] " + getNurse() + ": " + m);
	}

	public String getNurse() {
		return nurse;
	}

	public void setNurse(String nurse) {
		this.nurse = nurse;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getPositiveTest() {
		return positiveTest;
	}

	public void setPositiveTest(int positiveTest) {
		this.positiveTest = positiveTest;
	}

	public PrincipalThread getPrincipalThread() {
		return principalThread;
	}

	public void setPrincipalThread(PrincipalThread principalThread) {
		this.principalThread = principalThread;
	}

	public List<StudentThread> getList() {
		return list;
	}

	public void setList(List<StudentThread> list) {
		this.list = list;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean over) {
		isOver = over;
	}

}
