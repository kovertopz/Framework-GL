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
package net.smert.frameworkgl.gui.render.factory;

import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend;
import java.nio.ByteBuffer;
import net.smert.frameworkgl.gui.render.ByteBufferedImage;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class ImageFactory implements de.lessvoid.nifty.render.batch.spi.ImageFactory {

    @Override
    public BatchRenderBackend.Image create(ByteBuffer buffer, int imageWidth, int imageHeight) {
        return new ByteBufferedImage(buffer, imageWidth, imageHeight);
    }

    @Override
    public ByteBuffer asByteBuffer(BatchRenderBackend.Image image) {
        return (image instanceof ByteBufferedImage) ? ((ByteBufferedImage) image).getBuffer() : null;
    }

}
