// function urlAssistant(matchedURL,baseURL) {
//     var allVersions = document.getElementsByClassName('versionContainerURLs');
//     for (var i = 0; i < allVersions.length; i++) {
//         var allURLs = allVersions[i].querySelectorAll('p');
//         for (var inc = 0; inc < allURLs.length; inc++) {
//             var thisURL = allURLs[inc].innerText;
//             // console.log(thisURL);
//             if ( thisURL.includes(matchedURL)  ) {
//                 // console.log(thisURL + " contains: " + matchedURL);
//                 makeContainer(allVersions[i].dataset.version,baseURL+thisURL);
//             } 
//         }
//     }
//     if (i == allVersions.length) {
//         document.getElementById('siteURLS').remove();
//     }
// }




function makeContainer(version,url) {
    var pageContainer = document.getElementById('otherVersions');



    var a = document.createElement('a');
    var linkText = document.createTextNode(version);
    a.appendChild(linkText);
    a.title = version
    a.href = url
    pageContainer.appendChild(a);


}