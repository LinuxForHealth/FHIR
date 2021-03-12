var request;
var loadedContent;
var filterStorage;
var sinkSourceArrowAnimationLoaded = false;

let FILTER_TYPES = {
  SUPPORT: 'supportLevelFilter',
  TYPE: 'typeLevelFilter'
};

function makeRequest(url, callback) {
  if (request) {
    request.abort()
  }

  request = new XMLHttpRequest();
  request.open('GET', url, true);

  let timeOfStartingRequest;

  request.onreadystatechange = function () {
    if (this.readyState === 4) {
      request = null
      let responseTime = Date.now() - timeOfStartingRequest;
      if (this.status >= 200 && this.status < 400) {
        let parser = new DOMParser();
        let doc = parser.parseFromString(this.responseText, 'text/html');
        loadedContent = doc.getElementById('contentLoadContainer').innerHTML;

        callback(doc, null, responseTime)
      } else {
        callback(null, new Error('Request failed'), responseTime);
      }
    }
  };

  timeOfStartingRequest = Date.now();
  request.send();
}

function removeClassFrom(item, className) {
  item.classList.remove(className);
}

function removeClassFromCollection(collection, className) {
  [...collection].map(item => removeClassFrom(item, className));
}

function addClassTo(element, className) {
  element.classList.add(className);
}

function addClassToCollection(collection, className) {
  [...collection].map(item => addClassTo(item, className));
}

function isActive(element) {
  return element.classList.contains('active');
}

function getById(id) {
  return document.getElementById(id);
}

function getByClassName(className) {
  return document.getElementsByClassName(className);
}

function connectorCategory() {
  getById('connectorsContent').classList.remove('show');
}

function loadConnector(url) {
  makeRequest(url, (response, err) => {
    if(err) { throw err }

    let connectorContent = response.getElementById('initial-content').innerHTML;
    getById('connectorsPage').innerHTML = connectorContent
    getById('connectorsContent').classList.remove('hide');
  });
}

function connectorCard(card, url) {
  let selectedCard = card.parentElement;
  let isAlreadyActive = isActive(selectedCard);
  let allConnectorCards = getByClassName('buttonContainer');

  removeClassFromCollection(allConnectorCards, 'active');
  addClassTo(selectedCard, 'active');

  if(!isAlreadyActive) {
    setTimeout(() => {
      addClassTo(getById('connectorsAll'), 'connectorOpen');
    }, 10)

    sinkSourceArrowAnimationLoaded = false;

    makeRequest(url, (response, err, responseTime) => {
      if(err) { throw err }

      let loadedContent = response.getElementById('contentLoadContainer').innerHTML;

      if (responseTime < 400) {
        setTimeout(() => {
          getById('contentLoadContainer').innerHTML = loadedContent
        }, 400)
      } else {
        getById('contentLoadContainer').innerHTML = loadedContent
      }

      setTimeout(() => {
        removeClassFrom(getById('connectorsPage'), 'loading');
      }, 450)

      setActiveCategory(sessionStorage.connectorCategory);
    });

    getById('connectorsPage').classList.add('loading');

    history.pushState(null, null, url);
    addClassTo(getById('connectorsContent'), 'show');
    testFixHeight();
  }

  setActiveCategory(sessionStorage.connectorCategory);
}

function testFixHeight() {
  if (getWidth() <= 1024) {
    document.body.scrollTop = document.documentElement.scrollTop = 0;
    setTimeout(() => {
      addClassTo(getById('connectorsAll'), 'connectorOpen');
    }, 10)
  }
}

function subPageChange(selectedTab, url) {
  // set clicked card to active
  let allSubTabs = getByClassName('tab');
  removeClassFromCollection(allSubTabs, 'active');
  addClassTo(selectedTab, 'active');

  sinkSourceArrowAnimationLoaded = false;

  addClassTo(getById('subPageContent'), 'loading');

  makeRequest(url, (response, err, responseTime) => {
    if(err) { throw err }

    loadedContent = response.getElementById('thePageContent').innerHTML;

    if (responseTime < 400) {
      addClassTo(getById('subPageContent'), 'loading');
      setTimeout(() => {
        getById('thePageContent').innerHTML = loadedContent
      }, 400 - responseTime)
      setTimeout(() => {
        removeClassFrom(getById('subPageContent'), 'loading');
      }, 410)

    } else {
      getById('thePageContent').innerHTML = loadedContent
      setTimeout(() => {
        removeClassFrom(getById('subPageContent'), 'loading');
      }, 400)
    }

    setActiveCategory(sessionStorage.connectorCategory);
  })

  history.pushState(null, null, url);
  addClassTo(getById('connectorsContent'), 'show');
}

