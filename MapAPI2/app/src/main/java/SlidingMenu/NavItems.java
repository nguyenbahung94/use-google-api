package SlidingMenu;

/**
 * Created by Adam on 6/9/2016.
 */
public class NavItems {
    private String title;
    private int resIcon;

    public NavItems(String title, int resIcon) {
        this.title = title;
        this.resIcon = resIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResIcon() {
        return resIcon;
    }
}
