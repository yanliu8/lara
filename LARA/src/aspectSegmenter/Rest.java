package aspectSegmenter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

public class Rest {
	public String m_ID;
	List<Review> m_reviews;
	DecimalFormat m_formater;
	
	public Rest(String ID)
	{
		m_ID = ID;
	}
	public void addReview(Review r)
	{
		if (m_reviews==null)
			m_reviews = new Vector<Review>();
		m_reviews.add(r);
	}
}
