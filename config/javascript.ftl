<#if config.isIncludeSearch()>
<script>
  function soundex(name) {
    let s = [];
    let si = 1;
    let c;

    //              ABCDEFGHIJKLMNOPQRSTUVWXYZ
    let mappings = "01230120022455012623010202";
    s[0] = name[0].toUpperCase();

    for(let i = 1; i < name.length; i++) {
      c = (name[i].toUpperCase()).charCodeAt(0) - 65;
      if(c >= 0 && c <= 25) {
        if(mappings[c] != '0') {
          if(mappings[c] != s[si-1]) {
            s[si] = mappings[c];
            si++;
          }
          if(si > 3) {
            break;
          }
        }
      }
    }

    if(si <= 3) {
      while(si <= 3) {
        s[si] = '0';
        si++;
      }
    }

    return s.join("");
  } 

  function createLink(link, keyword) {
    let li = document.createElement('li');
    let a = document.createElement('a');
    let title = keyword + ': ' + link.sectionName;
    a.title = title;
    a.href = link.targetFile + '#' + link.link;
    a.textContent = title;
    li.appendChild(a);
    return li;
  }

  function search() {
    let keyword = document.querySelector('#searchInput').value;
    let search = soundex(keyword);
    fetch('data/' + keyword[0] + '.json')
      .then(response => response.json())
      .then(data => { 
        
        let element = document.getElementById('searchResponse');
        element.replaceChildren();
        let ul = document.createElement('ul');
        element.appendChild(ul);

        let found = false;
        data.forEach((result) => {
          if (result.keyword === keyword) {            
            found = true;
            result.links.forEach((match) => {
              ul.appendChild(createLink(match, result.keyword));
            });
          } else if (result.soundex === search) {
            found = true;
            result.links.forEach((match) => {
              ul.appendChild(createLink(match, result.keyword));
            });
          }
        });

        if (!found) {
          element.replaceChildren();
          let p = document.createElement('p');
          p.textContent = 'No results for "' + keyword + '"';
          element.appendChild(p);
        }

        let searchModal = new bootstrap.Modal(document.getElementById('searchModal'));
        searchModal.show();
      }).catch(error => {

        let element = document.getElementById('searchResponse');
        element.replaceChildren();
        let p = document.createElement('p');
        p.textContent = 'No results for "' + keyword + '"';
        element.appendChild(p);

        let searchModal = new bootstrap.Modal(document.getElementById('searchModal'));
        searchModal.show();
      });
  }

  document.getElementById('searchInput').onkeyup = (event) => {
    if (event.keyCode === 13) {
        search();
    }
  };

  document.onkeyup = function (event) {
    if (event.key === '/') {
        document.getElementById('searchInput').focus();
    }
  };

  document.getElementById('searchButton').onclick = search;

</script>
</#if>