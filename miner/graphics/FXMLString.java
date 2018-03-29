package scripts.miner.graphics;

public class FXMLString {
	
	public static String get = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"\r\n" + 
			"<?import javafx.scene.control.Button?>\r\n" + 
			"<?import javafx.scene.control.CheckBox?>\r\n" + 
			"<?import javafx.scene.control.ComboBox?>\r\n" + 
			"<?import javafx.scene.control.Label?>\r\n" + 
			"<?import javafx.scene.control.Slider?>\r\n" + 
			"<?import javafx.scene.control.TextField?>\r\n" + 
			"<?import javafx.scene.layout.ColumnConstraints?>\r\n" + 
			"<?import javafx.scene.layout.GridPane?>\r\n" + 
			"<?import javafx.scene.layout.HBox?>\r\n" + 
			"<?import javafx.scene.layout.RowConstraints?>\r\n" + 
			"<?import javafx.scene.layout.VBox?>\r\n" + 
			"<?import javafx.scene.text.Font?>\r\n" + 
			"<?import javafx.scene.text.Text?>\r\n" + 
			"\r\n" + 
			"<GridPane alignment=\"CENTER\" prefHeight=\"335.0\" prefWidth=\"302.0\" xmlns=\"http://javafx.com/javafx/8.0.141\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller=\"scripts.miner.graphics.GUIController\">\r\n" + 
			"  <columnConstraints>\r\n" + 
			"    <ColumnConstraints hgrow=\"SOMETIMES\" maxWidth=\"600.0\" minWidth=\"10.0\" prefWidth=\"258.0\" />\r\n" + 
			"  </columnConstraints>\r\n" + 
			"  <rowConstraints>\r\n" + 
			"    <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"    <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"      <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"      <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"      <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"      <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"      <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"      <RowConstraints maxHeight=\"50.33332824707031\" minHeight=\"10.0\" prefHeight=\"23.333328247070312\" vgrow=\"SOMETIMES\" />\r\n" + 
			"      <RowConstraints maxHeight=\"61.66667175292969\" minHeight=\"10.0\" prefHeight=\"61.66667175292969\" vgrow=\"SOMETIMES\" />\r\n" + 
			"  </rowConstraints>\r\n" + 
			"   <children>\r\n" + 
			"      <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\">\r\n" + 
			"         <children>\r\n" + 
			"            <Text strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"nMiner\" textAlignment=\"CENTER\">\r\n" + 
			"               <font>\r\n" + 
			"                  <Font size=\"23.0\" />\r\n" + 
			"               </font>\r\n" + 
			"            </Text>\r\n" + 
			"         </children>\r\n" + 
			"      </HBox>\r\n" + 
			"      <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\" GridPane.rowIndex=\"7\">\r\n" + 
			"         <children>\r\n" + 
			"            <Button mnemonicParsing=\"false\" onAction=\"#startScript\" text=\"Start Script\" />\r\n" + 
			"         </children>\r\n" + 
			"      </HBox>\r\n" + 
			"      <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\" GridPane.rowIndex=\"4\">\r\n" + 
			"         <children>\r\n" + 
			"            <CheckBox fx:id=\"bank\" mnemonicParsing=\"false\" text=\"Bank Ores (Otherwise we drop them)\" />\r\n" + 
			"         </children>\r\n" + 
			"      </HBox>\r\n" + 
			"      <GridPane GridPane.rowIndex=\"1\">\r\n" + 
			"         <columnConstraints>\r\n" + 
			"            <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"            <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"         </columnConstraints>\r\n" + 
			"         <rowConstraints>\r\n" + 
			"            <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"         </rowConstraints>\r\n" + 
			"         <children>\r\n" + 
			"            <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\" GridPane.columnIndex=\"1\">\r\n" + 
			"               <children>\r\n" + 
			"                  <ComboBox fx:id=\"rock\" prefWidth=\"100.0\" />\r\n" + 
			"               </children>\r\n" + 
			"            </HBox>\r\n" + 
			"            <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\">\r\n" + 
			"               <children>\r\n" + 
			"                  <Label text=\"Rock:\" />\r\n" + 
			"               </children>\r\n" + 
			"            </HBox>\r\n" + 
			"         </children>\r\n" + 
			"      </GridPane>\r\n" + 
			"      <GridPane GridPane.rowIndex=\"2\">\r\n" + 
			"         <columnConstraints>\r\n" + 
			"            <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"            <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"         </columnConstraints>\r\n" + 
			"         <rowConstraints>\r\n" + 
			"            <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"         </rowConstraints>\r\n" + 
			"         <children>\r\n" + 
			"            <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\" GridPane.columnIndex=\"1\">\r\n" + 
			"               <children>\r\n" + 
			"                  <TextField fx:id=\"miningTile\" editable=\"false\" prefWidth=\"100.0\" />\r\n" + 
			"               </children>\r\n" + 
			"            </HBox>\r\n" + 
			"            <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\">\r\n" + 
			"               <children>\r\n" + 
			"                  <Label text=\"Mining Tile:\" />\r\n" + 
			"               </children>\r\n" + 
			"            </HBox>\r\n" + 
			"         </children>\r\n" + 
			"      </GridPane>\r\n" + 
			"      <GridPane GridPane.rowIndex=\"3\">\r\n" + 
			"         <columnConstraints>\r\n" + 
			"            <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"            <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"         </columnConstraints>\r\n" + 
			"         <rowConstraints>\r\n" + 
			"            <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"         </rowConstraints>\r\n" + 
			"         <children>\r\n" + 
			"            <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\" GridPane.columnIndex=\"1\">\r\n" + 
			"               <children>\r\n" + 
			"                  <Button mnemonicParsing=\"false\" onAction=\"#getCurrentTile\" text=\"Get Current Tile\" />\r\n" + 
			"               </children>\r\n" + 
			"            </HBox>\r\n" + 
			"            <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\" />\r\n" + 
			"         </children>\r\n" + 
			"      </GridPane>\r\n" + 
			"      <GridPane style=\"-fx-border-radius: 5; -fx-border-color: black; -fx-border-insets: 2 2 2 2; -fx-border-width: 0.5;\" GridPane.rowIndex=\"8\">\r\n" + 
			"        <columnConstraints>\r\n" + 
			"          <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"          <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"        </columnConstraints>\r\n" + 
			"        <rowConstraints>\r\n" + 
			"          <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"        </rowConstraints>\r\n" + 
			"         <children>\r\n" + 
			"            <VBox prefHeight=\"200.0\" prefWidth=\"100.0\" GridPane.columnIndex=\"1\">\r\n" + 
			"               <children>\r\n" + 
			"                  <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\">\r\n" + 
			"                     <children>\r\n" + 
			"                        <ComboBox fx:id=\"settingsBox\" maxWidth=\"100.0\" minWidth=\"100.0\" prefWidth=\"100.0\" />\r\n" + 
			"                     </children>\r\n" + 
			"                  </HBox>\r\n" + 
			"                  <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\">\r\n" + 
			"                     <children>\r\n" + 
			"                        <Button mnemonicParsing=\"false\" onAction=\"#loadSettings\" text=\"Load Settings\" />\r\n" + 
			"                     </children>\r\n" + 
			"                  </HBox>\r\n" + 
			"               </children>\r\n" + 
			"            </VBox>\r\n" + 
			"            <VBox prefHeight=\"200.0\" prefWidth=\"100.0\">\r\n" + 
			"               <children>\r\n" + 
			"                  <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\">\r\n" + 
			"                     <children>\r\n" + 
			"                        <TextField fx:id=\"settingsName\" alignment=\"CENTER\" maxWidth=\"100.0\" minWidth=\"100.0\" prefWidth=\"100.0\" promptText=\"Settings Name\" />\r\n" + 
			"                     </children>\r\n" + 
			"                  </HBox>\r\n" + 
			"                  <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\">\r\n" + 
			"                     <children>\r\n" + 
			"                        <Button mnemonicParsing=\"false\" onAction=\"#saveSettings\" text=\"Save Settings\" />\r\n" + 
			"                     </children>\r\n" + 
			"                  </HBox>\r\n" + 
			"               </children>\r\n" + 
			"            </VBox>\r\n" + 
			"         </children>\r\n" + 
			"      </GridPane>\r\n" + 
			"      <GridPane GridPane.rowIndex=\"5\">\r\n" + 
			"        <columnConstraints>\r\n" + 
			"          <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"            <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"        </columnConstraints>\r\n" + 
			"        <rowConstraints>\r\n" + 
			"          <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"        </rowConstraints>\r\n" + 
			"         <children>\r\n" + 
			"            <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\">\r\n" + 
			"               <children>\r\n" + 
			"                  <Label text=\"Reaction Time Scale:\" />\r\n" + 
			"               </children>\r\n" + 
			"            </HBox>\r\n" + 
			"            <HBox prefHeight=\"100.0\" prefWidth=\"200.0\" GridPane.columnIndex=\"1\">\r\n" + 
			"               <children>\r\n" + 
			"                  <Slider fx:id=\"scale\" blockIncrement=\"0.1\" majorTickUnit=\"0.2\" max=\"1.0\" min=\"0.1\" minorTickCount=\"1\" showTickLabels=\"true\" showTickMarks=\"true\" snapToTicks=\"true\" value=\"1.0\" />\r\n" + 
			"               </children>\r\n" + 
			"            </HBox>\r\n" + 
			"         </children>\r\n" + 
			"      </GridPane>\r\n" + 
			"      <GridPane GridPane.rowIndex=\"6\">\r\n" + 
			"        <columnConstraints>\r\n" + 
			"          <ColumnConstraints hgrow=\"SOMETIMES\" minWidth=\"10.0\" prefWidth=\"100.0\" />\r\n" + 
			"        </columnConstraints>\r\n" + 
			"        <rowConstraints>\r\n" + 
			"          <RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />\r\n" + 
			"        </rowConstraints>\r\n" + 
			"         <children>\r\n" + 
			"            <HBox alignment=\"CENTER\" prefHeight=\"100.0\" prefWidth=\"200.0\">\r\n" + 
			"               <children>\r\n" + 
			"                  <CheckBox fx:id=\"hop\" mnemonicParsing=\"false\" text=\"Hop World If Empty\" />\r\n" + 
			"               </children>\r\n" + 
			"            </HBox>\r\n" + 
			"         </children>\r\n" + 
			"      </GridPane>\r\n" + 
			"   </children>\r\n" + 
			"</GridPane>\r\n" + 
			"" 
			;

}
