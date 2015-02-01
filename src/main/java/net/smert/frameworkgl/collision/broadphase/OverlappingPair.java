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
package net.smert.frameworkgl.collision.broadphase;

import java.util.Objects;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class OverlappingPair {

    public BroadphaseProxy proxy0;
    public BroadphaseProxy proxy1;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.proxy0);
        hash = 89 * hash + Objects.hashCode(this.proxy1);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OverlappingPair other = (OverlappingPair) obj;
        if (!Objects.equals(this.proxy0, other.proxy0)) {
            return false;
        }
        return Objects.equals(this.proxy1, other.proxy1);
    }

}
