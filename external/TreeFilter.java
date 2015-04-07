package external;

import preferences.Regex;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Filter;

/**
 * <p>This class represents a tree filter, which defines 
 * the nodes that are accepted to remain in the tree.</p>
 * (implements the interface Filter<Tree> from the stanford package).
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class TreeFilter implements Filter<Tree> {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean accept(Tree arg0) {
		return !arg0.nodeString().endsWith(Regex.TREE2REMOVE);
	}
}
