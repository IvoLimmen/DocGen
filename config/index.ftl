<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title></title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
      <#include "styles.ftl">
    </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
  </head>
<body>
<#include "navigation.ftl">
<#assign h1></#assign>
<#assign h2></#assign>
<#assign h3></#assign>
<#list files as file>  
  <#assign oldh1>${h1}</#assign>
  <#assign h1>${helper.getDirectory(file, 0)}</#assign>
  <#assign oldh2>${h2}</#assign>
  <#assign h2>${helper.getDirectory(file, 1)}</#assign>
  <#assign oldh3>${h3}</#assign>
  <#assign h3>${helper.getDirectory(file, 2)}</#assign>
  <#if file?index == 0 || oldh1 != h1>
    <div class="divider"></div>    
    <a name="#${toLink(helper.getDirectory(file, 0))}"><h1>${helper.getDirectory(file, 0)}</h1></a>
  </#if>
  <#if file?index == 0 || oldh2 != h2>
    <a name="#${toLink(helper.getDirectory(file, 1))}"><h2>${helper.getDirectory(file, 1)}</h2></a>
  </#if>
  <#if file?index == 0 || oldh3 != h3>
    <h3>${helper.getDirectory(file, 2)}</h3>    
  </#if>

  <!-- Show link -->  
  <a href="${helper.toRelativeLink(file)}">${helper.getFileName(file)}</a>
</#list>
<#include "search.ftl">
<#include "footer.ftl">
<#include "javascript.ftl">
</body>
</html>