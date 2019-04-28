package com.barath.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SpringSecurityProtectedResourceApplicationTests {

	@Test
	public void contextLoads() throws CloneNotSupportedException {
		
		Car a =new Car("audi");
		System.out.println(a.toString());
		a.clone();
		System.out.println(a.toString());
	}
	
	class Car implements Cloneable{
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Car(String name) {
			super();
			this.name = name;
		}

		@Override
		public String toString() {
			return "Car [name=" + name + "]";
		}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			
			return new Car(null);
		}
		
		
	}
		
	
	interface ComplexClass{
		
		public void drawCar();
		
	}
	
}
