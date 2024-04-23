package model.dao;

import java.util.List;

public interface HtmlDAO {
	
	public void create(Html html);
	
	public void update(Html html);
	
	public void delete(String uuid);
	
	public Html get(String uuid);
	
	public boolean contains(String uuid);
	
	public List<Html> list();
	
	public String toString();
}