function fixSplashHeight(set) {
  if (set) {
    // get splashPanel height
    let splashPanelHeight = getById('splashPanel').clientHeight;
    // provide it's height
    getById('splashPanel').setAttribute('style', 'height:' + splashPanelHeight + 'px');

    let splashPanelWidth = getById('connectorsCardsContainer').clientWidth;
    // provide it's height
    getById('mobileSplashPanel').setAttribute('style', 'width:' + splashPanelWidth + 'px');
    getById('connectorsCards').setAttribute('style', 'width:' + splashPanelWidth + 'px');

    setTimeout(() => {
      getById('connectorsAll').classList.add('connectorOpen');
    }, 10)
  } else {
    getById('splashPanel').removeAttribute('style');
  }
}

function filterCategories(slug, connectorsHomeURL) {
  setActiveCategory(slug);

  sessionStorage.connectorCategory = slug;
  sessionStorage.categoryURL = connectorsHomeURL;
}

function setActiveCategory(slug) {
  getById('connectorsCards').removeAttribute('class');
  addClassTo(getById('connectorsCards'), 'connectorsCards');
  addClassTo(getById('connectorsCards'), slug);

  getById('connectorsCategories').removeAttribute('class');
  addClassTo(getById('connectorsCategories'), 'connectorsCategories');
  addClassTo(getById('connectorsCategories'), slug);

  countVisibleCards();
}

function toggleTag(object, group, tagID) {
  clearTagActiveStates(group);

  getTagStorage(group);
  if (filterStorage === tagID) {
    clearTagStorage(group);
  } else {
    setTag(object, group, tagID)
  }

  calculateFilters();
}

function calculateFilters() {
  calculateFilterTags();
  calculateFilterCards();
  countVisibleCards();
}

function filterPageLoadCheck() {
  if (sessionStorage.typeFilter || sessionStorage.supportLevelFilter) {
    toggleFilterArea('open');
  }
}

function calculateFilterTags() {
  let supportFilter = sessionStorage.supportLevelFilter
  let typeFilter = sessionStorage.typeFilter

  if (supportFilter) {
    addClassTo(getById(supportFilter), 'active');
  }

  if (typeFilter) {
    addClassTo(getById(typeFilter), 'active');
  }

  if (typeFilter || supportFilter) {
    getById('cardsContainer').classList.add('filterActive')
    addClassTo(getById('filterArea'), 'visible');
    addClassTo(getById('filterToggle'), 'visible');
    setFilterTabIndex(0);

    addClassTo(getById('filterToggle'), 'filterActive');
    addClassTo(getById('filterArea'), 'filterActive');
  } else {
    removeClassFrom(getById('cardsContainer'), 'filterActive');
    removeClassFrom(getById('filterArea'), 'filterActive');
    removeClassFrom(getById('filterToggle'), 'filterActive');
  }
}

function calculateFilterCards() {
  let supportFilter = sessionStorage.supportLevelFilter
  let typeFilter = sessionStorage.typeFilter
  let supportFilterQuery = ''
  let typeFilterQuery = '';

  if (supportFilter) {
    supportFilterQuery = '.' + sessionStorage.supportLevelFilter
  }

  if (typeFilter) {
    typeFilterQuery = '.' + sessionStorage.typeFilter
  }

  addClassToCollection(getByClassName('buttonContainer'), 'hide');
  let query = supportFilterQuery + typeFilterQuery;
  if (query) {
    let queriedCards = document.querySelectorAll(query);

    if (queriedCards) {
      removeClassFromCollection(queriedCards, 'hide');
    }

    countVisibleCards();
  }
}

function countVisibleCards() {
  addClassTo(getById('connectorsEmptyState'), 'hide');
  let visibleCards = 0;
  let allCards = [...getById('cardsContainer').children];

  allCards.map(card => {
    if (window.getComputedStyle(card).display !== 'none') {
      visibleCards++;
    }
  });

  if (!visibleCards) {
    getById('connectorsEmptyState').classList.remove('hide');
  }
}

function resetAllTags() {
  sessionStorage.removeItem(FILTER_TYPES.SUPPORT);
  sessionStorage.removeItem(FILTER_TYPES.TYPE);
}

function getTagStorage(key) {
  switch(key) {
    case FILTER_TYPES.SUPPORT:
      filterStorage = sessionStorage.supportLevelFilter; break;
    case FILTER_TYPES.TYPE:
      filterStorage = sessionStorage.typeFilter; break;
  }
}

