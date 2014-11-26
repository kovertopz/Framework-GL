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
package net.smert.frameworkgl.gui.builders;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import net.smert.frameworkgl.gui.GuiXmlElement;
import net.smert.frameworkgl.gui.GuiXmlElementAttribute;
import net.smert.frameworkgl.gui.widgets.AbstractGuiControl;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class GuiControlBuilder {

    private final Map<String, Class> nameToClass;

    public GuiControlBuilder() {
        nameToClass = new HashMap<>();
    }

    private AbstractGuiControl newInstance(Class clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        Constructor foundConstructor = null;
        for (Constructor constructor : constructors) {
            if (constructor.getGenericParameterTypes().length == 0) {
                foundConstructor = constructor;
                break;
            }
        }
        if (foundConstructor == null) {
            throw new RuntimeException("Unable to find the default constructor for class: " + clazz.getCanonicalName());
        }
        try {
            return (AbstractGuiControl) foundConstructor.newInstance();
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    public AbstractGuiControl create(GuiXmlElement control) {
        Map<String, String> attributes = control.getAttributes();
        String attributeType = attributes.get(GuiXmlElementAttribute.TYPE);
        Class clazz = nameToClass.get(attributeType);
        if (clazz == null) {
            throw new RuntimeException("The control type has not been registered: " + attributeType);
        }
        AbstractGuiControl guiControl = newInstance(clazz);
        GuiBuilder.BuildCommon(guiControl, control);
        return guiControl;
    }

    public void register(String name, Class clazz) {
        if (!AbstractGuiControl.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("The class '" + clazz.getCanonicalName() + "' must extend from '"
                    + AbstractGuiControl.class.getCanonicalName() + "'");
        }
        name = name.toLowerCase();
        if (nameToClass.containsKey(name)) {
            throw new IllegalArgumentException("The name has already been registered: " + name);
        }
        nameToClass.put(name, clazz);
    }

    public void unregister(String name) {
        name = name.toLowerCase();
        if (!nameToClass.containsKey(name)) {
            throw new IllegalArgumentException("The name has not been registered: " + name);
        }
        nameToClass.remove(name);
    }

}
