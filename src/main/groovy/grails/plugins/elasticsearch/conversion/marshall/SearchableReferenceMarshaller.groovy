/*
 * Copyright 2002-2010 the original author or authors.
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

package grails.plugins.elasticsearch.conversion.marshall

import grails.plugins.elasticsearch.mapping.SearchableClassMapping
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Marshall only searchable class ID.
 */
class SearchableReferenceMarshaller extends DefaultMarshaller {

    /** Domain class we refer to */
    Class refClass

    /**
     * Marshall domain class id.
     * @param object object to be marshalled
     * @return raw domain class identifier.
     */
    protected doMarshall(object) {
        assert refClass != null
        assert refClass.isAssignableFrom(object.getClass()): "Marshalled object ${object} is not [${refClass}]."

        // TODO: encapsulate me
        SearchableClassMapping scm = marshallingContext.findMappingContext(refClass)
        assert scm

        def referenceMap = [id: InvokerHelper.invokeMethod(object, 'ident', null)]

        def parentProperty = scm.propertiesMapping.find { it.isParent() }
        if (parentProperty) {
            referenceMap.'parent' = object."$parentProperty.propertyName".ident()
        }
        return referenceMap
    }
}
