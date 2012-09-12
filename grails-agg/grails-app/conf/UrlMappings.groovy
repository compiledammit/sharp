class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'home')
        "/admin/manage/$action?"(controller: "adminManage")
        "/adminManage/$action?"(controller: "errors", action: "urlMapping")

        "403"(controller: "errors", action: "accessDenied")
        "404"(controller: "errors", action: "notFound")
        "405"(controller: "errors", action: "notAllowed")
        "500"(controller: "errors", action: "error")
    }
}
