package qt.qtbutton.model;

/**
 * Created by Надюша on 09.06.2016.
 */
public class ProductLine {
    private int id;
    private String product;
    private int numberOfProduct;
    private String category;
    private String comment;
    private boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getNumberOfProduct() {
        return numberOfProduct;
    }

    public void setNumberOfProduct(int numberOfProduct) {
        this.numberOfProduct = numberOfProduct;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ProductLine(int id, String product, int numberOfProduct, String category, String comment, boolean active) {

        this.id = id;
        this.product = product;
        this.numberOfProduct = numberOfProduct;
        this.category = category;
        this.comment = comment;
        this.active = active;
    }
}
