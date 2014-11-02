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
package net.smert.frameworkgl.openal;

import net.smert.frameworkgl.openal.factory.ALFactory;
import net.smert.frameworkgl.openal.helpers.ALBufferHelper;
import net.smert.frameworkgl.openal.helpers.ALListenerHelper;
import net.smert.frameworkgl.openal.helpers.ALSourceHelper;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class AL {

    public static ALFactory alFactory;
    public static ALBufferHelper bufferHelper;
    public static ALListenerHelper listenerHelper;
    public static ALSourceHelper sourceHelper;
    public static OpenAL openal;

}
