package framework.element.internal;

public interface ICheckBox extends IWebElement {

    void toggle();

    void check();

    void unCheck();

    boolean isChecked();

}
