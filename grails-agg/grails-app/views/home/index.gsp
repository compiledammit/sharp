<!doctype html>
<html>
<head>
    <meta name="layout" content="bootstrap"/>
    <title>gr('ails blog')[aggregator]</title>
</head>

<body>

<div class="row-fluid">

    <section id="main" class="span9">

        <div class="page-header">
            <h1>Recent Entries</h1>
        </div>

        <g:if test="${flash.message}">
            <bootstrap:alert class="alert-info">${flash.message}</bootstrap:alert>
        </g:if>

        <g:render template="/entry/entryListTemplate" model="[entries: entries, entriesTotal: entriesTotal]"/>

    </section>

    <aside id="application-status" class="span3">
        <g:render template="/shared/sidebars/siteInfoTemplate"/>
        <g:render template="/shared/sidebars/statsTemplate" />
    </aside>
</div>
</body>
</html>
