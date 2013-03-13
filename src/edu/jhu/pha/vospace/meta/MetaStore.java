/*******************************************************************************
 * Copyright 2013 Johns Hopkins University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package edu.jhu.pha.vospace.meta;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.jhu.pha.vospace.node.Node;
import edu.jhu.pha.vospace.node.Node.PropertyType;
import edu.jhu.pha.vospace.node.NodeInfo;
import edu.jhu.pha.vospace.node.NodeType;
import edu.jhu.pha.vospace.node.VospaceId;

/**
 * This interface represents a metadata store for VOSpace 
 */
public interface MetaStore {

    /**
     * Get the node bytes
     * @param identifier
     * @return Node bytes
     */
	public byte[] getNodeBytes(VospaceId identifier);
 
    /**
     * Get the node children
     * @param uri
     * @param searchDeep
     * @param includeDeleted
     * @return
     */
    public NodesList getNodeChildren(VospaceId uri, boolean searchDeep, boolean includeDeleted, int start, int count) ;

    public NodeInfo getNodeInfo(VospaceId uri);

    /**
     * Get the type of the object with the specified identifier
     * @param identifier
     * @return
     */
    public NodeType getType(VospaceId identifier) ;

    //public void incrementRevision(VospaceId uri);


    /**
     * Check whether the object with the specified identifier is in the store
     * @param identifier
     * @return
     */
    public boolean isStored(VospaceId identifier);
    
    public void markRemoved(VospaceId uri);

	/**
     * Remove the metadata for the specified identifier
     * @param identifier
     */
    public void remove(VospaceId identifier);

	public List<VospaceId> search(VospaceId identifier, String searchPattern, int fileLimit, boolean includeDeleted);

    /**
     * Store the metadata for the specified identifier
     * @param identifier
     * @param type
     * @param metadata
     */
    public void storeData(VospaceId identifier, NodeType type) ;

	public void storeInfo(VospaceId identifier, NodeInfo info);

	/**
     * Update the metadata for the specified identifier including updating the
     * identifier
     * @param identifier
     * @param newIdentifier
     * @param metadata
     */
    public void updateData(VospaceId identifier, VospaceId newIdentifier) ;

	/**
     * Update the specified properties
     * @param properties
     */
    public void updateUserProperties(VospaceId identifier, Map<String, String> properties);

	/**
     * Get share ID for node
     */
	String createShare(VospaceId identifier, String groupId, boolean write_perm);

	public Map<String, String> getProperties(VospaceId identifier, PropertyType properties);

}