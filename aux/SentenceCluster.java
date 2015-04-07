package aux;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import core.Sentence;

/**
 * <p>This class represents a sentence cluster.</p>
 *  
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class SentenceCluster extends Cluster implements Comparable{
	
	private double value;
	
	/**
	 * <p>Constructor.</p>
	 */
	public SentenceCluster(){
		super(new Sentence(), new LinkedList<Sentence>());
		this.value = 0;
	}
	
	/**
	 * <p>Constructor.</p>
	 * 
	 * @param value centroid similarity value.
	 * @param centroid cluster centroid. 
	 * @param values sentences associated to the current centroid.
	 */
	public SentenceCluster(double value, Sentence centroid, Collection<Sentence> values){
		super(centroid, values);
		this.setValue(value);
	}
	
	/**
	 * <p>Constructor.</p>
	 * <p>Builds a cluster from another cluster.</p>
	 * 
	 * @param cluster the cluster to be based on.
	 */
	public SentenceCluster(SentenceCluster cluster){
		super(cluster.getCentroid(), cluster.getValues());
		this.setValue(cluster.getValue()); 
	}

	/** Getters & Setters **/
	public double getValue() { return value; }
	public Collection getValues() { return this.values; }
	public void setValue(double value) { this.value = value; }
	public int size() { return this.values.size(); }
	
	@SuppressWarnings("unchecked")
	public void addValue(Sentence sentence){ ((Collection<Sentence>)this.values).add(sentence); }
	public void addFirstValue(Sentence sentence){ 
		LinkedList<Sentence> values = new LinkedList<Sentence>((Collection<Sentence>)this.values);
		values.addFirst(sentence);
		this.values = new LinkedList<Sentence>(values);
		
	}
	public void removeValue(Sentence sentence){ this.values.remove(sentence); }
	public boolean containsValue(Sentence sentence) { return this.values.contains(sentence); }

	@Override
	public int compareTo(Object arg0) {
		SentenceCluster current = (SentenceCluster) arg0;
		
		if (this.values.size() == 0)
			return -1;
		if (current.getValues().size() == 0)
			return 1;
		
		LinkedList<Sentence> thisValues = new LinkedList<Sentence>((Collection<Sentence>)this.values);
		Collections.sort(thisValues);
		Sentence thisFirst = thisValues.getFirst();
		
		LinkedList<Sentence> argValues = new LinkedList<Sentence>((Collection<Sentence>)current.getValues());
		Collections.sort(argValues);
		Sentence argFirst = argValues.getFirst();
		
		return thisFirst.compareTo(argFirst);
	}
}

