package fr.axa.application;

import java.util.Set;
import java.util.HashSet;

import javax.ws.rs.core.Application;

import fr.axa.services.ServiceMessage;
import fr.axa.services.ServiceText;
import fr.axa.services.ServiceJson;

public class ApplicationAxa extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	public ApplicationAxa(){
		singletons.add(new ServiceJson());
		singletons.add(new ServiceText());
		singletons.add(new ServiceMessage());
	}
	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
