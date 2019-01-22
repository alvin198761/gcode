package org.alvin.code.v2.sys.mock;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueDemo {
	//阻塞队列
	private LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
	//非阻塞
	private ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();

	/**
	 * 阻塞队列生产
	 */
	public void blockProd() {

		while (true) {
			try {
				String task = UUID.randomUUID().toString();
				System.out.println("阻塞队列生产任务：" + task);
				linkedBlockingQueue.put(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//5秒一次
			try {
				TimeUnit.SECONDS.sleep(5);
				System.out.println("等待五秒之后");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 阻塞队列消费
	 */
	public void blockComs() {
		while (true) {
			try {
				String task = linkedBlockingQueue.take(); //这里会卡住，一直等到有新的数据
				System.out.println("阻塞队列消费任务：" + task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 非阻塞生产
	 */
	public void queueProd() {
		while (true) {
			String task = UUID.randomUUID().toString();
			System.out.println("非阻队列生产任务：" + task);
			concurrentLinkedQueue.offer(task);
			//5秒一次
			try {
				TimeUnit.SECONDS.sleep(2);
				System.out.println("等待二秒之后");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 非阻队列消费
	 */
	public void queueComs() {
		while (true) {
			//这个if 纯粹是为了打印判断的
			if (concurrentLinkedQueue.isEmpty()) {
				System.out.println("来了，发现是空的");
			} else {
				System.out.println("发现有数据，全部消费完");
			}
			while (!concurrentLinkedQueue.isEmpty()) {
				String task = concurrentLinkedQueue.poll(); //这里会卡住，一直等到有新的数据
				System.out.println("非阻队列消费任务：" + task);
			}
			try {
				System.out.println("等待十秒");
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
//		QueueDemo demo = new QueueDemo();
		//阻塞队列
//		new Thread(demo::blockProd).start();
//		new Thread(demo::blockComs).start();
		//非阻
//		new Thread(demo::queueProd).start();
//		new Thread(demo::queueComs).start();
		System.out.println("\u6d48\u6c5f\u533a");
	}
}
