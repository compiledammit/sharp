<g:each in="${entries}" var="entry">
    <div class="well">
        <div class="row-fluid">
            <div class="span12">
                <h2><g:link controller="entry" action="go" id="${entry.id}" target="_blank">${entry.title}</g:link></h2>

                <div>
                    <p>
                        <g:link controller="entry" action="byFeed" id="${entry.feed.id}">${entry.feed.title}</g:link>
                    &bull;
                        <prettytime:display date="${entry.postedOn}"/>
                    &bull;
                        <g:each in="${entry.categories}" var="cat" status="s">
                            <g:link controller="entry" action="byCategory" id="${cat.id}">${cat.category}</g:link><g:if test="${s != entry.categories.size() - 1}">,</g:if>
                        </g:each>
                    &bull;
                        ${entry.getViewCount()} View<g:if test="${entry.getViewCount() != 1}">s</g:if>
                    </p>
                </div>
            </div>
        </div>

        <p>
            ${entry.contents.encodeAsHTML()}
        </p>
    </div>
</g:each>

<div class="pagination">
    <bootstrap:paginate action="${action ?: 'index'}" params="${params}" id="${id ?: ''}" total="${entriesTotal}"/>
</div>