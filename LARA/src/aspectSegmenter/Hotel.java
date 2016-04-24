package aspectSegmenter;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

public class Hotel implements Comparable<Hotel>, Serializable{
	
	private static final long serialVersionUID = -5695057513648909065L;
	public String m_ID;
	public double m_rating;

	public List<Review> m_reviews;
	public double m_rScore;
	public int m_index;
	DecimalFormat m_formater;
	
	public Hotel(String ID, double rating){
		m_ID = ID;
		m_rating = rating;	
		m_reviews = null;
		
		m_formater = new DecimalFormat("#.#");
	}
	
	public Hotel(String ID){
		m_ID = ID;
		m_reviews = new Vector<Review>();
		m_formater = new DecimalFormat("#.#");
	}
	
	public int getReviewSize(){
		return m_reviews==null?0:m_reviews.size();
	}
	
	@Override
	public int compareTo(Hotel h) {
		if (this.m_rScore>h.m_rScore)
			return -1;
		else if (this.m_rScore<h.m_rScore)
			return 1;
		else 
			return 0;
	}
	
	public String toString(){
		return m_ID +  "\n" + m_rating + "\n";
	}
	
	public String toPrintString(){
		StringBuffer buffer = new StringBuffer(512);
		buffer.append("<Overall Rating>"+m_rating+"\n");
		return buffer.toString();
	}		
		
	public void addReview(Review r){
		if (m_reviews==null)
			m_reviews = new Vector<Review>();
		m_reviews.add(r);
	}
}
