<%@ page import="com.sharp.agg.feed.Feed" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="bootstrap">
    <g:set var="entityName" value="${message(code: 'feed.label', default: 'Feed')}"/>
    <title><g:message code="default.feed.suggest.label"/></title>
</head>

<body>
<div class="row-fluid">
    <section class="span12">

        <div class="page-header">
            <h1><g:message code="default.feed.suggest.label"/></h1>
        </div>

        <g:if test="${flash.message}">
            <bootstrap:alert class="alert-info">${flash.message}</bootstrap:alert>
        </g:if>

        <g:hasErrors bean="${feedInstance}">
            <bootstrap:alert class="alert-error">
                <ul>
                    <g:eachError bean="${feedInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
            </bootstrap:alert>
        </g:hasErrors>

        <fieldset>
            <g:form class="form-horizontal" action="saveSuggestFeed" id="${feedInstance?.id}">
                <g:hiddenField name="version" value="${feedInstance?.version}"/>
                <fieldset>
                    <f:all bean="feedInstance" except="lastChecked, isApproved, createdBy, entries"/>
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="icon-ok icon-white"></i>
                            <g:message code="default.button.submit.label" default="Submit"/>
                        </button>
                    </div>
                </fieldset>
            </g:form>
        </fieldset>

    </section>

    <%--
    <aside id="application-status" class="span3">
        <g:render template="/shared/sidebars/siteInfoTemplate"/>
        <g:render template="/shared/sidebars/statsTemplate"/>
    </aside>
    --%>
</div>
</body>
</html>
