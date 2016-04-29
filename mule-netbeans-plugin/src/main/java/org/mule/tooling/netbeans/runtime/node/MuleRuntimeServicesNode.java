/*
 * Copyright 2015 facundolopezkaufmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mule.tooling.netbeans.runtime.node;

import org.mule.tooling.netbeans.runtime.ui.MuleHomeView;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.core.ide.ServicesTabNodeRegistration;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle.Messages;
import org.mule.tooling.netbeans.api.MuleRuntime;
import org.mule.tooling.netbeans.api.MuleSupport;
import org.mule.tooling.netbeans.common.IconUtil;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Facundo Lopez Kaufmann
 */
@ServicesTabNodeRegistration(
        name = MuleRuntimeServicesNode.NODE_NAME,
        displayName = "#MuleRuntimeSupport_DisplayName",
        shortDescription = "#MuleRuntimeSupport_ShortDescription",
        iconResource = IconUtil.MULE_ICON,
        position = 1000)
@Messages({
    "MuleRuntimeSupport_DisplayName=Mule Runtimes",
    "MuleRuntimeSupport_ShortDescription=Mule Runtime Support",    
    "MuleRuntimeSupport_AddRuntimeActionTitle=Add Runtime"
})
public class MuleRuntimeServicesNode extends AbstractNode {

    static final String NODE_NAME = "muleruntimes"; // NOI18N

    private MuleRuntimeServicesNode() {
        super(Children.create(new MuleRuntimeNodesFactory(), true));
        setName(NODE_NAME);
        setDisplayName(Bundle.MuleRuntimeSupport_DisplayName());
        setIconBaseWithExtension(IconUtil.MULE_ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            new AddRuntimeAction()
        };
    }

    private static class AddRuntimeAction extends AbstractAction {

        public AddRuntimeAction() {
            super(Bundle.MuleRuntimeSupport_AddRuntimeActionTitle());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Path muleHome = MuleHomeView.capture();
            if(muleHome != null) {
                MuleSupport.getMuleRuntime(muleHome).register();
            }
        }
    }

    private static class MuleRuntimeNodesFactory extends ChildFactory.Detachable<MuleRuntime> implements ChangeListener {

        @Override
        protected boolean createKeys(List<MuleRuntime> toPopulate) {
            toPopulate.addAll(MuleSupport.getRegistry().getRegisteredRuntimes());
            Collections.sort(toPopulate, new Comparator<MuleRuntime>() {
                @Override
                public int compare(MuleRuntime o1, MuleRuntime o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            return true;
        }

        @Override
        protected Node createNodeForKey(MuleRuntime key) {
            return new SingleRuntimeNode(key);
        }

        @Override
        protected void addNotify() {
            MuleSupport.getRegistry().addChangeListener(this);
        }

        @Override
        protected void removeNotify() {
            MuleSupport.getRegistry().removeChangeListener(this);
        }

        @Override
        public void stateChanged(ChangeEvent event) {
            refresh(true);
        }
    }
}