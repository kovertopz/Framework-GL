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
package net.smert.frameworkgl.examples;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class JFrameMainController {

    // Directory that contains all the examples
    private final static String EXAMPLE_DIRECTORY = "examples/";
    private final static String ROOT_DIRECTORY_PREFIX = "net/smert/frameworkgl/";

    private final JFrameMainModel jFrameMainModel;
    private final JFrameMainView jFrameMainView;
    private final String[] mainArgs;

    public JFrameMainController(String[] args) {
        jFrameMainModel = new JFrameMainModel(EXAMPLE_DIRECTORY, ROOT_DIRECTORY_PREFIX);
        jFrameMainView = new JFrameMainView(this);
        mainArgs = args;
    }

    public void actionRunExample(String exampleClass) {
        jFrameMainModel.runDemo(exampleClass, mainArgs);
    }

    public void actionDefault() {
        jFrameMainView.setListModel(jFrameMainModel.getListModelWithMainClasses());
    }

    public void displayWindow(int x, int y, int width, int height, String title) {
        jFrameMainView.setBounds(x, y, width, height);
        jFrameMainView.setTitle(title);
        if ((x == -1) && (y == -1)) {
            jFrameMainView.setLocationRelativeTo(null);
        }
        jFrameMainView.setVisible(true);
    }

}
