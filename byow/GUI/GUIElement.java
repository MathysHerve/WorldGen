package byow.GUI;

/**
 * GUIElements are non interactable components of the GUI. They can be displayed with show() and
 * not displayed with hide().
 */
public interface GUIElement {
    public void show();
    public void hide();
    public void toggle();
    public void draw();
}
