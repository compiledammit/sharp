package com.sharp.agg.feed

import grails.test.ControllerUnitTestCase

import java.text.MessageFormat

/**
 * Created with IntelliJ IDEA.
 * User: 558848
 * Date: 9/12/12
 * Time: 9:04 AM
 * To change this template use File | Settings | File Templates.
 */

/*
    the funkiness in setUp and mockI18N() is courtesy of http://jira.grails.org/browse/GRAILS-5926?focusedCommentId=55980&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-55980
 */
class BaseControllerTestCase extends ControllerUnitTestCase {
    def props

    protected void setUp() {
        super.setUp()

        props = new Properties()
        def stream = new FileInputStream("grails-app/i18n/messages.properties")
        props.load stream
        stream.close()

        mockI18N(controller)
    }

    protected void tearDown() {
        super.tearDown()
    }

    def mockI18N = { controller ->
        controller.metaClass.message = { Map map ->
            if (!map.code)
                return ""
            if (map.args) {
                def formatter = new MessageFormat("")
                formatter.applyPattern props.getProperty(map.code)
                return formatter.format(map.args.toArray())
            } else {
                return props.getProperty(map.code)
            }
        }
    }
}

