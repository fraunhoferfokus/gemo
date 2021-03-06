/**
 * Copyright (c) 2012, 2013 Fraunhofer Institute FOKUS
 *
 * This file is part of Open Data Platform.
 *
 * Open Data Platform is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Open Data Plaform is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with Open Data Platform.  If not, see <http://www.gnu.org/licenses/agpl-3.0>.
 */

/**
 * 
 */
package de.fhg.fokus.odp.registry.ckan.json;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author sim
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GroupBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2526804296948451352L;

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String display_name;

    @JsonProperty
    private String title;

    @JsonProperty
    private String description;

    @JsonProperty
    private String packages;

    @JsonProperty
    private String approval_status;

    @JsonProperty
    private String state;

    @JsonProperty
    private String image_url;

    @JsonProperty
    private String revision_id;

    @JsonProperty
    private String type;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the count
     */
    public String getPackages() {
        return packages;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setPackages(String packages) {
        this.packages = packages;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return display_name;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(String display_name) {
        this.display_name = display_name;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the approval_status
     */
    public String getApprovalStatus() {
        return approval_status;
    }

    /**
     * @param approval_status
     *            the approval_status to set
     */
    public void setApprovalStatus(String approval_status) {
        this.approval_status = approval_status;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the image_url
     */
    public String getImage_url() {
        return image_url;
    }

    /**
     * @param image_url
     *            the image_url to set
     */
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    /**
     * @return the revision_id
     */
    public String getRevision_id() {
        return revision_id;
    }

    /**
     * @param revision_id
     *            the revision_id to set
     */
    public void setRevision_id(String revision_id) {
        this.revision_id = revision_id;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
