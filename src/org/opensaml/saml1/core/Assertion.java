/*
 * Copyright [2005] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.saml1.core;

import java.util.List;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLObject;
import org.opensaml.xml.signature.SignableXMLObject;

/**
 * This interface defines how the object representing a SAML 1 <code> Assertion </code> element behaves.
 */
public interface Assertion extends SAMLObject, SignableXMLObject {

    /** Element name, no namespace. */

    public final static String LOCAL_NAME = "Assertion";

    /** Name for the attribute which defines Major Version (attribute's value must be 1) */
    public final static String MAJORVERSION_ATTRIB_NAME = "MajorVersion";

    /** Name for the attribute which defines Minor Version. */
    public final static String MINORVERSION_ATTRIB_NAME = "MinorVersion";

    /** Name for the attribute which defines Assertion ID */
    public final static String ASSERTIONID_ATTRIB_NAME = "AssertionID";

    /** Name for the attribute which defines Issuer */
    public final static String ISSUER_ATTRIB_NAME = "Issuer";

    /** Name for the attribute which defines the issue instant */
    public final static String ISSUEINSTANT_ATTRIB_NAME = "IssueInstant";

    /* attributes */

    /**
     * Get the MinorVersion attribute.
     * 
     * @return The stored MinorVersion
     */
    public int getMinorVersion();

    /**
     * List the MinorVersion (attribute).
     * 
     * @param version the value to List
     */
    public void setMinorVersion(int version);

    /**
     * Get the Issuer (which is an attribute) .
     * 
     * @return the Issuer
     */
    public String getIssuer();

    /**
     * List the Issuer (attribute).
     * 
     * @param Issuer the value to List
     */
    public void setIssuer(String Issuer);

    /**
     * Get the IssueInstant (attribute).
     * 
     * @return the Issue Instant (as a Date)
     */
    public DateTime getIssueInstant();

    /**
     * List the IssueInstance (attribute).
     * 
     * @param issueInstant the issue instant value to List
     */
    public void setIssueInstant(DateTime issueInstant);

    /* Singleton Elements */

    /**
     * Return the (singleton) Object, representing the <code> Conditions </code> sub element.
     * 
     * @return the Conditions object.
     */
    public Conditions getConditions();

    /**
     * List the Object representing the <code> Conditions </code> Sub element.
     * 
     * @param conditions the condition to List
     * 
     * @throws IllegalArgumentException if the condition has already been List into another object
     */
    public void setConditions(Conditions conditions) throws IllegalArgumentException;

    /**
     * advice is a (singleton) Object, representing the <code> Advice </code> sub element
     * 
     * @return the advice object in this assertion
     */
    public Advice getAdvice();

    /**
     * List the Object representing the <code> Advice </code> sub element.
     * 
     * @param advice the object to List
     * 
     * @throws IllegalArgumentException if the object has already been put into another SAMLObject
     */
    public void setAdvice(Advice advice) throws IllegalArgumentException;

    /* Multiple Elements */

    /**
     * Return the List representing all the <code> Statement </code> sub elements.
     * 
     * @return the List representing all the statements
     */
    public List<Statement> getStatements();

    /**
     * Return the List representing all the <code> Statement </code> sub elements with a given schema type or element name.
     * 
     * @param the schema type or element name
     * 
     * @return the List representing all the statements
     */
    public List<Statement> getStatements(QName typeOrName);

    /**
     * Return the List representing all the <code> SubjectStatement </code> sub elements.
     * 
     * @return all the SubjectStatements
     */
    public List<SubjectStatement> getSubjectStatements();

    /**
     * Return the List representing all the <code> AuthenticationStatement </code> sub elements.
     * 
     * @return all the AuthenticationStatements
     */
    public List<AuthenticationStatement> getAuthenticationStatements();

    /**
     * Return the List representing all the <code> AuthorizationStatement </code> sub elements.
     * 
     * @return all the authorizationDecisionStatements.
     */
    public List<AuthorizationDecisionStatement> getAuthorizationDecisionStatements();

    /**
     * Add a single <code> AttributeStatement </code> to the List (if appropriate).
     * 
     * @return all the attributeStatements
     */
    public List<AttributeStatement> getAttributeStatements();
}