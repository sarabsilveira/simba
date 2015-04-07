package core;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import preferences.Regex;
import preferences.Utils;
import edu.stanford.nlp.ling.StringLabel;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.ParseException;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import external.TreeFilter;

public class ManageTrees {

	/**
	 * <p>
	 * Removes a set of subtrees from a tree.
	 * </p>
	 * 
	 * @param original
	 *            the original tree.
	 * @param toRemove
	 *            the trees to be removed.
	 * 
	 * @return the tree without the subtrees.
	 */
	public static Tree removeTrees(Tree original, Collection<Tree> toRemove) {

		if (toRemove == null)
			return original;

		for (Tree tree : toRemove) {
			String newNode = tree.nodeString() + Regex.TREE2REMOVE;
			tree.label().setValue(newNode);
		}

		// All changes are made in a deeper copy of the original tree.
		Tree copy = original.deepCopy();
		// Removes all the subtrees marked to be removed.
		copy = copy.prune(new TreeFilter());

		if (copy == null)
			return original;

		// Cleaning the original tree
		for (Tree tree : toRemove) {
			String newNode = tree.nodeString().replaceAll(
					Regex.TREE2REMOVE + "+", "");
			tree.label().setValue(newNode);
		}

		return copy;
	}

	/**
	 * <p>
	 * Removes the set of phrases from the original tree.
	 * </p>
	 * 
	 * @param original
	 *            the tree from which will be removed the phrases.
	 * @param phrases
	 *            the phrases to be removed.
	 * 
	 * @return a Tree without the set of phrases.
	 */
	public static Tree removePhrases(Tree original, Set<Phrase> phrases) {

		Set<Tree> trees = new HashSet<Tree>();

		for (Phrase phrase : phrases)
			trees.add(phrase.getPhraseTree());

		Tree tree = ManageTrees.removeTrees(original, trees);

		return tree;
	}

	/**
	 * <p>
	 * Removes a tree from the original tree.
	 * </p>
	 * 
	 * @param original
	 *            the main tree.
	 * @param toRemove
	 *            the tree to be removed.
	 * 
	 * @return a tree without the given subtree.
	 */
	public static Tree removeTree(Tree original, Tree toRemove) {

		Set<Tree> trees = new HashSet<Tree>();
		trees.add(toRemove);

		return removeTrees(original, trees);
	}

	/**
	 * <p>
	 * Filters the parent tree if it contains the subtree given.
	 * </p>
	 * 
	 * @param parent
	 *            the tree to be filtered.
	 * @param subtree
	 *            the subtree to be removed.
	 * @return the parent tree without the subtree.
	 */
	public static Tree filterParentTree(Tree parent, Tree subtree) {

		Tree subtreeFromParent = retrieveContainingSubtree(parent, subtree);
		if (subtreeFromParent != null) {

			String node = subtreeFromParent.nodeString();

			parent = removeTree(parent, subtreeFromParent);
			subtreeFromParent.label().setValue(node);
		}

		return parent.deepCopy();
	}

	/**
	 * <p>
	 * Checks if a tree contains a given subtree.
	 * </p>
	 * 
	 * @param parent
	 *            the tree in which the subtree will be searched.
	 * @param subtree
	 *            the subtree to be searched.
	 * @return
	 */
	public static boolean containsTree(Tree parent, Tree subtree) {
		return retrieveContainingSubtree(parent, subtree) != null;
	}

