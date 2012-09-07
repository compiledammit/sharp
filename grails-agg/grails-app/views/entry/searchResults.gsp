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
            <h1>Search results for '${params.q}' (${entriesTotal} result<g:if test="${entriesTotal != 1}">s</g:if>)</h1>
        </div>

        <g:if test="${entriesTotal == 0}">
            <g:if test="${suggest}">
                <p>
                    What about trying <a href="${createLink(action: 'search', controller: 'entry', params: [q:suggest])}">${suggest}</a>?
                </p>
            </g:if>
        </g:if>

        <g:render template="/entry/entryListTemplate" model="[entries: entries, q: params?.q, action: 'search', entriesTotal: entriesTotal]"/>

    </section>

    <aside id="application-status" class="span3">
        <g:render template="/shared/sidebars/searchBoxTemplate"/>
        <g:render template="/shared/sidebars/siteInfoTemplate"/>
        <g:render template="/shared/sidebars/statsTemplate"/>
    </aside>
</div>
</body>
</html>
