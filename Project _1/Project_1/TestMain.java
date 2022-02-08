

public class TestMain {
    public static void main(String[] args) {

        int capacity = 20;

        PrincipalThread principalThread = new PrincipalThread(1);
        Thread pThread = new Thread(principalThread);
        InstructorThread ELA = new InstructorThread(4,1,"ELA");
        InstructorThread MATH = new InstructorThread(4,1,"MATH");
        nurseRoom nurse = new nurseRoom(2);
        principalThread.setELA(ELA);
        principalThread.setMATH(MATH);
        principalThread.setNurseThread(nurse);
        Thread ELAThread = new Thread(ELA);
        Thread MATHThread = new Thread(MATH);
        Thread nurseThread = new Thread(nurse);

        nurseThread.start();


        for (int i = 0; i < capacity;i++){
            StudentThread studentThread = new StudentThread(i + 1);
            studentThread.setELA(ELA);
            studentThread.setMATH(MATH);

            studentThread.setPrincipalThread(principalThread);

            Thread sThread = new Thread(studentThread);
            sThread.start();
        }
        try {
        	Thread.sleep(2500);
        	pThread.start();
            MATHThread.start();
            ELAThread.start();
        }catch (Exception e){

        }
    }}