	/**
	 * <p>
	 * Retrieves a tree which is contained in another tree.
	 * </p>
	 * 
	 * @param parent
	 *            the tree in which the subtree will be searched.
	 * @param subtree
	 *            the subtree to be searched.
	 * @return the tree if found; null otherwise.
	 */
	public static Tree retrieveContainingSubtree(Tree parent, Tree subtree) {

		Tree toReturn = null;

		if (parent.equals(subtree))
			toReturn = parent;

		List<Tree> children = parent.getChildrenAsList();

		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).equals(subtree)) {
				toReturn = children.get(i);
				break;
			} else
				toReturn = retrieveContainingSubtree(children.get(i), subtree);
		}

		return toReturn;
	}

	public static Tree retrieveMainTree(Tree tree) {

		String pattern = "/S(?:" + Regex.TREE_SYN_ANNOTATION + ")?/"
				+ "=tree < (VP , NP)";
		Tree mainTree = tree;

		try {
			Collection<Tree> mainTrees = ManageTrees
					.applyPattern(pattern, tree);

			mainTree = filterSubtreesByDepth(mainTrees, tree);

			if (mainTree == null)
				mainTree = tree;

		} catch (ParseException e) {
			System.out
					.println("============= ERROR in class [ManageTrees.retrieveMainTree].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return mainTree;
	}

	/**
	 * <p>
	 * Retrieves the NP of the main phrase.
	 * </p>
	 * 
	 * @param a
	 *            the NP child of the main tree. [in the form S (NP ...)]
	 * @return a Tree representing the NP phrase of the sentence.
	 */
	public static Tree retrieveNPChild(Tree tree) {

		String pattern = "NP=tree > (S < (VP , NP))";
		Tree npTree = tree;

		try {

			Collection<Tree> mainTrees = ManageTrees
					.applyPattern(pattern, tree);
			npTree = filterSubtreesByDepth(mainTrees, tree);

			if (npTree == null)
				npTree = tree;

		} catch (ParseException e) {
			System.out
					.println("============= ERROR in class [ManageTrees.retrieveNPChild].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return npTree;
	}

	/**
	 * <p>
	 * Retrieves the VP of the main phrase.
	 * </p>
	 * 
	 * @param a
	 *            the VP child of the main tree. [in the form S (VP ...)]
	 * @return a Tree representing the VP phrase of the sentence.
	 */
	public static Tree retrieveVPChild(Tree tree) {

		String pattern = "VP=tree > S";
		Tree vpTree = tree;

		try {
			Collection<Tree> mainTrees = ManageTrees
					.applyPattern(pattern, tree);

			vpTree = filterSubtreesByDepth(mainTrees, tree);

			if (vpTree == null)
				vpTree = tree;

		} catch (ParseException e) {
			System.out
					.println("============= ERROR in class [ManageTrees.retrieveVPChild].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return vpTree;
	}

	/**
	 * <p>
	 * Filters the subtrees by depth.
	 * </p>
	 * 
	 * @param subtrees
	 *            the collection of subtrees to be filtered.
	 * @param tree
	 *            the tree containing the subtrees.
	 * @return the lowest deep tree of the collection.
	 * @post the returned subtree is the lowest deep tree in the original tree.
	 */
	private static Tree filterSubtreesByDepth(Collection<Tree> subtrees,
			Tree tree) {
		Tree mainTree = null;

		if (subtrees.size() > 1) {

			int lowestDepth = 1000;

			for (Tree subtree : subtrees) {
				int depth = tree.depth(subtree);

				if (depth < lowestDepth) {
					mainTree = subtree;
					lowestDepth = depth;
				}
			}
		} else if (subtrees.size() == 1)
			mainTree = (new LinkedList<Tree>(subtrees)).getFirst();

		return mainTree;
	}

	/**
	 * <p>
	 * Retrieves the appositions contained in the given tree.
	 * </p>
	 * 
	 * @param tree
	 *            the Tree to be searched.
	 * @return the Collection of appositions in the tree.
	 */
	public static Collection<Tree> retrieveAppositions(Tree tree) {

		Collection<Tree> appositions = new HashSet<Tree>();
		try {
			String node = "NP|AP", nodeString = "/" + node + "(?:"
					+ Regex.TREE_SYN_ANNOTATION + ")?/", commaRegex = Regex.COMMA, dashRegex = Regex.DASH
					+ "(?:" + Regex.DASH + ")?", treeNodeIndex = "("
					+ Regex.TREE_NODE_INDEX + ")?$", open = "##OPEN##", close = "##CLOSE##", pattern = nodeString
					+ "=tree <<, (PNT < /^"
					+ open
					+ treeNodeIndex
					+ "/) <<- (PNT < /" + close + treeNodeIndex + "/)";

			Collection<Tree> phrasesRetrieved = null;

			String finalPattern = pattern.replaceFirst(open, commaRegex);
			finalPattern = finalPattern.replaceFirst(close, commaRegex);

			phrasesRetrieved = applyPattern(finalPattern, tree);
			if (phrasesRetrieved != null && phrasesRetrieved.size() > 0) {
				appositions.addAll(phrasesRetrieved);
			}

			finalPattern = pattern.replaceFirst(open, dashRegex);
			finalPattern = finalPattern.replaceFirst(close, dashRegex);

			phrasesRetrieved = applyPattern(finalPattern, tree);
			if (phrasesRetrieved != null && phrasesRetrieved.size() > 0) {
				appositions.addAll(phrasesRetrieved);
			}

			appositions = correctInclusions(appositions);

		} catch (ParseException e) {
			System.out
					.println("\n============= ERROR in class [ManageTrees.retrieveAppositions].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return appositions;
	}

	/**
	 * <p>
	 * Retrieves the collection of parenthetical phrases contained in the given
	 * tree.
	 * </p>
	 * 
	 * @param tree
	 *            the Tree to be searched.
	 * @return the Collection of parenthetical phrases in the tree.
	 */
	public static Collection<Tree> retrieveParentheticals(Tree tree) {

		String node = "PP|ADV|ADV\\'|ADVP|CONJ|CONJP("
				+ Regex.TREE_SYN_ANNOTATION + ")?";

		Collection<Tree> parentheticals = new HashSet<Tree>();

		try {

			if (tree != null) {

				String nodeString = "##NODE##", treeNodeIndex = "("
						+ Regex.TREE_NODE_INDEX + ")?", commaRegex = Regex.COMMA, dashRegex = Regex.DASH
						+ "(?:" + Regex.DASH + ")?", open = "##OPEN##", close = "##CLOSE##", pattern = nodeString
						+ "=tree <<, (PNT < /^"
						+ open
						+ treeNodeIndex
						+ "$/)  <<- (PNT < /^" + close + treeNodeIndex + "$/)";

				Collection<Tree> phrasesRetrieved = null;

				// Parentheticals enclosed by '(' or ')'
				String finalPattern = pattern.replaceFirst(nodeString, "__");
				finalPattern = finalPattern.replaceFirst(open,
						Regex.BRACKETS[1][0]);
				finalPattern = finalPattern.replaceFirst(close,
						Regex.BRACKETS[1][1]);

				phrasesRetrieved = applyPattern(finalPattern, tree);
				if (phrasesRetrieved != null && phrasesRetrieved.size() > 0)
					parentheticals.addAll(phrasesRetrieved);

				// Parentheticals enclosed by '[' or ']'
				finalPattern = pattern.replaceFirst(nodeString, "__");
				finalPattern = finalPattern.replaceFirst(open,
						Regex.BRACKETS[1][2]);
				finalPattern = finalPattern.replaceFirst(close,
						Regex.BRACKETS[1][3]);
				phrasesRetrieved = applyPattern(finalPattern, tree);

				if (phrasesRetrieved != null && phrasesRetrieved.size() > 0)
					parentheticals.addAll(phrasesRetrieved);

				//
				// Parentheticals enclosed by ','
				nodeString = "/" + node + "=tree/";
				finalPattern = finalPattern.replaceFirst(open, commaRegex);
				finalPattern = finalPattern.replaceFirst(close, commaRegex);

				phrasesRetrieved = applyPattern(finalPattern, tree);
				if (phrasesRetrieved != null && phrasesRetrieved.size() > 0)
					parentheticals.addAll(phrasesRetrieved);

				// Parentheticals enclosed by '-'
				finalPattern = finalPattern.replaceFirst(open, dashRegex);
				finalPattern = finalPattern.replaceFirst(close, dashRegex);

				phrasesRetrieved = applyPattern(finalPattern, tree);
				if (phrasesRetrieved != null && phrasesRetrieved.size() > 0)
					parentheticals.addAll(phrasesRetrieved);

				parentheticals = correctInclusions(parentheticals);
			}

		} catch (ParseException e) {
			System.out
					.println("============= ERROR in class [ManageTrees.retrieveParentheticals].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return parentheticals;
	}

	/**
	 * <p>
	 * Retrieves the collection of relative clauses contained in the given tree.
	 * </p>
	 * 
	 * @param tree
	 *            the Tree to be searched.
	 * @return the Collection of relative clauses in the tree.
	 */
	public static Collection<Tree> retrieveRelativeClauses(Tree tree) {

		Collection<Tree> relativeClauses = new HashSet<Tree>();
		try {
			String node = "CP", commaRegex = Regex.COMMA, dashRegex = Regex.DASH
					+ "(?:" + Regex.DASH + ")?", treeNodeIndex = "("
					+ Regex.TREE_NODE_INDEX + ")?$", open = "##OPEN##", close = "##CLOSE##", pattern = node
					+ "=tree <<, (PNT < /"
					+ open
					+ treeNodeIndex
					+ "/)  <<- (PNT < /" + close + treeNodeIndex + "/)";

			Collection<Tree> phrasesRetrieved = null;

			String finalPattern = pattern.replaceFirst(open, commaRegex);
			finalPattern = finalPattern.replaceFirst(close, commaRegex);

			phrasesRetrieved = applyPattern(finalPattern, tree);
			if (phrasesRetrieved != null && phrasesRetrieved.size() > 0)
				relativeClauses.addAll(phrasesRetrieved);

			finalPattern = pattern.replaceFirst(open, dashRegex);
			finalPattern = finalPattern.replaceFirst(close, dashRegex);

			phrasesRetrieved = applyPattern(finalPattern, tree);
			if (phrasesRetrieved != null && phrasesRetrieved.size() > 0)
				relativeClauses.addAll(phrasesRetrieved);

			finalPattern = node + "=tree <<, (PNT < /^" + open + treeNodeIndex
					+ "/)";
			finalPattern = finalPattern.replaceFirst(open, commaRegex);
			phrasesRetrieved = applyPattern(finalPattern, tree);

			if (phrasesRetrieved != null && phrasesRetrieved.size() > 0) {
				relativeClauses.addAll(phrasesRetrieved);
			}

			finalPattern = node + "=tree <<, (PNT < /^" + open + treeNodeIndex
					+ "/)";
			finalPattern = finalPattern.replaceFirst(open, dashRegex);
			phrasesRetrieved = applyPattern(finalPattern, tree);

			if (phrasesRetrieved != null && phrasesRetrieved.size() > 0) {
				relativeClauses.addAll(phrasesRetrieved);
			}

		} catch (ParseException e) {
			System.out
					.println("============= ERROR in class [ManageTrees.retrieveRelativeClauses].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return relativeClauses;
	}

	/**
	 * <p>
	 * Retrieves the collection of adjectives over an NP or over a VP node.
	 * </p>
	 * 
	 * @param tree
	 *            the tree to be searched.
	 * @return the collection of Trees containing the adjectives.
	 */
	public static Collection<Tree> retrieveAdjectives(Tree tree) {
		Collection<Tree> adjectives = new HashSet<Tree>();
		try {
			String pattern = "A=tree $, N";

			Collection<Tree> phrasesRetrieved = applyPattern(pattern, tree);
			if (phrasesRetrieved != null && phrasesRetrieved.size() > 0)
				adjectives.addAll(phrasesRetrieved);

		} catch (ParseException e) {
			System.out
					.println("============= ERROR in class [ManageTrees.retrieveAdjectives].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return adjectives;
	}

	/**
	 * <p>
	 * Retrieves the collection of adverbs over an NP or over a VP node.
	 * </p>
	 * 
	 * @param tree
	 *            the tree to be searched.
	 * @return the collection of Trees containing the adverbs.
	 */
	public static Collection<Tree> retrieveAdverbsInNP(Tree tree) {
		Collection<Tree> adverbs = new HashSet<Tree>();

		try {
			String pattern = "ADV|ADV'|ADVP=tree $ N|NP";

			Collection<Tree> phrasesRetrieved = applyPattern(pattern, tree);
			if (phrasesRetrieved != null && phrasesRetrieved.size() > 0)
				adverbs.addAll(phrasesRetrieved);

		} catch (ParseException e) {
			System.out
					.println("============= ERROR in class [ManageTrees.retrieveAdverbs].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return adverbs;
	}

	/**
	 * <p>
	 * Retrieves the collection of adverbs over an NP or over a VP node.
	 * </p>
	 * 
	 * @param tree
	 *            the tree to be searched.
	 * @return the collection of Trees containing the adverbs.
	 */
	public static Collection<Tree> retrieveAdverbsInVP(Tree tree) {
		Collection<Tree> adverbs = new HashSet<Tree>();

		try {
			String pattern = "ADV|ADV'|ADVP=tree , V|VP";
			Collection<Tree> phrasesRetrieved = applyPattern(pattern, tree);

			if (phrasesRetrieved != null && phrasesRetrieved.size() > 0)
				adverbs.addAll(phrasesRetrieved);

		} catch (ParseException e) {
			System.out
					.println("============= ERROR in class [ManageTrees.retrieveAdverbs].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return adverbs;
	}

	/**
	 * <p>
	 * Retrieves prepositional phrases occurring in a tree.
	 * </p>
	 * 
	 * @param tree
	 *            the tree to be searched.
	 * @return the collection of prepositional phrases found;
	 */
	public static Collection<Tree> retrievePrepositionalPhrases(Tree tree) {
		Collection<Tree> pps = new HashSet<Tree>();
		try {
			String pattern = "(PP=tree $ S) > S";

			Collection<Tree> phrasesRetrieved = applyPattern(pattern, tree);
			if (phrasesRetrieved != null && phrasesRetrieved.size() > 0)
				pps.addAll(phrasesRetrieved);

		} catch (ParseException e) {
			System.out
					.println("============= ERROR in class [ManageTrees.retrievePrepositionalPhrases].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return pps;
	}

	/**
	 * <p>
	 * Applies the given pattern to identify parenthetical phrases.
	 * </p>
	 * 
	 * @param pattern
	 *            pattern to be catched.
	 * @param tree
	 *            the tree to be searched.
	 * @return Collection<Tree> collection of phrases retrieved.
	 * @throws ParseException
	 *             if an error occurs.
	 */
	private static Collection<Tree> applyPattern(String pattern, Tree tree)
			throws ParseException {

		Collection<Tree> phrases = new LinkedList<Tree>();
		TregexPattern tregex = TregexPattern.compile(pattern);
		TregexMatcher ma = tregex.matcher(tree);

		while (ma.find()) {
			Tree phrase = ma.getNode("tree");

			if (phrase != null)
				phrases.add(phrase);
		}

		return phrases;
	}

	/**
	 * <p>
	 * Filters the trees that include trees that are also in the collection.
	 * </p>
	 * 
	 * @param trees
	 *            the Collection of trees to be filtered.
	 * @return a filtered collection of trees.
	 */
	private static Collection<Tree> correctInclusions(Collection<Tree> trees) {

		Collection<Tree> filtered = new HashSet<Tree>();

		for (Tree current : trees) {

			boolean isSubTree = false;

			for (Tree subtree : trees) {

				if (!subtree.equals(current)) {
					isSubTree = ManageTrees.isSubtree(current, subtree);

					if (isSubTree)
						break;
				}
			}

			if (!isSubTree)
				filtered.add(current);
		}

		return filtered;
	}

	/**
	 * <p>
	 * Checks if the given subtree is in fact a subtree of the main tree give.
	 * </p>
	 * 
	 * @param parent
	 *            the parent tree.
	 * @param subtree
	 *            the subtree to be searched.
	 * @return true if subtree is a subtree of the parent tree; false otherwise.
	 */
	private static boolean isSubtree(Tree parent, Tree subtree) {
		boolean found = false;

		for (Tree current : parent.subTrees()) {

			found = current.equals(subtree);

			if (found)
				break;

		}

		return found;
	}

	/**
	 * <p>
	 * Adds an index to each tree leaf.
	 * </p>
	 * <p>
	 * The new node shall contain the node string, and an annotation mark ("/")
	 * followed by the leaf index.
	 * </p>
	 * 
	 * @param parseTree
	 *            the parseTree to be updated.
	 * 
	 * @return a tree with the leaves nodes updated.
	 */
	public static Tree updateLeavesIndexes(Tree parseTree) {
		Tree copy = parseTree.deepCopy();
		List<Tree> leaves = copy.getLeaves();

		int counter = 0;

		for (Tree leaf : leaves) {
			String node = leaf.nodeString() + Regex.ANNOTATION_SPLITTER
					+ counter;
			leaf.label().setValue(node);
			counter++;
		}

		return copy;
	}

	/**
	 * <p>
	 * Cleans the tree leafs from the words information.
	 * </p>
	 * 
	 * @param parseTree
	 *            the parsed tree to be cleaned.
	 * 
	 * @return a tree with the original leafs.
	 */
	public static Tree cleanTreeLeafs(Tree parseTree) {

		List<Tree> leaves = parseTree.getLeaves();

		String node = "";
		Matcher ma = null;

		for (Tree leaf : leaves) {
			ma = Pattern.compile("(.*?)" + Regex.TREE_NODE_INDEX).matcher(
					leaf.nodeString());

			if (ma.find())
				node = ma.group(1);

			leaf.label().setFromString(node);
		}

		return parseTree;

	}

	/**
	 * <p>
	 * Retrieves the tree node number based on the original tree.
	 * </p>
	 * 
	 * @param original
	 *            the original tree to be searched.
	 * @param subtree
	 *            the subtree.
	 * 
	 * @return the number of the roots subtree node.
	 */
	public static int getTreeNodeNumber(Tree original, Tree subtree) {
		int nodeNumber = subtree.nodeNumber(original);
		return nodeNumber;
	}

	/**
	 * <p>
	 * Retrieves the leaf node by removing its annotation.
	 * </p>
	 * 
	 * @param leaf
	 *            the leaf node string.
	 * 
	 * @return a String containing the leaf node without annotation.
	 */
	public static String getLeafNode(String leaf) {

		String node = "";
		Matcher ma = Pattern.compile("(\\S+?)/[-]?\\d+$").matcher(leaf);

		if (ma.find())
			node = ma.group(1);

		return node;
	}

	/**
	 * <p>
	 * Retrieves the leaf index by removing its node value.
	 * </p>
	 * 
	 * @param leaf
	 *            the leaf node string.
	 * 
	 * @return the index leaf number; -1 if not found.
	 */
	public static int getLeafIndex(String leaf) {
		int index = -1;

		Matcher ma = Pattern.compile("\\S+?/(\\d+)$").matcher(leaf);

		if (ma.find())
			index = new Integer(ma.group(1)).intValue();

		return index;
	}

	public static Sentence mergeTwoNPs(Tree np, Sentence first, Sentence second) {

		Sentence main = first, subordinate = second, finalSentence = first;

		if (second.getTotalWords() < first.getTotalWords()) {
			main = second;
			subordinate = first;
			finalSentence = second;
		}

		Tree mainVP = retrieveVPChild(main.getParseTree()).deepCopy(), subdordinateVP = retrieveVPChild(
				subordinate.getParseTree()).deepCopy();

		if (mainVP != null && subdordinateVP != null) {

			Tree topNode = new LabeledScoredTreeNode(new StringLabel("S"));
			Tree leftNode = new LabeledScoredTreeNode(new StringLabel("S"));
			Tree rightNode = new LabeledScoredTreeNode(new StringLabel("S"));

			leftNode.setChildren(new Tree[] { np.deepCopy(), mainVP.deepCopy() });

			Tree newNode = new LabeledScoredTreeNode(new StringLabel("S"));
			Tree conjunction = new LabeledScoredTreeNode(
					new StringLabel("CONJ"));
			conjunction.addChild(new LabeledScoredTreeNode(new StringLabel(
					"e/-1")));

			Tree otherVP = new LabeledScoredTreeNode(new StringLabel("S"));
			otherVP.setChildren(new Tree[] { subdordinateVP });

			newNode.setChildren(new Tree[] { conjunction, otherVP });
			rightNode.setChildren(new Tree[] { newNode });
			topNode.setChildren(new Tree[] { leftNode, rightNode });

			Sentence subordinateWithoutNP = subordinate
					.buildSubSentence(subdordinateVP);
			finalSentence = Utils.mergeSentences(topNode, main,
					subordinateWithoutNP);

		} else if (mainVP == null)
			finalSentence = second;
		else if (subdordinateVP == null)
			finalSentence = first;

		return finalSentence;
	}

	/**
	 * <p>
	 * Retrieves all the removable passages found in the given parse tree.
	 * </p>
	 * 
	 * @param parseTree
	 *            the tree to be searched.
	 * @return a <code>Collection</code> containing all the passages candidates
	 *         to removal.
	 */
	public static Collection<Tree> removablePassages(Tree parseTree) {

		Collection<Tree> passages = new LinkedList<Tree>();
		Collection<Tree> current = retrieveAppositions(parseTree);
		passages.addAll(current);
		current = retrieveParentheticals(parseTree);
		passages.addAll(current);
		current = retrieveRelativeClauses(parseTree);
		passages.addAll(current);
		current = retrievePrepositionalPhrases(parseTree);
		passages.addAll(current);

		return passages;
	}

	public static Collection<Phrase> convertTrees2Phrases(Collection<Tree> trees) {

		Collection<Phrase> phrases = new LinkedList<Phrase>();

		for (Tree tree : trees) {
			Phrase phrase = new Phrase(tree);
			phrases.add(phrase);
		}

		return phrases;
	}
}
