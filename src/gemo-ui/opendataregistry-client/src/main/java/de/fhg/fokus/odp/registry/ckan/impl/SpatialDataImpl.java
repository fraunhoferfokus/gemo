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
package de.fhg.fokus.odp.registry.ckan.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.fhg.fokus.odp.registry.model.Coordinate;
import de.fhg.fokus.odp.registry.model.SpatialData;
import de.fhg.fokus.odp.registry.model.SpatialEnumType;

/**
 * @author sim
 * 
 */
public class SpatialDataImpl implements SpatialData, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8914868607356372491L;

    private SpatialEnumType type;

    private List<Coordinate> coordinates;

    public SpatialDataImpl(SpatialEnumType type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.fhg.fokus.odp.registry.model.SpatialData#getType()
     */
    @Override
    public SpatialEnumType getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.fhg.fokus.odp.registry.model.SpatialData#getCoordinates()
     */
    @Override
    public List<Coordinate> getCoordinates() {
        if (coordinates == null) {
            coordinates = new ArrayList<Coordinate>();
        }
        return coordinates;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.fhg.fokus.odp.registry.model.SpatialData#addCoordinate(double, double)
     */
    @Override
    public Coordinate addCoordinate(double x, double y) {
        Coordinate coordinate = new CoordinateImpl(x, y);
        getCoordinates().add(coordinate);
        return coordinate;
    }

}
