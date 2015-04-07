package core.scores;

/**
 * <p>
 * This class represents the sentence Score.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Score {

	/**
	 * <p>
	 * Main score value.
	 * </p>
	 **/
	private double tfidfScore;
	/**
	 * <p>
	 * Extra score (used to give bonus to this sentence - computed in the
	 * clustering phase).
	 * </p>
	 **/
	private double extraScore;
	/**
	 * <p>
	 * Simplification score (if the sentence is a subsentence)./p>
	 **/
	private double simplificationScore;
	/**
	 * <p>
	 * Cluster score.
	 * </p>
	 **/
	private double clusterScore;
	/**
	 * <p>
	 * Position score.
	 * </p>
	 **/
	private double positionScore;
	/**
	 * <p>
	 * Score of the keyword that represents the cluster to which this sentences
	 * has been added.
	 * </p>
	 **/
	private double clusterKeywordScore;
	/**
	 * <p>
	 * Number of system keywords in this sentence.
	 * </p>
	 **/
	private int numberOfKeywords;
	/**
	 * <p>
	 * Number of named entities in the sentence.
	 * </p>
	 **/
	private int numberOfNamedEntities;

	public Score() {
		this.tfidfScore = 0;
		this.extraScore = 0;
		this.simplificationScore = 0;
		this.clusterScore = 0;
		this.positionScore = 0;
		this.clusterKeywordScore = 0;
		this.numberOfKeywords = 0;
		this.numberOfNamedEntities = 0;
	}

	public Score(double tfidfScore, double extraScore,
			double simplificationScore, double clusterScore,
			double positionScore, int numberOfKeywords,
			double clusterKeywordScore, int numberOfNamedEntities) {
		this.tfidfScore = tfidfScore;
		this.extraScore = extraScore;
		this.simplificationScore = simplificationScore;
		this.clusterScore = clusterScore;
		this.positionScore = positionScore;
		this.clusterKeywordScore = clusterKeywordScore;
		this.numberOfKeywords = numberOfKeywords;
		this.numberOfNamedEntities = numberOfNamedEntities;
	}

	public Score(Score currentScore) {
		this.tfidfScore = currentScore.getTFIDFScore();
		this.extraScore = currentScore.getExtraScore();
		this.simplificationScore = currentScore.getSimplificationScore();
		this.clusterScore = currentScore.getClusterScore();
		this.positionScore = currentScore.getPositionScore();
		this.clusterKeywordScore = currentScore.getClusterKeywordScore();
		this.numberOfKeywords = currentScore.getNumberOfKeywords();
		this.numberOfNamedEntities = currentScore.getNumberOfNamedEntities();

	}

	// Getters & Setters
	public double getTFIDFScore() {
		return tfidfScore;
	}

	public double getExtraScore() {
		return extraScore;
	}

	public double getSimplificationScore() {
		return simplificationScore;
	}

	public double getClusterScore() {
		return clusterScore;
	}

	public double getPositionScore() {
		return positionScore;
	}

	public int getNumberOfKeywords() {
		return numberOfKeywords;
	}

	public double getClusterKeywordScore() {
		return clusterKeywordScore;
	}

	public int getNumberOfNamedEntities() {
		return numberOfNamedEntities;
	}

	public void setTFIDFScore(double value) {
		this.tfidfScore = value;
	}

	public void setExtraScore(double extraScore) {
		this.extraScore = extraScore;
	}

	public void setSimplificationScore(double simplificationScore) {
		this.simplificationScore = simplificationScore;
	}

	public void setClusterScore(double clusterScore) {
		this.clusterScore = clusterScore;
	}

	public void setPositionScore(double positionScore) {
		this.positionScore = positionScore;
	}

	public void setNumberOfKeywords(int numberOfKeywords) {
		this.numberOfKeywords = numberOfKeywords;
	}

	public void setClusterKeywordScore(double clusterKeywordScore) {
		this.clusterKeywordScore = clusterKeywordScore;
	}

	public void setNumberOfNamedEntities(int numberOfNamedEntities) {
		this.numberOfNamedEntities = numberOfNamedEntities;
	}

}
