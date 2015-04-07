package aux;

import java.util.Collection;

/**
 * <p>
 * This class represents an abstract Cluster.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public abstract class Cluster {

	protected Object centroid;
	protected Collection<?> values;

	public Cluster() {
	}

	public Cluster(Object centroid, Collection<?> values) {
		this.centroid = centroid;
		this.values = values;
	}

	public Object getCentroid() {
		return centroid;
	}

	public void setCentroid(Object centroid) {
		this.centroid = centroid;
	}

	@SuppressWarnings("unchecked")
	public Collection getValues() {
		return values;
	}

	@SuppressWarnings("unchecked")
	public void setValues(Collection values) {
		this.values = values;
	}
}
