package components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Counter extends HBox {
    private double width;
    private double height;
    private TextField countField;
    private Label countLabel;

    /**
     * Calculates the necessary left/right (can be used for either) margin
     * needed to horizontally center the child node in the specified parent node.
     * @param parentWidth The parent node to center within
     * @param childWidth The child node to be centered
     * @return The left/right margin
     */
    private double calculateHCenter(double parentWidth, double childWidth){
        return ((parentWidth-childWidth)/2);
    }

    /**
     * Generates an Insets to horizontally center the child relative to the parent
     * @param insets The Insets to center based off
     * @param parentWidth The width of the parent to center within
     * @param childWidth The width of the child to center
     * @return The new Insets
     */
    private Insets horizontallyCenter(Insets insets, double parentWidth, double childWidth){
        double margin = (parentWidth-childWidth)/2.0;
        return new Insets(insets.getTop(),margin,insets.getBottom(),margin); 
    }

    public void setCount(String count){
        //System.out.println(count);
        this.countField.setText(count);
    }

    public void setText(String text){
        this.countLabel.setText(text);
    }

    /**
     * Generates a box containing the timer label and timer time inside the control panel
     * @param controlPanelWidth The width of the control panel
     * @param controlPanelHeight The height of the control panel
     */
    public Counter(double width, double height, double tileSize, String label, String count){
        this.width = width;
        this.height = tileSize/4;
        double centerMargin;
        this.setPrefSize(this.width, this.height);
        VBox.setMargin(this, horizontallyCenter(new Insets(height*0.01),width,this.width));

        countLabel = createTimerLabel(label);
        countField = createCountField(count);
        centerMargin = calculateHCenter(this.width, countLabel.getWidth()+countField.getWidth());
        HBox.setMargin(countLabel, new Insets(0,0,0,centerMargin));
        HBox.setMargin(countField, new Insets(0,centerMargin,0,0));
        //timerPanel.setStyle("-fx-background-color: #000000;");

        this.getChildren().addAll(
            countLabel,
            countField
        );

    }

    /**
     * Generates the counter's label
     * @return The counter's label
     */
    private Label createTimerLabel(String timeText){
        final double countLabelWidth = this.width*0.4;
        final double countLabelHeight = this.height;

        Label label = new Label(timeText+":");
        label.setPrefSize(countLabelWidth, countLabelHeight);
        return label;
    }

    /**
     * Generates the counter's count field (the part that displays the count)
     * @return The counter's count field
     */
    private TextField createCountField(String count){
        final double countWidth = this.width*0.5;
        final double countHeight = this.height;
        TextField textField = new TextField(count);
        textField.setPrefSize(countWidth, countHeight);
        textField.editableProperty().set(false);
        //stop auto highlighting upon GUI rendering
        textField.focusTraversableProperty().set(false);
        return textField;
    }
}
