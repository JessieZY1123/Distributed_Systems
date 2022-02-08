

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PrincipalThread implements Runnable {
	private String principalName;
	public boolean isOver = false;

	public InstructorThread ELA;
	public InstructorThread MATH;
	
	public static LinkedHashMap<Object, StudentThread> waitingStudent;
	
	/* 
	 * The student who already take Health Questionnaire
	 */
	public static LinkedHashMap<Integer, StudentThread> studentMap;
	
	public nurseRoom nurseThread;
	public List<StudentThread> list;
	
	Set<Integer> COVIDSet = new HashSet<Integer>();

	public PrincipalThread(int capacity) {
		principalName = "PrincipalThread";
		studentMap = new LinkedHashMap<>();
		waitingStudent = new LinkedHashMap<>();
		list=new ArrayList<>();

	}

	public  int getRandom(int capacity){
	    int n =0;
	    while(true){
	        Random r = new Random();
	        n = r.nextInt(capacity);
	        if(!COVIDSet.contains(n)){
	            COVIDSet.add(n);
	            break;
	        }
	    }
	    return n;
	}

	public  void addWaitStudent(Object schoolyard, StudentThread student) {
		synchronized(this) {
		waitingStudent.put(schoolyard, student);
	}}

	public  void checkHealthQ(StudentThread student) {
		if (!student.isDoHealthQuestionaire(student.getStudentId())) {
			student.setOver(true);
		} else {
			studentMap.put(student.getStudentId(), student);
		}
	}

	@Override
	public void run() {
		
		synchronized(this) {
		synchronized (waitingStudent) {
			printlnMsg("Principle start to check HealthQ ");
			for(Object obj:waitingStudent.keySet()){
				synchronized(obj) {
				checkHealthQ(waitingStudent.get(obj)); 
					     obj.notify();
					    }
			   }

		}}
		
		printlnMsg("Check over");
		
		 COVIDSet.clear();
		 int num = studentMap.size()/3;
		 
         for(int i =0;i<num;i++) {
          getRandom(studentMap.size());
         }
         int j =0;
         for(int id:studentMap.keySet()) {
             StudentThread student = studentMap.get(id);
             if(COVIDSet.contains(j)){
                 student.setCOVID(true);
                 nurseThread.getList().add(student); 
             }
            
             synchronized(student) {
                 student.notifyAll();
                
             }
             j++;
         }
         synchronized(nurseThread) {
             nurseThread.notify();
         }


         startTeach();


}
 
public void startTeach(){
     try {
         Thread.sleep(2000);
         int k = 0;
         while (true) {
             k++;
             synchronized (this) {
     
                 this.wait();
             }
             Thread.sleep(1000);

             if (isOver) {
                 for (int key : studentMap.keySet()) {
                     StudentThread student = studentMap.get(key);
                     student.setOver(true);
                 }
                 MATH.setOver(true);
                 ELA.setOver(true);
             }


             printlnMsg("The NO."+k+" time to start class ");

             synchronized (ELA) {
                 ELA.notify();
             }
             synchronized (MATH) {
                 MATH.notify();
             }
             for(StudentThread student:list){
                 synchronized (student){
                     student.notify();
                 }
             }
             Thread.sleep(2000);

			if(k!=3) {
				printlnMsg( "The NO."+k+" time to notify instructor to have a break. ");
			}
             synchronized (ELA) {
                 ELA.notify();
             }
             synchronized (MATH) {
                 MATH.notify();
             }
             synchronized (this) {
            	  for(StudentThread student:list){
                      synchronized (student){
                          student.notifyAll();
                      }
			}
           
             }
             
             list.clear();
             if (k == 3) {
                 printlnMsg("Study finished");
                 for (int key : studentMap.keySet()) {
                     StudentThread student = studentMap.get(key);
                     student.setOver(true);
                     synchronized(student) {
                         student.notify();
                     }
                     
                     String claS = "";
                     if(student.getClassSize() != 0) {
          			for (String str : student.getClassName()) {
          				//System.out.println(student.getClassSize());	
          				claS = claS + str + " ";
          			}
          			 
          			printlnMsg(student.getsName()+" takes " + claS + "class and go home");
                 }}
                 printlnMsg("Terminate");
                 break;
             }
             Thread.sleep(1000);
             
         }
     }catch(Exception e){
         e.printStackTrace();
     }
 }



	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}
	public void printlnMsg(String m) { 
		 System.out.println("["+(System.currentTimeMillis())+"] "+getPrincipalName()+": "+m);
	    }

    
	public InstructorThread getELA() {
		return ELA;
	}

	public void setELA(InstructorThread eLA) {
		ELA = eLA;
	}

	public InstructorThread getMATH() {
		return MATH;
	}

	public void setMATH(InstructorThread mATH) {
		MATH = mATH;
	}

	public static LinkedHashMap<Integer, StudentThread> getStudentMap() {
		return studentMap;
	}

	public static void setStudentMap(LinkedHashMap<Integer, StudentThread> studentMap) {
		PrincipalThread.studentMap = studentMap;
	}

	public nurseRoom getNurseThread() {
		return nurseThread;
	}

	public void setNurseThread(nurseRoom nurseThread) {
		this.nurseThread = nurseThread;
	}

	public Set<Integer> getCOVIDSet() {
		return COVIDSet;
	}

	public void setCOVIDSet(Set<Integer> cOVIDSet) {
		COVIDSet = cOVIDSet;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean over) {
		isOver = over;
	}
	public synchronized void addStudent(StudentThread student){
        synchronized(this){
                list.add(student);

            }

    }

}
