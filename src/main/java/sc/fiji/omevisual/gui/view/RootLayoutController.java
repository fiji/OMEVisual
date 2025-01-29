/*-
 * #%L
 * A Fiji plugin to easily visualize OME metadata.
 * %%
 * Copyright (C) 2016 - 2025 Fiji developers.
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

package sc.fiji.omevisual.gui.view;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.scijava.Context;
import org.scijava.convert.ConvertService;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import loci.formats.ome.OMEXMLMetadata;
import net.imagej.axis.AxisType;
import net.imagej.display.ImageDisplay;
import sc.fiji.omevisual.OMEUtils;
import sc.fiji.omevisual.gui.model.GenericModel;
import sc.fiji.omevisual.gui.model.ImageModel;
import sc.fiji.omevisual.gui.model.TiffDataModel;

/**
 * FXML Controller class
 *
 * @author Hadrien Mary
 */
public class RootLayoutController implements Initializable {

	@Parameter
	private LogService log;

	@Parameter
	private ConvertService convert;

	@FXML
	private TreeView testTree;

	@FXML
	private CheckBox syncWithImageBox;

	@FXML
	private TableView<List<String>> imageTable;

	@FXML
	private TableColumn<List<String>, String> imageNameColumn;

	@FXML
	private TableColumn<List<String>, String> imageValueColumn;

	@FXML
	private TableView<List<String>> tiffDataTable;

	@FXML
	private TableColumn<List<String>, String> tiffDataNameColumn;

	@FXML
	private TableColumn<List<String>, String> tiffDataValueColumn;
	private ImageDisplay image;

	@Override
	public void initialize(URL location, ResourceBundle resources) {}

	public void setContext(Context context) {
		context.inject(this);
	}

	/**
	 * Not the best place to put ImageDisplay in the gui package but it's easier
	 * for now. I take any ideas to improve this.
	 *
	 * @param image
	 */
	public void setImage(ImageDisplay image) {
		this.image = image;
	}

	public void fill(OMEXMLMetadata md) {

		TreeItem<GenericModel<?>> root = new TreeItem<>();
		testTree.setRoot(root);
		testTree.setShowRoot(false);

		// Build and populate the tree
		for (int i = 0; i < md.getImageCount(); i++) {
			ImageModel imageModel = new ImageModel(i, md);
			TreeItem<GenericModel<?>> imageItem = new TreeItem<>(imageModel);
			root.getChildren().add(imageItem);

			for (int j = 0; j < md.getTiffDataCount(i); j++) {
				TiffDataModel dataModel = new TiffDataModel(i, j, md, imageModel);
				// md.getTiffDataFirstC(i, j),
				// md.getTiffDataFirstT(i, j), md.getTiffDataFirstZ(i, j),
				// md.getTiffDataIFD(i,
				// j));
				TreeItem<GenericModel<?>> dataItem = new TreeItem<>(dataModel);
				imageItem.getChildren().add(dataItem);

			}
		}

		// Handle selection in the tree
		testTree.getSelectionModel().selectedItemProperty().addListener((
			ObservableValue obs, Object oldValue, Object newValue) -> {
			TreeItem<GenericModel<?>> selectedItem =
				(TreeItem<GenericModel<?>>) newValue;
			GenericModel<?> model = selectedItem.getValue();

			if (model instanceof TiffDataModel) {
				// Display informations relative to TiffData
				populateTiffDataInformations((TiffDataModel) model);

			}

			if (model instanceof ImageModel) {
				// Clear TiffData informations
				this.tiffDataTable.getItems().clear();

				// Display informations relative to Image
				populateImageInformations((ImageModel) model);
			}

			if (syncWithImageBox.isSelected()) {
				if (model instanceof TiffDataModel) {
					// Sync the current selected item with image display
					Map<AxisType, Long> positions = ((TiffDataModel) model).getPosition();
					OMEUtils.setPosition(this.image, positions, this.convert);
				}
			}
		});

	}

	private void populateImageInformations(ImageModel model) {

		this.imageTable.getItems().clear();

		this.imageNameColumn.setCellValueFactory(data -> {
			return new ReadOnlyStringWrapper(data.getValue().get(0));
		});

		this.imageValueColumn.setCellValueFactory(data -> {
			return new ReadOnlyStringWrapper(data.getValue().get(1));
		});

		for (List<String> row : model.getInformationsRow()) {
			this.imageTable.getItems().add(row);
		}

	}

	private void populateTiffDataInformations(TiffDataModel model) {
		ImageModel imageModel = model.getImageModel();
		this.populateImageInformations(imageModel);

		// Populate tiffData
		this.tiffDataTable.getItems().clear();

		this.tiffDataNameColumn.setCellValueFactory(data -> {
			return new ReadOnlyStringWrapper(data.getValue().get(0));
		});

		this.tiffDataValueColumn.setCellValueFactory(data -> {
			return new ReadOnlyStringWrapper(data.getValue().get(1));
		});

		for (List<String> row : model.getInformationsRow()) {
			this.tiffDataTable.getItems().add(row);
		}
	}
}