function clearTagStorage(group) {
  switch(group) {
    case FILTER_TYPES.SUPPORT:
      sessionStorage.removeItem(FILTER_TYPES.SUPPORT);
      clearTagActiveStates(group);
      break;
    case FILTER_TYPES.TYPE:
      sessionStorage.removeItem(FILTER_TYPES.TYPE);
      clearTagActiveStates(group);
      break;
    default:
      sessionStorage.removeItem(FILTER_TYPES.SUPPORT);
      sessionStorage.removeItem(FILTER_TYPES.TYPE);
      clearTagActiveStates();
      break;
  }
}

function clearTagActiveStates(group) {
  let allToggles;

  if (group) {
    let groupID = document.getElementById(group);
    allToggles = groupID.getElementsByClassName('tagToggle');
  } else {
    allToggles = getByClassName('tagToggle');
  }

  removeClassFromCollection(allToggles, 'active');
}

function setTag(object, key, value) {
  switch(key) {
    case FILTER_TYPES.SUPPORT:
      sessionStorage.supportLevelFilter = value; break;
    case FILTER_TYPES.TYPE:
      sessionStorage.typeFilter = value; break;
  }

  addClassTo(object, 'active');
}

function changeSubpage(index, url) {
  window.open(url, '_self');
}

function toggleSplash(defaultURL) {
  removeClassFrom(getById('connectorsAll'), 'connectorOpen');
  let categoryBaseURL = sessionStorage.categoryURL || defaultURL;
  let allConnectorCards = getByClassName('buttonContainer');
  removeClassFromCollection(allConnectorCards, 'active');

  history.pushState(null, null, categoryBaseURL);
}

function loadSinkSourceAnimation() {
  if (!sinkSourceArrowAnimationLoaded) {
    let svgContainer = getById('sinkSourceArrow');
    if (svgContainer) {
      sinkSourceArrowAnimation = bodymovin.loadAnimation({
        wrapper: svgContainer,
        animType: 'svg',
        loop: true,
        autoplay: true,
        path: '../../assets/animation/sinkSourceArrow.json'
      });
      sinkSourceArrowAnimation.setSpeed(1.5);
      svgContainer.innerHTML = '';
      sinkSourceArrowAnimationLoaded = true;
    }
  }
}

window.addEventListener('popstate', (event) => {
  window.location.href = window.location.href;
});

function clearFilterArea() {
  getById('filterIcon').focus();
  removeClassFrom(getById('filterArea'), 'filterActive');
  removeClassFrom(getById('filterToggle'), 'filterActive');

  clearTagStorage();
  calculateFilters();
}

function toggleFilterArea(override) {
  let isFilterOpen;

  addClassTo(getById('filterArea'), 'animate');
  addClassTo(getById('filterToggle'), 'visible');

  switch(override) {
    case 'open':
      isFilterOpen = false; break;
    case 'close':
      isFilterOpen = true; break;
    default: 
      isFilterOpen = getById('filterArea').classList.contains('visible'); break;
  }

  if (isFilterOpen) {
    // if a filter is not active, then close the panel
    removeClassFrom(getById('filterArea'), 'visible');
    removeClassFrom(getById('filterToggle'), 'visible');
    setFilterTabIndex(-1);
    getById('filterIcon').focus();
  } else {
    getById('filterArea').classList.add('visible');
    setFilterTabIndex(0);
  }
}

function setFilterTabIndex(tabIndexNumber = 0) {
  let allFiltersTabs = [...document.getElementsByClassName('tagToggle')];
  allFiltersTabs.map(tab => tab.tabIndex = tabIndexNumber);

  let triggerArea = [...document.getElementsByClassName('filterTriggerArea')];
  triggerArea.map(area => area.tabIndex = tabIndexNumber);
}

function mobileBackButton(defaultURL) {
  let categoryHomeURL = sessionStorage.categoryURL || defaultURL;

  removeClassFrom(getById('connectorsAll'), 'connectorOpen');

  getById('mainConnectorContent').remove();

  let allConnectorCards = getByClassName('buttonContainer');
  removeClassFromCollection(allConnectorCards, 'active');
  
  history.pushState(null, null, categoryHomeURL);

  setTimeout(() => {
    fixSplashHeight(false);
  }, 400)
}

(function () {
  // ======== SCROLL FUNCTION ======== //
  const scrollTo = (element, to, duration) => {
    if (duration <= 0) {
      return;
    }
    var difference = to - element.scrollTop;
    var perTick = difference / duration * 10;

    setTimeout(() => {
      element.scrollTop = element.scrollTop + perTick;
      if (element.scrollTop === to) {
        return;
      }
      scrollTo(element, to, duration - 10);
    }, 10);
  }
});
