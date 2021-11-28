<html>
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
    <h1>${helper.getDirectory(file, 0)}</h1>    
  </#if>
  <#if file?index == 0 || oldh2 != h2>
    <h2>${helper.getDirectory(file, 1)}</h2>    
  </#if>
  <#if file?index == 0 || oldh3 != h3>
    <h3>${helper.getDirectory(file, 2)}</h3>    
  </#if>

  <!-- Show link -->  
  <a href="${helper.toRelativeLink(file)}">${helper.getFileName(file)}</a>
</#list>
</html>