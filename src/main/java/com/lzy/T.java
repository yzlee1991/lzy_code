package com.lzy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.entity.Boke;

public class T {
	public static void main(String[] args) {
		hibernate();
	}
	
	public static void spring(){
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
		H h=(H) applicationContext.getBean("H");
		System.out.println(h);
	}
	
	public static void hibernate(){
		Configuration configuration=new Configuration();
		configuration.configure();
		ServiceRegistryBuilder serviceRegistryBuilder=new ServiceRegistryBuilder();
		ServiceRegistry serviceRegistry=serviceRegistryBuilder.applySettings(configuration.getProperties()).buildServiceRegistry();
		SessionFactory sessionFactory=configuration.buildSessionFactory(serviceRegistry);
		Session session=sessionFactory.getCurrentSession();
		Transaction transaction=session.beginTransaction();
		
		Boke boke=new Boke();
		boke.setName("lzy");
		
		session.save(boke);
		transaction.commit();
		sessionFactory.close();
		
	}
}
