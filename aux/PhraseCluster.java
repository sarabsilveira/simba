package aux;

import java.util.Collection;
import java.util.LinkedList;

import core.Phrase;
import core.Sentence;

public class PhraseCluster extends Cluster {

	private double value;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 */
	public PhraseCluster() {
		super(new Phrase(new Sentence(), null, -1), new LinkedList<Phrase>());
		this.value = 0;
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * 
	 * @param value
	 *            centroid similarity value.
	 * @param centroid
	 *            cluster centroid.
	 * @param values
	 *            sentences associated to the current centroid.
	 */
	public PhraseCluster(double value, Phrase centroid,
			Collection<Phrase> values) {
		super(centroid, values);
		this.setValue(value);
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a cluster from another cluster.
	 * </p>
	 * 
	 * @param cluster
	 *            the cluster to be based on.
	 */
	public PhraseCluster(PhraseCluster cluster) {
		super(cluster.getCentroid(), cluster.getValues());
		this.setValue(cluster.getValue());
	}

	/** Getters & Setters **/
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public void addValue(Phrase phrase) {
		((Collection<Phrase>) this.values).add(phrase);
	}

	public void addValues(Collection<Phrase> phrases) {
		((Collection<Phrase>) this.values).addAll(phrases);
	}

	public void removeValue(Phrase phrase) {
		this.values.remove(phrase);
	}

	public boolean containsValue(Phrase phrase) {
		return this.values.contains(phrase);
	}

}
