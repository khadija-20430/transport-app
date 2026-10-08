package transport.core;
public interface Suspendable {
    
    void suspendre();
    void reactiver();
    public boolean estSuspendu();
    public String getEtat(); 
    
}
