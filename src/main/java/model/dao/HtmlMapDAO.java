package model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HtmlMapDAO implements HtmlDAO{
	
	private Map<String, String> pages;

	public HtmlMapDAO(Map<String, String> pages) {
		this.pages = pages;
	}
	

	@Override
	public void create(Html html) {
		pages.put(html.getUuid(), html.getContent());
	}

	@Override
	public void update(Html html) {
		pages.replace(html.getUuid(), html.getContent());
	}

	@Override
	public void delete(String uuid) {
		pages.remove(uuid);
	}

	@Override
	public Html get(String uuid) {
		return (pages.containsKey(uuid)) ? new Html(uuid, pages.get(uuid)) : null;
	}

	public boolean contains(String uuid) {
		return pages.containsKey(uuid);
	}
	
	@Override
	public List<Html> list() {
		List<Html> lista = new ArrayList<Html>();
		pages.forEach((key, value) -> {
			lista.add(new Html(key, value));
		});
		return lista;
	}
	
	public String toString() {
		return pages.toString();
	}

}
