<!doctype html>
<html>
<head>
    <meta name="layout" content="bootstrap"/>
    <title>gr('ails blog')[aggregator] Administrator</title>
</head>

<body>
<div class="page-header">
    <h1>Welcome To The Grails Blog Aggregator Administrator</h1>
</div>

<div class="row-fluid">
    <div class="span3">
        <div class="well sidebar-nav">
            <h1>Application Status</h1>
            <ul>
                <li>App version: <g:meta name="app.version"/></li>
                <li>Grails version: <g:meta name="app.grails.version"/></li>
                <li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>
                <li>JVM version: ${System.getProperty('java.version')}</li>
                <li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
                <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
                <li>Domains: ${grailsApplication.domainClasses.size()}</li>
                <li>Services: ${grailsApplication.serviceClasses.size()}</li>
                <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
            </ul>

            <h1>Installed Plugins</h1>
            <ul>
                <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                    <li>${plugin.name} - ${plugin.version}</li>
                </g:each>
            </ul>
        </div>
    </div>

    <div class="span9">

        <div>
            <div id="controller-list" role="navigation">
                <h2>Available Controllers:</h2>
                <ul>
                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName }}">
                        <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.naturalName}</g:link></li>
                    </g:each>
                </ul>
            </div>

        </div>
    </div>
</div>

</body>
</html>
