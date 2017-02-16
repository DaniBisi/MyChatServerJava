package it.bisignano.mychatserver;
@FunctionalInterface
public interface Visitable {
	public String acceptVisit(HttpMessage msg);	
}
