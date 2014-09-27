/**
 * Copyright 2012 Jason Sorensen (sorensenj@smert.net)
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
package net.smert.jreactphysics3d.framework.opengl.image.jpg;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.smert.jreactphysics3d.framework.opengl.image.DefaultImageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class JPGReader extends DefaultImageReader {

    private final static Logger log = LoggerFactory.getLogger(JPGReader.class);

    @Override
    public BufferedImage load(String filename) throws IOException {
        log.info("Loading JPG image: {}", filename);
        return super.load(filename);
    }

}
