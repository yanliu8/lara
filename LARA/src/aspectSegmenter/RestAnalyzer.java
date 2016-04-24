package aspectSegmenter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class RestAnalyzer {
	public static final String PUNCT = ":;=+-()[],.\"'";
	Vector<Rest> m_restList;
	Vector<_Aspect> m_keywords;
	Hashtable<String, Integer> m_vocabulary;//indexed vocabulary
	Vector<String> m_wordlist;//term list in the original order
	
	HashSet<String> m_stopwords;
	Vector<rank_item<String>> m_ranklist;
	double[][] m_chi_table;
	double[] m_wordCount;
	boolean m_isLoadCV; // if the vocabulary is fixed
	static public final double chi_ratio = 4.0;
	static public final int chi_size = 35;
	static public final int chi_iter = 10;
	static public final int tf_cut = 10;
	SentenceDetectorME m_stnDector;
	TokenizerME m_tokenizer;
	POSTaggerME m_postagger;
	Stemmer m_stemmer;	
	class _Aspect{
		String m_name;
		HashSet<String> m_keywords;
		_Aspect(String name, HashSet<String> keywords){
			m_name = name;
			m_keywords = keywords;
		}
		
		
	}
	class rank_item<E> implements Comparable<rank_item<E>>{
		E m_name;
		double m_value;
		
		public rank_item(E name, double value){
			m_name = name;
			m_value = value;
		}
		
		@Override
		public int compareTo(rank_item<E> v) {
			if (m_value < v.m_value) return 1;
			else if (m_value > v.m_value) return -1;
			return 0;
		}
		
	}
	public RestAnalyzer(String seedwords, String stopwords, String stnSplModel, String tknModel, String posModel){
		m_restList = new Vector<Rest>();
		m_vocabulary = new Hashtable<String, Integer>();
		m_chi_table = null;
		m_isLoadCV = false;
		if (seedwords != null && seedwords.isEmpty()==false)
			LoadKeywords(seedwords);
		LoadStopwords(stopwords);
		
		try {
			m_stnDector = new SentenceDetectorME(new SentenceModel(new FileInputStream(stnSplModel)));
			m_tokenizer = new TokenizerME(new TokenizerModel(new FileInputStream(tknModel)));
			m_postagger = new POSTaggerME(new POSModel(new FileInputStream(posModel)));
			m_stemmer = new Stemmer();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[Info]NLP modules initialized...");
	}
	public void LoadKeywords(String filename){
		try {
			m_keywords = new Vector<_Aspect>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			String tmpTxt;
			String[] container;
			HashSet<String> keywords;
			while( (tmpTxt=reader.readLine()) != null ){
				container = tmpTxt.split("\t");
				keywords = new HashSet<String>(container.length-1);
				for(int i=1; i<container.length; i++)
					keywords.add(container[i]);
				m_keywords.add(new _Aspect(container[0], keywords));
				System.out.println("Keywords for " + container[0] + ": " + keywords.size());
			}
			reader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void LoadVocabulary(String filename){
		try {
			m_vocabulary = new Hashtable<String, Integer>();
			m_wordlist = new Vector<String>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			String tmpTxt;
			String[] container;
			while( (tmpTxt=reader.readLine()) != null ){
				container = tmpTxt.split("\t");
				m_vocabulary.put(container[0], m_vocabulary.size());
				m_wordlist.add(tmpTxt.trim());
			}
			reader.close();
			m_isLoadCV = true;
			System.out.println("[Info]Load " + m_vocabulary.size() + " control terms...");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void LoadStopwords(String filename){
		try {
			m_stopwords = new HashSet<String>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			String tmpTxt;
			while( (tmpTxt=reader.readLine()) != null )
				m_stopwords.add(tmpTxt.toLowerCase());
			reader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public String[] getLemma(String[] tokens){
		String[] lemma = new String[tokens.length];
		String term;
		for(int i=0; i<lemma.length; i++){
			//lemma[i] = m_stemmer.stem(tokens[i].toLowerCase());//shall we stem it?
			term = tokens[i].toLowerCase();
			if (term.length()>1 && PUNCT.indexOf(term.charAt(0))!=-1 && term.charAt(1)>='a' && term.charAt(1)<='z')
				lemma[i] = term.substring(1);
			else 
				lemma[i] = term;
		}
		return lemma;
	}

}
