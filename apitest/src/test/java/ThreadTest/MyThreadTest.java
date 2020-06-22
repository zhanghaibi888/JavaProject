package ThreadTest;

public class MyThreadTest {

	public static void main(String[] args) {

		System.out.println("开始");

		new MyThread().start();
		new MyThread2().start();

		System.out.println("结束");
	}

}

class MyThread extends Thread {

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			System.out.println(Thread.currentThread().getName() + " " + i);

		}
	}
}

class MyThread2 extends Thread {

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			System.out.println(Thread.currentThread().getName() + " " + i);

		}
	}
}