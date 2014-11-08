/**
 * Copyright 2014 Jason Sorensen (sorensenj@smert.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.smert.frameworkgl.examples.gl3gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.events.NiftyMouseEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryReleaseEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryReleaseEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryReleaseEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseWheelEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.IOException;
import net.smert.frameworkgl.Fw;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class StartScreenController implements ScreenController {

    private Label mouseEventsMoveText;
    private Label mouseEventsPrimaryText;
    private Label mouseEventsSecondaryText;
    private Label mouseEventsTertiaryText;
    private Label mouseEventsText;
    private Label mouseEventsWheelText;
    private Nifty nifty;
    private Screen screen;

    public void quit() {
        try {
            Fw.gui.loadGuiFromXml("gl3gui.xml", "quit");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Mouse general
    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementMouse(String id, NiftyMouseEvent event) {
        mouseEventsText.setText(
                id + " -> " + event.getMouseX() + ", " + event.getMouseY() + ", " + event.getMouseWheel() + "\n"
                + event.isButton0Down() + ", " + event.isButton1Down() + ", " + event.isButton2Down() + "\n"
                + event.isButton0Release() + ", " + event.isButton1Release() + ", " + event.isButton2Release());
    }

    // Mouse move
    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementMouseMove(String id, NiftyMouseMovedEvent event) {
        mouseEventsMoveText.setText(event.getMouseX() + ", " + event.getMouseY());
    }

    // Mouse wheel
    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementMouseWheel(String id, NiftyMouseWheelEvent event) {
        mouseEventsWheelText.setText(String.valueOf(event.getMouseWheel()));
    }

    // Primary
    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementPrimaryClick(String id, NiftyMousePrimaryClickedEvent event) {
        mouseEventsPrimaryText.setText("onElementPrimaryClick");
    }

    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementPrimaryClickMove(String id, NiftyMousePrimaryClickedMovedEvent event) {
        mouseEventsPrimaryText.setText("onElementPrimaryClickMove");
    }

    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementPrimaryRelease(String id, NiftyMousePrimaryReleaseEvent event) {
        mouseEventsPrimaryText.setText("onElementPrimaryRelease");
    }

    // Secondary
    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementSecondaryClick(String id, NiftyMouseSecondaryClickedEvent event) {
        mouseEventsSecondaryText.setText("onElementSecondaryClick");
    }

    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementSecondaryClickMove(String id, NiftyMouseSecondaryClickedMovedEvent event) {
        mouseEventsSecondaryText.setText("onElementSecondaryClickMove");
    }

    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementSecondaryRelease(String id, NiftyMouseSecondaryReleaseEvent event) {
        mouseEventsSecondaryText.setText("onElementSecondaryRelease");
    }

    // Tertiary
    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementTertiaryClick(String id, NiftyMouseTertiaryClickedEvent event) {
        mouseEventsTertiaryText.setText("onElementTertiaryClick");
    }

    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementTertiaryClickMove(String id, NiftyMouseTertiaryClickedMovedEvent event) {
        mouseEventsTertiaryText.setText("onElementTertiaryClickMove");
    }

    @NiftyEventSubscriber(id = "mouseEvents")
    public void onElementTertiaryRelease(String id, NiftyMouseTertiaryReleaseEvent event) {
        mouseEventsTertiaryText.setText("onElementTertiaryRelease");
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("StartScreenController bind");
        this.nifty = nifty;
        this.screen = screen;

        this.mouseEventsPrimaryText = screen.findNiftyControl("mouseEventsPrimaryText", Label.class);
        this.mouseEventsSecondaryText = screen.findNiftyControl("mouseEventsSecondaryText", Label.class);
        this.mouseEventsTertiaryText = screen.findNiftyControl("mouseEventsTertiaryText", Label.class);
        this.mouseEventsMoveText = screen.findNiftyControl("mouseEventsMoveText", Label.class);
        this.mouseEventsWheelText = screen.findNiftyControl("mouseEventsWheelText", Label.class);
        this.mouseEventsText = screen.findNiftyControl("mouseEventsText", Label.class);
    }

    @Override
    public void onStartScreen() {
        System.out.println("StartScreenController onStartScreen");

        try {
            NiftyMouse niftyMouse = nifty.getNiftyMouse();
            niftyMouse.registerMouseCursor("mouseId", "cursors/arrow-147279_640.png", 0, 0);
            niftyMouse.enableMouseCursor("mouseId");
        } catch (IOException e) {
            System.err.println("Loading the mouse cursor failed.");
        }
    }

    @Override
    public void onEndScreen() {
        System.out.println("StartScreenController onEndScreen");
    }

}
