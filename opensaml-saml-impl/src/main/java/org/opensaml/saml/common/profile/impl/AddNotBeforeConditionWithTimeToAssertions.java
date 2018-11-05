package org.opensaml.saml.common.profile.impl;


import com.google.common.base.Function;
import com.google.common.base.Functions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.shibboleth.utilities.java.support.annotation.Duration;
import net.shibboleth.utilities.java.support.annotation.constraint.NonNegative;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;
import org.joda.time.DateTime;
import org.opensaml.messaging.context.navigate.MessageLookup;
import org.opensaml.profile.action.ActionSupport;
import org.opensaml.profile.action.EventIds;
import org.opensaml.profile.context.ProfileRequestContext;
import org.opensaml.profile.context.navigate.OutboundMessageContextLookup;
import org.opensaml.saml.common.SAMLObject;
import org.opensaml.saml.saml1.profile.SAML1ActionSupport;
import org.opensaml.saml.saml2.profile.SAML2ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Date:    02/11/2018
 *
 * @author xin.meng
 * @version 1.0
 */
public class AddNotBeforeConditionWithTimeToAssertions extends AddNotBeforeConditionToAssertions {

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory
            .getLogger(AddNotBeforeConditionWithTimeToAssertions.class);

    /** Strategy used to locate the response to operate on. */
    @Nonnull
    private Function<ProfileRequestContext, SAMLObject> responseLookupStrategy;

    /** Response to modify. */
    @Nullable
    private SAMLObject response;


    /** Strategy to obtain assertion lifetime policy. */
    @Nullable
    private Function<ProfileRequestContext, Long> inAdvanceLifetimeStrategy;

    /** Default in advance to use to establish timestamp. */
    @Duration
    @NonNegative
    private long defaultInAdvanceLifetime;

    /** Constructor. */
    public AddNotBeforeConditionWithTimeToAssertions() {
        responseLookupStrategy =
                Functions
                        .compose(new MessageLookup<>(SAMLObject.class),
                                new OutboundMessageContextLookup());

        defaultInAdvanceLifetime = 0L;
    }

    /**
     * Set the strategy used to locate the Response to operate on.
     *
     * @param strategy lookup strategy
     */
    public void setResponseLookupStrategy(@Nonnull final Function<ProfileRequestContext,SAMLObject> strategy) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);

        responseLookupStrategy = Constraint.isNotNull(strategy, "Response lookup strategy cannot be null");
    }

    /**
     * Set strategy function to obtain assertion lifetime.
     *
     * @param strategy strategy function
     */
    public void setInAdvanceLifetimeStrategy(@Nullable final Function<ProfileRequestContext, Long> strategy) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);

        inAdvanceLifetimeStrategy = strategy;
    }

    /**
     * Set the default assertion lifetime in milliseconds.
     *
     * @param lifetime default lifetime in milliseconds
     */
    @Duration
    public void setDefaultInAdvanceLifetime(@Duration @NonNegative final long lifetime) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);

        defaultInAdvanceLifetime = Constraint.isGreaterThanOrEqual(0, lifetime,
                "Default assertion lifetime must be greater than or equal to 0");
    }

    /** {@inheritDoc} */
    @Override
    protected boolean doPreExecute(@Nonnull final ProfileRequestContext profileRequestContext) {
        log.debug("{} Attempting to add NotBefore condition to every Assertion in outgoing Response", getLogPrefix());

        response = responseLookupStrategy.apply(profileRequestContext);
        if (response == null) {
            log.debug("{} No SAML Response located in current profile request context", getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext, EventIds.INVALID_MSG_CTX);
            return false;
        }

        if (response instanceof org.opensaml.saml.saml1.core.Response) {
            if (((org.opensaml.saml.saml1.core.Response) response).getAssertions().isEmpty()) {
                log.debug("{} No assertions available, nothing to do", getLogPrefix());
                return false;
            }
        } else if (response instanceof org.opensaml.saml.saml2.core.Response) {
            if (((org.opensaml.saml.saml2.core.Response) response).getAssertions().isEmpty()) {
                log.debug("{} No assertions available, nothing to do", getLogPrefix());
                return false;
            }
        } else {
            log.debug("{} Message returned by lookup strategy was not a SAML Response", getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext, EventIds.INVALID_MSG_CTX);
            return false;
        }

        return super.doPreExecute(profileRequestContext);
    }


    /** {@inheritDoc} */
    @Override
    protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext) {

        final Long lifetime = inAdvanceLifetimeStrategy != null ?
                inAdvanceLifetimeStrategy.apply(profileRequestContext) : null;
        if (lifetime == null) {
            log.debug("{} No assertion lifetime supplied, using default", getLogPrefix());
        }

        if (response instanceof org.opensaml.saml.saml1.core.Response) {
            for (final org.opensaml.saml.saml1.core.Assertion assertion :
                    ((org.opensaml.saml.saml1.core.Response) response).getAssertions()) {
                final DateTime inAdvance = new DateTime(
                        ((org.opensaml.saml.saml1.core.Response) response).getIssueInstant()).minus(
                        lifetime != null ? lifetime : defaultInAdvanceLifetime);
                log.debug(
                        "{} Added NotBefore condition, indicating an in advance of {}, to Assertion {}",
                        new Object[]{getLogPrefix(), inAdvance, assertion.getID()});
                SAML1ActionSupport.addConditionsToAssertion(this, assertion)
                        .setNotBefore(inAdvance);
            }
        } else if (response instanceof org.opensaml.saml.saml2.core.Response) {
            for (final org.opensaml.saml.saml2.core.Assertion assertion :
                    ((org.opensaml.saml.saml2.core.Response) response).getAssertions()) {
                final DateTime inAdvance =
                        new DateTime(((org.opensaml.saml.saml2.core.Response) response)
                                .getIssueInstant()).minus(
                                lifetime != null ? lifetime : defaultInAdvanceLifetime);
                log.debug(
                        "{} Added NotBefore condition, indicating an in advance of {}, to Assertion {}",
                        new Object[]{getLogPrefix(), inAdvance, assertion.getID()});
                SAML2ActionSupport.addConditionsToAssertion(this, assertion)
                        .setNotBefore(inAdvance);
            }
        }
    }
}
