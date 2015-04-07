package external;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import preferences.Preferences;
import preferences.Regex;
import preferences.Utils;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class ManageClassification {

	private String arg1;
	private String arg2;
	private boolean inverted;

	private FilteredClassifier binary;

	public ManageClassification(String firstSentence, String secondSentence) {
		arg1 = firstSentence;
		arg2 = secondSentence;
		inverted = false;

		try {
			binary = (FilteredClassifier) SerializationHelper
					.read(new FileInputStream(
							Preferences.WEKA_BINARY_MODEL_FILE));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String classifyClass() {

		String relation = "";

		try {

			LinkedList<Attribute> attributes = new LinkedList<Attribute>();

			// Convert the sentences to features...
			LinkedList<String> features = getSubtypeFeatures(false);

			FastVector dataset = new FastVector(features.size() + 1);

			for (int i = 1; i < features.size() + 1; i++) {
				Attribute attribute = new Attribute(i + "", (FastVector) null);
				attributes.add(attribute);
				dataset.addElement(attribute);
			}

			// Add class attribute.
			Set<String> classes = Preferences.getDiscourseRelationClasses();
			FastVector classValues = new FastVector(classes.size());

			for (String classType : classes)
				classValues.addElement(classType);

			dataset.addElement(new Attribute("CLASSE", classValues));

			Instances data = new Instances("DATA", dataset, dataset.size() + 1);
			data.setClassIndex(data.numAttributes() - 1); // the class is the
															// last attribute

			Instance instance = new Instance(features.size() + 1);
			instance.setDataset(data);
			data.add(instance);

			Iterator<Attribute> attsIt = attributes.iterator();
			Iterator<String> featsIt = features.iterator();

			while (attsIt.hasNext() && featsIt.hasNext()) {
				Attribute attribute = attsIt.next();
				String feature = featsIt.next();
				instance.setValue(attribute, feature);
			}

			String modelFile = Preferences.WEKA_SUBTYPE_MODEL_FILE.replaceAll(
					Regex.MARKER, "Classes");
			FilteredClassifier classifier = (FilteredClassifier) SerializationHelper
					.read(new FileInputStream(modelFile));

			StringToWordVector filter = new StringToWordVector();
			filter.setIDFTransform(true);
			filter.setTFTransform(true);

			filter.setInputFormat(data); // data instances that you are going to
											// input to the filter
			filter.input(instance);

			double pred = classifier.classifyInstance(instance);
			relation = instance.classAttribute().value((int) pred);

		} catch (Exception e) {
			e.printStackTrace();
			relation = null;
		}

		return relation;
	}

	public boolean haveRelation() {

		boolean haveRelation = false;
		double[] predOriginal = buildClassification(
				Preferences.WEKA_BINARY_CLASSIFICATION_TYPE, false);
		double[] predInverted = buildClassification(
				Preferences.WEKA_BINARY_CLASSIFICATION_TYPE, true);

		LinkedList<Prediction> allPredictions = new LinkedList<Prediction>();
		allPredictions.add(new Prediction(Preferences.WEKA_YES + "-Normal",
				predOriginal[0]));
		allPredictions.add(new Prediction(Preferences.WEKA_NO + "-Normal",
				predOriginal[1]));
		allPredictions.add(new Prediction(Preferences.WEKA_YES + "-Inverted",
				predInverted[0]));
		allPredictions.add(new Prediction(Preferences.WEKA_NO + "-Inverted",
				predInverted[1]));

		Collections.sort(allPredictions);

		Prediction first = allPredictions.getFirst();

		haveRelation = first.subtype.startsWith(Preferences.WEKA_YES);

		if (haveRelation)
			inverted = first.subtype.endsWith("Inverted");

		System.out.println("The sentences "
				+ (haveRelation ? "HAVE" : "DON'T HAVE") + " a relation which "
				+ (inverted ? "IS" : "IS NOT") + " inverted.");

		return haveRelation;
	}

	private double[] buildClassification(String type, boolean order) {

		LinkedList<Attribute> attributes = new LinkedList<Attribute>();
		try {

			Set<String> classes = new TreeSet<String>();
			LinkedList<String> features = null;
			String modelFile = "";
			FilteredClassifier classifier = binary;

			if (type.equals(Preferences.WEKA_BINARY_CLASSIFICATION_TYPE)) {
				// Add the available classes
				classes = Preferences.getDiscourseBinaryClasses();
				// Convert the sentences to features...
				features = getSubtypeFeatures(order);
				// Defining the model to be used
				modelFile = Preferences.WEKA_BINARY_MODEL_FILE;
			} else {
				classes.add(type);
				classes.add(Preferences.WEKA_OTHER);
				features = getSubtypeFeatures(order);
				modelFile = Preferences.WEKA_SUBTYPE_MODEL_FILE.replaceAll(
						Regex.MARKER, type);
				classifier = (FilteredClassifier) SerializationHelper
						.read(new FileInputStream(modelFile));
			}

			FastVector dataset = new FastVector(features.size() + 1);

			for (int i = 1; i < features.size() + 1; i++) {
				Attribute attribute = new Attribute(i + "", (FastVector) null);
				attributes.add(attribute);
				dataset.addElement(attribute);
			}

			FastVector classValues = new FastVector(classes.size());

			for (String classType : classes)
				classValues.addElement(classType);

			dataset.addElement(new Attribute("CLASSE", classValues));

			Instances data = new Instances("DATA", dataset, dataset.size() + 1);
			data.setClassIndex(data.numAttributes() - 1); // the class is the
															// last attribute

			Instance instance = new Instance(features.size() + 1);
			instance.setDataset(data);
			data.add(instance);

			Iterator<Attribute> attsIt = attributes.iterator();
			Iterator<String> featsIt = features.iterator();

			while (attsIt.hasNext() && featsIt.hasNext()) {
				Attribute attribute = attsIt.next();
				String feature = featsIt.next();

				instance.setValue(attribute, feature);
			}

			StringToWordVector filter = new StringToWordVector();
			filter.setIDFTransform(true);
			filter.setTFTransform(true);

			filter.setInputFormat(data); // data instances that you are going to
											// input to the filter
			filter.input(instance);

			double[] pred = classifier.distributionForInstance(instance);
			return pred;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String classifyType(String relation) {

		String type = "";

		try {

			LinkedList<Attribute> attributes = new LinkedList<Attribute>();

			// Convert the sentences to features...
			LinkedList<String> features = getSubtypeFeatures(false);

			FastVector dataset = new FastVector(features.size() + 1);

			for (int i = 1; i < features.size() + 1; i++) {
				Attribute attribute = new Attribute(i + "", (FastVector) null);
				attributes.add(attribute);
				dataset.addElement(attribute);
			}

			// Add class attribute.
			Set<String> classes = Preferences
					.getDiscourseRelationTypes(relation);
			FastVector classValues = new FastVector(classes.size());

			for (String classType : classes)
				classValues.addElement(classType);

			dataset.addElement(new Attribute("CLASSE", classValues));

			Instances data = new Instances("DATA", dataset, dataset.size() + 1);
			data.setClassIndex(data.numAttributes() - 1); // the class is the
															// last attribute

			Instance instance = new Instance(features.size() + 1);
			instance.setDataset(data);
			data.add(instance);

			Iterator<Attribute> attsIt = attributes.iterator();
			Iterator<String> featsIt = features.iterator();

			while (attsIt.hasNext() && featsIt.hasNext()) {
				Attribute attribute = attsIt.next();
				String feature = featsIt.next();

				instance.setValue(attribute, feature);
			}

			String modelFile = Preferences.WEKA_SUBTYPE_MODEL_FILE.replaceAll(
					Regex.MARKER, relation);

			FilteredClassifier classifier = (FilteredClassifier) SerializationHelper
					.read(new FileInputStream(modelFile));
			StringToWordVector filter = new StringToWordVector();
			filter.setIDFTransform(true);
			filter.setTFTransform(true);

			filter.setInputFormat(data); // data instances that you are going to
											// input to the filter
			filter.input(instance);

			double pred = classifier.classifyInstance(instance);
			type = instance.classAttribute().value((int) pred);

		} catch (Exception e) {
			e.printStackTrace();
			type = null;
		}

		return type;
	}

	public String classifySubtype(String type) {

		String subtype = null;

		try {

			LinkedList<Attribute> attributes = new LinkedList<Attribute>();

			// Convert the sentences to features...
			LinkedList<String> features = getSubtypeFeatures(false);

			FastVector dataset = new FastVector(features.size() + 1);

			for (int i = 1; i < features.size() + 1; i++) {
				Attribute attribute = new Attribute(i + "", (FastVector) null);
				attributes.add(attribute);
				dataset.addElement(attribute);
			}

			// Add class attribute.
			Set<String> classes = Preferences
					.getDiscourseRelationSubtypes(type);

			if (classes.size() > 0) {
				FastVector classValues = new FastVector(classes.size());

				for (String classType : classes)
					classValues.addElement(classType);

				dataset.addElement(new Attribute("CLASSE", classValues));

				Instances data = new Instances("DATA", dataset,
						dataset.size() + 1);
				data.setClassIndex(data.numAttributes() - 1); // the class is
																// the last
																// attribute

				Instance instance = new Instance(features.size() + 1);
				instance.setDataset(data);
				data.add(instance);
				Iterator<Attribute> attsIt = attributes.iterator();
				Iterator<String> featsIt = features.iterator();

				while (attsIt.hasNext() && featsIt.hasNext()) {
					Attribute attribute = attsIt.next();
					String feature = featsIt.next();
					instance.setValue(attribute, feature);
				}

				String modelFile = Preferences.WEKA_SUBTYPE_MODEL_FILE
						.replaceAll(Regex.MARKER, type);

				FilteredClassifier classifier = (FilteredClassifier) SerializationHelper
						.read(new FileInputStream(modelFile));
				StringToWordVector filter = new StringToWordVector();
				filter.setIDFTransform(true);
				filter.setTFTransform(true);

				filter.setInputFormat(data); // data instances that you are
												// going to input to the filter
				filter.input(instance);

				double pred = classifier.classifyInstance(instance);
				subtype = instance.classAttribute().value((int) pred);
			}

		} catch (Exception e) {
			e.printStackTrace();
			subtype = null;
		}

		return subtype;
	}

	/**
	 * Gets the collection of features for the class decision.
	 * 
	 * @pre invertOrder=false if the order of the arguments is to be maintained.
	 * @param invertOrder
	 *            determines if the order of the arguments should be inverted.
	 * @return a collection containing the features for the given arguments.
	 */
	private LinkedList<String> getSubtypeFeatures(boolean invertOrder) {

		LinkedList<String> features = new LinkedList<String>();

		String sentence1 = arg1, sentence2 = arg2;

		if (invertOrder) {
			sentence1 = arg2;
			sentence2 = arg1;
		}

		LinkedList<String> tmp = Utils.getSplittedPropertiesWithPerson(Regex
				.getLastVerb(sentence1));
		tmp = Regex.addPrefix(tmp, "arg1");
		features.addAll(tmp);

		tmp = Utils.getSplittedPropertiesWithPerson(Regex
				.getFirstVerb(sentence2));
		tmp = Regex.addPrefix(tmp, "arg2");
		features.addAll(tmp);
		tmp = Utils.getLastWords(Preferences.CONTEXT_WINDOW, sentence1);
		tmp = Regex.addPrefix(tmp, "arg1");
		features.addAll(tmp);

		tmp = Utils.getFirstWords(Preferences.CONTEXT_WINDOW, sentence2);
		tmp = Regex.addPrefix(tmp, "arg2");
		features.addAll(tmp);

		tmp = Utils.getAllTokens("ADV", sentence1);
		tmp = Regex.addPrefix(tmp, "arg1");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("LADV.", sentence1);
		tmp = Regex.addPrefix(tmp, "arg1");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("ADV", sentence2);
		tmp = Regex.addPrefix(tmp, "arg2");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("LADV.", sentence2);
		tmp = Regex.addPrefix(tmp, "arg2");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("CJ", arg1);
		tmp = Regex.addPrefix(tmp, "arg1");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("LCJ.", arg1);
		tmp = Regex.addPrefix(tmp, "arg1");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("CJ", arg2);
		tmp = Regex.addPrefix(tmp, "arg2");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("LCJ.", arg2);
		tmp = Regex.addPrefix(tmp, "arg2");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("PREP", arg1);
		tmp = Regex.addPrefix(tmp, "arg1");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("LPREP.", arg1);
		tmp = Regex.addPrefix(tmp, "arg1");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("PREP", arg2);
		tmp = Regex.addPrefix(tmp, "arg2");
		features.add(Utils.buildStringFromList(tmp));

		tmp = Utils.getAllTokens("LPREP.", arg2);
		tmp = Regex.addPrefix(tmp, "arg2");
		features.add(Utils.buildStringFromList(tmp));

		return features;
	}

	class Prediction implements Comparable<Prediction> {

		String subtype;
		double value;

		Prediction(String subtype, double value) {
			this.subtype = subtype;
			this.value = value;
		}

		@Override
		public int compareTo(Prediction arg0) {

			if (this.value > arg0.value)
				return -1;
			else if (this.value < arg0.value)
				return 1;

			return 0;
		}

		@Override
		public String toString() {
			return this.subtype + "\t" + this.value;
		}
	}

	public boolean isInverted() {
		return inverted;
	}

	public String classifyRelation() {

		LinkedList<Prediction> allPredictions = new LinkedList<Prediction>();

		for (String subtype : Preferences.SUBTYPES) {
			double[] predictions = buildClassification(subtype, inverted);

			allPredictions.add(new Prediction(subtype, predictions[0]));
		}

		Collections.sort(allPredictions);

		Prediction first = allPredictions.getFirst();

		String subtype = first.subtype;

		return subtype;

	}
}
