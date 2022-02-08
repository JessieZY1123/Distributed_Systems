

import java.util.ArrayList;
import java.util.List;

public class InstructorThread implements Runnable {
	public int capacity;
	public int id;
	public String name;
	public boolean isOver =false;

	public List<StudentThread> list;

	public InstructorThread(int capacity, int id, String name) {
		this.capacity = capacity;
		this.name = name + "_InstructorThread_" + id;
		list =  new ArrayList<StudentThread>();
	}

	
    public void run() {
        try {

            int k =0;
            while (k<3) {
                if (isOver) {
                    return;
                }
           
                    synchronized(this) {
                        this.wait();
                    }

                    printlnMsg("Teach NO." + (k+1)+ " class" );
  
                    for(StudentThread student :list){
                        synchronized(student) {
                            student.notify();
                        }

                    }
                    k++;
          
                   
                    synchronized(this) {
                        this.wait();
                    }

                if(k==3){
                	printlnMsg(" Teaching finished, ready to go home.");
                	Thread.sleep(1000);

                }else {
                	printlnMsg("Notify the students to have a break");
                }
                    for(StudentThread student :list){
                        synchronized(student) {
                            student.notify();
                        }

                    }
                    list.clear();
                    Thread.sleep(1000);
            }

        }catch (InterruptedException e){
        		e.printStackTrace();
        }



}


	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	//Add students to each classroom
	 public boolean addStudent(StudentThread student){
	        synchronized(this){
	            if(list.size()<4) {
	                list.add(student);
	        //        printlnMsg("list:"+list.size()+"__--------"+student.getsName());
	                if(list.size()==4){
	                    student.setLastOne(true);
	                }
	                return true;
	            }

	        }
	      return false;
	    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void printlnMsg(String m) { 
		 System.out.println("["+(System.currentTimeMillis())+ "] "+getName()+": "+m);
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
