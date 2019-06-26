<!DOCTYPE HTML>
<html>
    <head>
        <title>${title!'Counter Clinic'}</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/skeleton/2.0.4/skeleton.min.css" type="text/css"/>
        <#if view.stylesheet??>
            <link href="${view.stylesheet}" type="text/css"/>
        </#if>
    </head>
    <body>
        <#include view.page + "/" + view.component + ".ftl" >

        <#if view.script??>
            <script src="${view.script}"></script>
        </#if>
    </body>
</html>