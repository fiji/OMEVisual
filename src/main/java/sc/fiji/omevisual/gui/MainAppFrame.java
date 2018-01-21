/*
 * The MIT License
 *
 * Copyright 2016 Fiji.
 *
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
 */
package sc.fiji.omevisual.gui;

import java.io.IOException;

import javax.swing.JFrame;

import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import loci.formats.ome.OMEXMLMetadata;
import net.imagej.ImageJ;
import net.imagej.display.ImageDisplay;
import sc.fiji.omevisual.gui.view.RootLayoutController;

/**
 * This class is called from the ImageJ plugin.
 *
 * @author Hadrien Mary
 */
public class MainAppFrame extends JFrame {

	@Parameter
	private LogService log;

	private final ImageJ ij;

	private JFXPanel fxPanel;
	private final ImageDisplay image;
	private final OMEXMLMetadata md;

	public MainAppFrame(ImageJ ij, ImageDisplay image, OMEXMLMetadata md) {
		ij.context().inject(this);
		this.ij = ij;
		this.image = image;
		this.md = md;
	}

	/**
	 * Create the JFXPanel that make the link between Swing (IJ) and JavaFX plugin.
	 */
	public void init() {
		this.fxPanel = new JFXPanel();
		this.add(this.fxPanel);
		this.setVisible(true);

		// The call to runLater() avoid a mix between JavaFX thread and Swing thread.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initFX(fxPanel);
			}
		});

	}

	public void initFX(JFXPanel fxPanel) {
		// Init the root layout
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainAppFrame.class.getResource("/sc/fiji/omevisual/gui/view/RootLayout.fxml"));
			AnchorPane rootLayout = (AnchorPane) loader.load();

			// Get the controller and add an ImageJ context to it.
			RootLayoutController controller = loader.getController();
			controller.setContext(ij.context());
			controller.setImage(this.image);

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			this.fxPanel.setScene(scene);

			// Resize the JFrame to the JavaFX scene
			this.setSize((int) scene.getWidth(), (int) scene.getHeight());

			controller.fill(md);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
