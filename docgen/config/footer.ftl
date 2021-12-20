<#if config.hasFooter()>
<footer class="text-center text-lg-start bg-light text-muted fixed-bottom">
  <div class="text-center p-4">
    ${config.footer.copyright}
    <a class="text-reset fw-bold" href="${config.footer.link.url}">${config.footer.link.name}</a>
  </div>
</footer>
</#if>