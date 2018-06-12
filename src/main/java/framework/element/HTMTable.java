package framework.element;

import framework.element.internal.ITable;
import org.fest.assertions.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import java.util.ArrayList;
import java.util.List;

public class HTMTable extends HTMWebElement implements ITable {

    public HTMTable(By by, String pageName, String elementName) {
        super(by, pageName, elementName);
        setElementType("HTMTable");
    }

    public int getRowCount() {
        return getRows().size();
    }

    public int getColumnCount() {
        return getWrappedElement().findElement(By.cssSelector("tr")).findElements(By.cssSelector("*")).size();
    }

    public WebElement getCellAtIndex(int rowIdx, int colIdx) {
        WebElement row = getRows().get(rowIdx);
        List<WebElement> cells;
        // Cells are most likely to be td tags
        if ((cells = row.findElements(By.tagName("td"))).size() > 0) {
            return cells.get(colIdx - 1);
        }
        // Failing that try th tags
        else if ((cells = row.findElements(By.tagName("th"))).size() > 0) {
            return cells.get(colIdx - 1);
        } else {
            final String error = String.format("Could not find cell at row: %s column: %s", rowIdx, colIdx);
            throw new RuntimeException(error);
        }
    }

    public String getCellTextAtIndex(int rowIdx, int colIdx){
        return getCellAtIndex(rowIdx, colIdx).getText();
    }

    private List<WebElement> getRows() {
        List<WebElement> rows = new ArrayList<WebElement>();
        //Header rows
        List<WebElement> headerRows = getWrappedElement().findElements(By.cssSelector("thead tr"));
        if(headerRows.size() > 0){
            rows.add(headerRows.get(0));
        }else{
            rows.add(null);
        }
        //Body rows
        List<WebElement> bodyRows = getWrappedElement().findElements(By.cssSelector("tbody tr"));
        if(bodyRows.size() > 0){
            rows.addAll(bodyRows);
        }
        //Footer rows
        List<WebElement> footerRows = getWrappedElement().findElements(By.cssSelector("tfoot tr"));
        rows.addAll(footerRows);

        return rows;
    }

    public void assertCellText(int rowIdx, int colIdx, String text){
        Reporter.log("<b>Assert " + getElementType() + " cell has text</b> <br> Page | Element | Row | Column | Text => " + getPageName() + " | " + getElementName() + " | " + rowIdx + " | " + colIdx + " | " + text + "<br>");
        Assertions.assertThat(getCellAtIndex(rowIdx, colIdx).getText().trim()).isEqualToIgnoringCase(text);
    }

    public void assertCellContainsText(int rowIdx, int colIdx, String text){
        Reporter.log("<b>Assert " + getElementType() + " cell contains text</b> <br> Page | Element | Row | Column | Text => " + getPageName() + " | " + getElementName() + " | " + rowIdx + " | " + colIdx + " | " + text + "<br>");
        Assertions.assertThat(getCellAtIndex(rowIdx, colIdx).getText().trim()).containsIgnoringCase(text);
    }

    public void assertCellDoesNotContainText(int rowIdx, int colIdx, String text){
        Reporter.log("<b>Assert " + getElementType() + " cell doesn't contain text</b> <br> Page | Element | Row | Column | Text => " + getPageName() + " | " + getElementName() + " | " + rowIdx + " | " + colIdx + " | " + text + "<br>");
        Assertions.assertThat(getCellAtIndex(rowIdx, colIdx).getText().trim()).doesNotContain(text);
    }

    private boolean isSortIconDisplayed(int colIdx){
        try{
            return getCellAtIndex(0, colIdx).findElement(By.xpath(".//span[contains(@class, 'DataTables_sort_icon')]")).isDisplayed();
        }catch (NoSuchElementException e){
            //do nothing
        }
        return false;
    }

    public void assertColumnSortable(int colIdx){
        Reporter.log("<b>Assert column " + colIdx + " is sortable<b><br> Page | Element | Column => " + getPageName() + " | " + getElementName() + " | " + colIdx + "<br>");
        Assertions.assertThat(isSortIconDisplayed(colIdx)).isTrue();
    }

    public void assertColumnNotSortable(int colIdx){
        Reporter.log("<b>Assert column " + colIdx + " is not sortable<b><br> Page | Element | Column => " + getPageName() + " | " + getElementName() + " | " + colIdx + "<br>");
        Assertions.assertThat(isSortIconDisplayed(colIdx)).isFalse();
    }

}