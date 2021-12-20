<#if config.isIncludeNavigation() || config.isIncludeSearch()>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
      <div class="collapse navbar-collapse" id="navbarSupportedContent">

<#if config.isIncludeNavigation()>      
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">

<#assign h1></#assign>
<#list files as file>  
  <#assign oldh1>${h1}</#assign>
  <#assign h1>${helper.getDirectory(file, 0)}</#assign>
  <#if file?index == 0 || oldh1 != h1>
    <li class="nav-item">
      <a class="nav-link active" aria-current="page" href="#${toLink(helper.getDirectory(file, 0))}">${helper.getDirectory(file, 0)}</a>
    </li>
  </#if>
</#list>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Products
            </a>            
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
<#assign h2></#assign>
<#list files as file>  
  <#assign oldh2>${h2}</#assign>
  <#assign h2>${helper.getDirectory(file, 1)}</#assign>
  <#if file?index == 0 || oldh2 != h2>
    <li><a class="dropdown-item" href="#${toLink(helper.getDirectory(file, 1))}">${helper.getDirectory(file, 1)}</a></li>
  </#if>
</#list>
            </ul>
          </li>
        </ul>
</#if>        
<#if config.isIncludeSearch()>
        <div class="d-flex">
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" id="searchInput">
          <button class="btn btn-outline-success" id="searchButton">Search</button>
        </div>
</#if>
      </div>
    </div>
  </nav>
</#if>