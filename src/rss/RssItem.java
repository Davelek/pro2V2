package rss;

import com.sun.javafx.binding.StringFormatter;

public class RssItem {

    private String title,link,description;

  /*  public RssItem(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%s",title,link,description);
    }
}
