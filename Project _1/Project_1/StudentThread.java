
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
/**
 * 
 * @author ZIYAN JIANG
 *
 */
public class StudentThread implements Runnable {

	private int studentId; 
	private String sName;
	private boolean isDoHealthQuestion;
	private boolean COVID = false;
	String msg;
	public boolean isOver = false; // flag for go home
	public boolean lastOne = false;
	/**
	 * ELA
	 */
	public boolean class1 = false;
	/**
	 * MATH
	 */
	public boolean class2 = false;
	/**
	 * PHy
	 */
	public boolean class3 = false;
	public PrincipalThread principalThread;
	public InstructorThread ELA;
	public InstructorThread MATH;

	public List<String> className = new ArrayList<>(3);
	
	public static long time = System.currentTimeMillis();

	public StudentThread(int studentId) {
		this.studentId = studentId;
		this.sName = "Student_" + studentId;
		isDoHealthQuestion = false;

	}
/**
 * Return a random number for the student who forgot to take the questionnaire
 * @param capacity
 * @return num
 */
	public static int getRandom(int capacity) {
		Set<Integer> forget = new HashSet<Integer>();
		int num = 0;
		while (true) {
			Random r = new Random();
			num = r.nextInt(capacity);
			if (!forget.contains(num)) {
				forget.add(num);
				break;
			}
		}
		return num;
	}

	public boolean isDoHealthQuestionaire(int studentId) {

		if (getRandom(20) < 20 * 0.85) {
			isDoHealthQuestion = true;
		}
		return isDoHealthQuestion;

	}

	public void waitingTocall(StudentThread student) {
		Object schoolyard = new Object();
		synchronized (schoolyard) {
			principalThread.addWaitStudent(schoolyard, this);
			while (true) {
				try {

					printlnMsg("is waiting in the schoolyard ");
					schoolyard.wait();
					break;

				} catch (InterruptedException e) {
					
					continue;
				}
			}
		}
	}

	public void run() {

		try {
			isDoHealthQuestionaire(this.getStudentId());
			 Thread.sleep(1000);
			printlnMsg("is arrived at school. ");
			 
			waitingTocall(this);
			 
			if (isOver) {
				printlnMsg("forget to take Health Questionnaite, go home now！");
				return;
			}

			synchronized (this) {
				this.wait(1000);
			}

			if (COVID) {
				synchronized (this) {
					this.wait();
				}
				if (isOver) {
					printlnMsg("COVID POSITIVE，go home now!!!");
					return;
				}

			}
			
			int k = 0;
			Thread.sleep(2000);
			while (true) {
		//		System.out.println("------------");
				if (isOver) {
					return;
				}
				if (!class1) {
					class1 = ELA.addStudent(this);
					if (class1) {
						k++;
						printlnMsg(" ready to take ELA class");
						className.add("ELA");
						if (lastOne) {
							lastOne = false;
						}

						synchronized (this) {
							this.wait();
						}
						printlnMsg("The NO."+ k +" time study ELA class");
						synchronized (this) {
							this.wait();
						}
						if (k == 3) {
				//			System.out.println("====================");
							break;	
							
						}
						printlnMsg("Class Break");
						Thread.sleep(1000);
						continue;
					}
				}

				if (!class2) {
					class2 = MATH.addStudent(this);
					if (class2) {
						k++;
						printlnMsg(" ready to take MATH class");
						className.add("MATH");
						if (lastOne) {
							lastOne = false;
							Thread.sleep(2500);
							synchronized (principalThread) {
								principalThread.notify();
								printlnMsg("------------Notify Principle------------");
//								if()ELA.notify();
//								MATH.notify();
							}
						}

						synchronized (this) {
							this.wait();
						}
						
						printlnMsg("The NO."+ k +" time study  MATH class");
						synchronized (this) {
							this.wait();
						}
						if (k == 3) {
							break;
						}
						printlnMsg("Class Break");
						Thread.sleep(1000);
						continue;
					}

				}
				if (k < 3) {
					k++;
					principalThread.addStudent(this);
					printlnMsg(" ready to take PHYS-ED classs");
					className.add("PHYS");

					synchronized (this) {
						this.wait();
					}
					printlnMsg("The NO."+k+" time study PHYS-ED classs ");

					synchronized (this) {
						this.wait();
					}
					if (k == 3) {
						break;
					}
					printlnMsg("Class Break");
				}

			}

			synchronized (this) {
				this.wait(1000);
			}



		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public int getRandomSleepTime(int time, int num) {
		Random ran = new Random();
		return time + ran.nextInt(num);
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public boolean isLastOne() {
		return lastOne;
	}

	public void setLastOne(boolean lastOne) {
		this.lastOne = lastOne;
	}

	public void printlnMsg(String m) {
		System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getsName() + ": " + m);
	}


	public boolean isCOVID() {
		return COVID;
	}

	public boolean isClass1() {
		return class1;
	}

	public void setClass1(boolean class1) {
		this.class1 = class1;
	}

	public boolean isClass2() {
		return class2;
	}

	public void setClass2(boolean class2) {
		this.class2 = class2;
	}

	public boolean isClass3() {
		return class3;
	}

	public void setClass3(boolean class3) {
		this.class3 = class3;
	}

	public InstructorThread getELA() {
		return ELA;
	}

	public void setELA(InstructorThread ELA) {
		this.ELA = ELA;
	}

	public InstructorThread getMATH() {
		return MATH;
	}

	public void setMATH(InstructorThread mATH) {
		MATH = mATH;
	}

	public List<String> getClassName() {
		return className;
	}

	public void setClassName(List<String> className) {
		this.className = className;
	}

	public void setCOVID(boolean COVID) {
		this.COVID = COVID;
	}
	public int getClassSize() {
		return className.size();
	}
	public PrincipalThread getPrincipalThread() {
		return principalThread;
	}

	public void setPrincipalThread(PrincipalThread principalThread) {
		this.principalThread = principalThread;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean over) {
		isOver = over;
	}
}
