---
layout: null
---

var idx = lunr(function () {
  this.field('title')
  this.field('excerpt')
  this.field('categories')
  this.field('tags')
  this.field('collection')
  this.ref('id')

  this.pipeline.remove(lunr.trimmer)

  for (var item in store) {
    this.add({
      title: store[item].title,
      excerpt: store[item].excerpt,
      categories: store[item].categories,
      tags: store[item].tags,
      collection: store[item].collection,
      id: item
    })
  }
});




$(document).ready(function() {
  $('input#search').on('keyup', function (event) {
    searchResults(event.target);
  }); // keyup
}); // doc ready

function searchResults(theSearchInput) {
  var searchResults=[];
  var resultdiv = $('#results');
  var query = $(theSearchInput).val().toLowerCase();
  var result =
    idx.query(function (q) {
      query.split(lunr.tokenizer.separator).forEach(function (term) {
        q.term(term, { boost: 100 })
        if(query.lastIndexOf(" ") != query.length-1){
          q.term(term, {  usePipeline: false, wildcard: lunr.Query.wildcard.TRAILING, boost: 10 })
        }
        if (term != ""){
          q.term(term, {  usePipeline: false, editDistance: 1, boost: 1 })
        }
      })
    });
  resultdiv.empty();
  resultdiv.prepend('<div class="numberOfResults"><p class="results__found secondary">'+result.length+' {{ site.data.ui-text[site.locale].results_found | default: "Result(s) found" }}</p></div>');


  let searchJson = {};
  let uniqueTitles = [];
  let uniqueExcerptsPerTitle = {};

  result.forEach((searchResult) => {
    let documentItem = store[searchResult.ref];
    let documentTitle = documentItem.title;
    let documentID = documentTitle.replace(/ /g, '_');//searchResult.ref;
    let documentationVersion = documentItem.collection;
    let documentIsTutorial = documentationVersion === 'tutorials';
    let documentIsSupport = documentationVersion === 'support';

    let obj = {
      id: documentID,
      title: documentTitle,
      excerpt: documentItem.excerpt.split(" ").splice(0,20).join(" "), // get first 20 words only
      url: documentItem.url,
      version:documentItem.collection,
      documentIsTutorial,
      documentIsSupport,
      versionYear: 0,
      versionQuarter: 0,
      versionNumber: 0
    };

    if(documentIsSupport === false && documentIsTutorial === false) {
      let versionParts =  documentationVersion.split('.');
      obj.versionYear = versionParts[0];
      obj.versionQuarter = versionParts[1];
      obj.versionNumber = versionParts[2];
    }

    if (uniqueTitles.indexOf(documentID) !== -1) {
       // This documentation exists, add it to the existing array
        searchJson[documentID].push(obj);

    } else {
      // This didnt exist before, adding a new one
      searchJson[documentID] = [obj];
      //searchJson[documentID][documentationVersion] = obj;
      uniqueTitles.push(documentID);
      uniqueExcerptsPerTitle[documentID] = [documentationVersion];
    }
  });

  var structure = "";


  let result_keys = Object.keys(searchJson);

  result_keys.forEach((documentTopic) => {
    let sortedTopicsByVersion = searchJson[documentTopic].sort((objA, objB) => {
      let result = 0;

      if(objA.versionYear > objB.versionYear) {
        result = -1;
      } else if(objA.versionYear < objB.versionYear) {
        result = 1;
      } else if(objA.versionQuarter > objB.versionQuarter) {
        result = -1;
      } else if(objA.versionQuarter < objB.versionQuarter) {
        result = 1;
      } else if(objA.versionNumber > objB.versionNumber) {
        result = -1;
      } else if(objA.versionNumber < objB.versionNumber) {
        result = 1;
      }

      return result;
    });

    let title = sortedTopicsByVersion[0].title;
    let excerpt = sortedTopicsByVersion[0].excerpt;

    structure += '<h2>'+title+'</h2>'
    sortedTopicsByVersion.forEach((topic, index) => {
      if(index === 0 || excerpt !== topic.excerpt){ // first results or if the excerpt is new
        let currentVersion = topic.excerpt;
        let currentIndex = index;
        let url = sortedTopicsByVersion[currentIndex].url;
        structure += '<div class="card" onclick="pushHistoryState(this);window.open(\''+url+'\',\'_self\')">'
        structure += '<p>'+topic.excerpt+'...</p>'
        structure += '<div class="versionContainer">'
        structure += '<p class="foundIn heading-five">Found in versions:</h5><br>'
        sortedTopicsByVersion.forEach((topic, index) => {
          if ( currentVersion == topic.excerpt ) {
            let initial = "";
            if ( index == currentIndex ) {
              initial = "initial";
            }
            structure += '<div class="versionPillContainer '+initial+'" onMouseOver="pillHover(this);" onMouseOut="pillBlur(this);" href="'+topic.url+'">'
            structure += '<a class="versionPill" href="'+topic.url+'">'+topic.version+'</a>'
            structure += '</div>'
          }
        });
        structure += '<div class="ctaArea"><img alt="" role="presentation" src="{{site.url}}{{ site.baseurl }}/assets/images/icons/next.svg" /></div>'
        structure += '</div>'
        structure += '</div>'
      }


    });
  });
  resultdiv.append(structure)
}

var pillContainer = document.querySelector('.pillContainer');

function pillHover(currentPill) {
    let parentContainer = currentPill.parentElement
    let versionContainer = parentContainer.parentElement
    let children = parentContainer.childNodes
    var ctaContainer = versionContainer.querySelectorAll('.ctaArea')
    for (var i = 0; i < children.length; i++) {
      if ( currentPill != children[i] ) {
        children[i].classList.add("inactive");
        ctaContainer[0].classList.add("hide");
      }
    }
}

function pillBlur(currentPill) {
  let parentContainer = currentPill.parentElement
  let versionContainer = parentContainer.parentElement
  let children = parentContainer.childNodes
  var ctaContainer = versionContainer.querySelectorAll('.ctaArea')
  for (var i = 0; i < children.length; i++) {
    children[i].classList.remove("inactive");
    ctaContainer[0].classList.remove("hide");
  }
}


function pushHistoryState(theSearchInput) {
  var query = document.getElementById('search').value.toLowerCase();
  var stateObj = { foo: "bar" };
  history.pushState(stateObj, "Search Documentation", "?q="+query);
}
