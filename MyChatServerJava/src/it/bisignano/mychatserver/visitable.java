package it.bisignano.mychatserver;
@FunctionalInterface
public interface visitable {
	public String acceptVisit(HttpMessage msg);	
}
